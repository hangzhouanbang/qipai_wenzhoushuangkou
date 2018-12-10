package com.anbang.qipai.wenzhoushuangkou.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PanActionFramePlayerViewFilter;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ChaodiResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.PukeActionResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ReadyForGameResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ReadyToNextPanResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.WenzhouShuangkouPanResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state.StartChaodi;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state.VoteNotPassWhenChaodi;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state.VotingWhenChaodi;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.GameLatestPanActionFrameDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.PanActionFrameDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.PukeGameDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.PukeGamePlayerChaodiDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanActionFrameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGamePlayerChaodiDbo;
import com.anbang.qipai.wenzhoushuangkou.plan.bean.PlayerInfo;
import com.anbang.qipai.wenzhoushuangkou.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;
import com.dml.shuangkou.pan.PanActionFrame;

@Service
public class PukePlayQueryService {

	@Autowired
	private PukeGameDboDao pukeGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private PanResultDboDao panResultDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private PanActionFrameDboDao panActionFrameDboDao;

	@Autowired
	private GameLatestPanActionFrameDboDao gameLatestPanActionFrameDboDao;

	@Autowired
	private PukeGamePlayerChaodiDboDao pukeGamePlayerChaodiDboDao;

	private PanActionFramePlayerViewFilter pvFilter = new PanActionFramePlayerViewFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		PukeGameDbo pukeGameDbo = pukeGameDboDao.findById(gameId);
		if (!(pukeGameDbo.getState().name().equals(Playing.name)
				|| pukeGameDbo.getState().name().equals(VotingWhenPlaying.name)
				|| pukeGameDbo.getState().name().equals(VoteNotPassWhenPlaying.name)
				|| pukeGameDbo.getState().name().equals(StartChaodi.name)
				|| pukeGameDbo.getState().name().equals(VotingWhenChaodi.name)
				|| pukeGameDbo.getState().name().equals(VoteNotPassWhenChaodi.name))) {
			throw new Exception("game not playing");
		}

		GameLatestPanActionFrameDbo frame = gameLatestPanActionFrameDboDao.findById(gameId);
		PanActionFrame panActionFrame = pvFilter.filter(frame, playerId, pukeGameDbo.isShuangming());
		return panActionFrame;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Throwable {
		PukeGameValueObject pukeGame = readyForGameResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

		if (pukeGame.getState().name().equals(StartChaodi.name)) {
			PukeGamePlayerChaodiDbo dbo = new PukeGamePlayerChaodiDbo();
			dbo.setGameId(pukeGame.getId());
			dbo.setPanNo(pukeGame.getPanNo());
			dbo.setPlayerChaodiStateMap(pukeGame.getPlayerChaodiStateMap());
			pukeGamePlayerChaodiDboDao.addPukeGamePlayerChaodiDbo(dbo);
		}

		if (readyForGameResult.getFirstActionFrame() != null) {
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			gameLatestPanActionFrameDboDao.save(pukeGame.getId(), panActionFrame);
			// 记录一条Frame，回放的时候要做
			String gameId = pukeGame.getId();
			int panNo = panActionFrame.getPanAfterAction().getNo();
			int actionNo = panActionFrame.getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(panActionFrame);
			panActionFrameDboDao.save(panActionFrameDbo);
		}
	}

