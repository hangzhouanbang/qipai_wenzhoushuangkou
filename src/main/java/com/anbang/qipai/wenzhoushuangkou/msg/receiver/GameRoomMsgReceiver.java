package com.anbang.qipai.wenzhoushuangkou.msg.receiver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.GameCmdService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGamePlayerDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.service.PukeGameQueryService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.service.PukePlayQueryService;
import com.anbang.qipai.wenzhoushuangkou.msg.channel.GameRoomSink;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.CommonMO;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.PukeHistoricalJuResult;
import com.anbang.qipai.wenzhoushuangkou.msg.service.WenzhouShuangkouGameMsgService;
import com.anbang.qipai.wenzhoushuangkou.msg.service.WenzhouShuangkouResultMsgService;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.google.gson.Gson;

@EnableBinding(GameRoomSink.class)
public class GameRoomMsgReceiver {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private PukeGameQueryService pukeGameQueryService;

	@Autowired
	private PukePlayQueryService pukePlayQueryService;

	@Autowired
	private WenzhouShuangkouResultMsgService wenzhouShuangkouResultMsgService;

	@Autowired
	private WenzhouShuangkouGameMsgService wenzhouShuangkouGameMsgService;

	private Gson gson = new Gson();

	@StreamListener(GameRoomSink.WENZHOUSHUANGKOUGAMEROOM)
	public void removeGameRoom(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("gameIds".equals(msg)) {
			List<String> gameIds = gson.fromJson(json, ArrayList.class);
			for (String gameId : gameIds) {
				try {
					PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
					boolean playerOnline = false;
					for (PukeGamePlayerDbo player : pukeGameDbo.getPlayers()) {
						if (GamePlayerOnlineState.online.equals(player.getOnlineState())) {
							playerOnline = true;
						}
					}
					if (playerOnline) {
						wenzhouShuangkouGameMsgService.delay(gameId);
					} else {
						PukeGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
						pukeGameQueryService.finishGameImmediately(gameValueObject);
						JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
						PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
						wenzhouShuangkouResultMsgService.recordJuResult(juResult);
						wenzhouShuangkouGameMsgService.gameFinished(gameId);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

}
