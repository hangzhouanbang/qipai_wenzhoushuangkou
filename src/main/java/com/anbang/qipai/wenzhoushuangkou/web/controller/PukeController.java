package com.anbang.qipai.wenzhoushuangkou.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeActionResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.PukePlayCmdService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.service.PukeGameQueryService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.service.PukePlayQueryService;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.MajiangHistoricalPanResult;
import com.anbang.qipai.wenzhoushuangkou.msg.service.WenzhouShuangkouGameMsgService;
import com.anbang.qipai.wenzhoushuangkou.msg.service.WenzhouShuangkouResultMsgService;
import com.anbang.qipai.wenzhoushuangkou.web.vo.CommonVO;
import com.anbang.qipai.wenzhoushuangkou.web.vo.PanActionFrameVO;
import com.anbang.qipai.wenzhoushuangkou.websocket.GamePlayWsNotifier;
import com.anbang.qipai.wenzhoushuangkou.websocket.QueryScope;
import com.dml.shuangkou.pan.PanActionFrame;

@RestController
@RequestMapping("/pk")
public class PukeController {

	@Autowired
	private PlayerAuthService playerAuthService;

	@Autowired
	private PukePlayCmdService pukePlayCmdService;

	@Autowired
	private PukePlayQueryService pukePlayQueryService;

	@Autowired
	private PukeGameQueryService pukeGameQueryService;

	@Autowired
	private WenzhouShuangkouResultMsgService wenzhouShuangkouResultMsgService;

	@Autowired
	private WenzhouShuangkouGameMsgService gameMsgService;

	@Autowired
	private GamePlayWsNotifier wsNotifier;

	/**
	 * 当前盘我应该看到的所有信息
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/pan_action_frame_for_me")
	@ResponseBody
	public CommonVO panactionframeforme(String token, String gameId) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PanActionFrame panActionFrame;
		try {
			panActionFrame = pukePlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId, playerId);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}
		data.put("panActionFrame", new PanActionFrameVO(panActionFrame));
		return vo;
	}

	@RequestMapping(value = "/action")
	@ResponseBody
	public CommonVO action(String token, List<Integer> paiIds, String dianshuZuheIdx) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		List<String> queryScopes = new ArrayList<>();
		data.put("queryScopes", queryScopes);
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		PukeActionResult pukeActionResult;
		try {
			pukeActionResult = pukePlayCmdService.action(playerId, paiIds, dianshuZuheIdx, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			pukePlayQueryService.action(pukeActionResult);
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}

		if (pukeActionResult.getPanResult() == null) {// 盘没结束
			queryScopes.add(QueryScope.panForMe.name());
		} else {// 盘结束了
			String gameId = pukeActionResult.getPukeGame().getId();
			PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
			if (pukeActionResult.getJuResult() != null) {// 局也结束了
				JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
				MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, pukeGameDbo);
				wenzhouShuangkouResultMsgService.recordJuResult(juResult);

				gameMsgService.gameFinished(gameId);
				queryScopes.add(QueryScope.juResult.name());
			} else {
				queryScopes.add(QueryScope.panResult.name());
				queryScopes.add(QueryScope.gameInfo.name());
			}
			PanResultDbo panResultDbo = pukePlayQueryService.findPanResultDbo(gameId,
					pukeActionResult.getPanResult().getPan().getNo());
			MajiangHistoricalPanResult panResult = new MajiangHistoricalPanResult(panResultDbo, pukeGameDbo);
			wenzhouShuangkouResultMsgService.recordPanResult(panResult);
			gameMsgService.panFinished(pukeActionResult.getPukeGame(),
					pukeActionResult.getPanActionFrame().getPanAfterAction());

		}
		// 通知其他人
		for (String otherPlayerId : pukeActionResult.getPukeGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				QueryScope.scopesForState(pukeActionResult.getPukeGame().getState(),
						pukeActionResult.getPukeGame().findPlayerState(otherPlayerId)).forEach((scope) -> {
							wsNotifier.notifyToQuery(otherPlayerId, scope.name());
						});
			}
		}

		return vo;
	}

	@RequestMapping(value = "/ready_to_next_pan")
	@ResponseBody
	public CommonVO readytonextpan(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		ReadyToNextPanResult readyToNextPanResult;
		try {
			readyToNextPanResult = pukePlayCmdService.readyToNextPan(playerId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		try {
			pukePlayQueryService.readyToNextPan(readyToNextPanResult);
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}

		PanActionFrame firstActionFrame = readyToNextPanResult.getFirstActionFrame();
		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (firstActionFrame != null) {
			queryScopes.add(QueryScope.panForMe);
		}
		data.put("queryScopes", queryScopes);

		// 通知其他人
		for (String otherPlayerId : readyToNextPanResult.getPukeGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				List<QueryScope> scopes = QueryScope.scopesForState(readyToNextPanResult.getPukeGame().getState(),
						readyToNextPanResult.getPukeGame().findPlayerState(otherPlayerId));
				scopes.remove(QueryScope.panResult);
				scopes.forEach((scope) -> {
					wsNotifier.notifyToQuery(otherPlayerId, scope.name());
				});
			}
		}
		return vo;
	}
}