	public void action(PukeActionResult pukeActionResult) throws Throwable {

		PukeGameValueObject pukeGame = pukeActionResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

		String gameId = pukeGameDbo.getId();
		PanActionFrame panActionFrame = pukeActionResult.getPanActionFrame();
		gameLatestPanActionFrameDboDao.save(gameId, panActionFrame);
		// 记录一条Frame，回放的时候要做
		int panNo = panActionFrame.getPanAfterAction().getNo();
		int actionNo = panActionFrame.getNo();
		PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
		panActionFrameDbo.setPanActionFrame(panActionFrame);
		panActionFrameDboDao.save(panActionFrameDbo);
		// 盘出结果的话要记录结果
		WenzhouShuangkouPanResult wenzhouShuangkouPanResult = pukeActionResult.getPanResult();
		if (wenzhouShuangkouPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, wenzhouShuangkouPanResult);
			panResultDbo.setPanActionFrame(panActionFrame);
			panResultDboDao.save(panResultDbo);
			if (pukeActionResult.getJuResult() != null) {// 一切都结束了
				// 要记录局结果
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, pukeActionResult.getJuResult());
				juResultDboDao.save(juResultDbo);
			}
		}
	}

	public void chaodi(ChaodiResult chaodiResult) throws Throwable {

		PukeGameValueObject pukeGame = chaodiResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

		String gameId = pukeGame.getId();
		int panNo = pukeGame.getPanNo();
		pukeGamePlayerChaodiDboDao.updatePukeGamePlayerChaodiDbo(gameId, panNo, pukeGame.getPlayerChaodiStateMap());
		PanActionFrame panActionFrame = chaodiResult.getPanActionFrame();
		if (panActionFrame != null) {
			gameLatestPanActionFrameDboDao.save(gameId, panActionFrame);
			// 记录一条Frame，回放的时候要做
			int actionNo = panActionFrame.getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(panActionFrame);
			panActionFrameDboDao.save(panActionFrameDbo);
		}
		// 盘出结果的话要记录结果
		WenzhouShuangkouPanResult wenzhouShuangkouPanResult = chaodiResult.getPanResult();
		if (wenzhouShuangkouPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, wenzhouShuangkouPanResult);
			panResultDbo.setPanActionFrame(panActionFrame);
			panResultDboDao.save(panResultDbo);
			if (chaodiResult.getJuResult() != null) {// 一切都结束了
				// 要记录局结果
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, chaodiResult.getJuResult());
				juResultDboDao.save(juResultDbo);
			}
		}
	}

	public void readyToNextPan(ReadyToNextPanResult readyToNextPanResult) throws Throwable {

		PukeGameValueObject pukeGame = readyToNextPanResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);
		if (pukeGame.getState().name().equals(StartChaodi.name)) {
			PukeGamePlayerChaodiDbo dbo = new PukeGamePlayerChaodiDbo();
			dbo.setGameId(pukeGame.getId());
			dbo.setPanNo(pukeGame.getPanNo());
			dbo.setPlayerChaodiStateMap(pukeGame.getPlayerChaodiStateMap());
			pukeGamePlayerChaodiDboDao.addPukeGamePlayerChaodiDbo(dbo);
		}

		if (readyToNextPanResult.getFirstActionFrame() != null) {
			String gameId = pukeGameDbo.getId();
			PanActionFrame panActionFrame = readyToNextPanResult.getFirstActionFrame();
			gameLatestPanActionFrameDboDao.save(gameId, panActionFrame);
			// 记录一条Frame，回放的时候要做
			int panNo = readyToNextPanResult.getFirstActionFrame().getPanAfterAction().getNo();
			int actionNo = readyToNextPanResult.getFirstActionFrame().getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(readyToNextPanResult.getFirstActionFrame());
			panActionFrameDboDao.save(panActionFrameDbo);
		}

	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) {
		return panResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public JuResultDbo findJuResultDbo(String gameId) {
		return juResultDboDao.findByGameId(gameId);
	}

	public PukeGamePlayerChaodiDbo findLastPlayerChaodiDboByGameId(String gameId) {
		return pukeGamePlayerChaodiDboDao.findLastByGameId(gameId);
	}

	public PukeGamePlayerChaodiDbo findPlayerChaodiDboByGameIdAndPanNo(String gameId, int panNo) {
		return pukeGamePlayerChaodiDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public List<PanActionFrameDbo> findPanActionFrameDboForBackPlay(String gameId, int panNo) {
		return panActionFrameDboDao.findByGameIdAndPanNo(gameId, panNo);
	}
}
