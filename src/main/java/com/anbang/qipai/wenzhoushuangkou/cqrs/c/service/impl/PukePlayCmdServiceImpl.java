package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeActionResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGame;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.PukePlayCmdService;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.player.PlayerNotInGameException;
import com.dml.mpgame.server.GameServer;
import com.dml.shuangkou.pan.PanActionFrame;

@Component
public class PukePlayCmdServiceImpl extends CmdServiceBase implements PukePlayCmdService {

	@Override
	public PukeActionResult action(String playerId, List<Integer> paiIds, String dianshuZuheIdx, Long actionTime)
			throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		String gameId = gameServer.findBindGameId(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}

		PukeGame pukeGame = (PukeGame) gameServer.findGame(gameId);
		PukeActionResult pukeActionResult = pukeGame.action(playerId, paiIds, dianshuZuheIdx, actionTime);

		if (pukeActionResult.getJuResult() != null) {// 全部结束
			gameServer.finishGame(gameId);
		}

		return pukeActionResult;
	}

	@Override
	public ReadyToNextPanResult readyToNextPan(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		String gameId = gameServer.findBindGameId(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		PukeGame pukeGame = (PukeGame) gameServer.findGame(gameId);

		ReadyToNextPanResult readyToNextPanResult = new ReadyToNextPanResult();
		pukeGame.readyToNextPan(playerId);
		if (pukeGame.getState().name().equals(Playing.name)) {// 开始下一盘了
			PanActionFrame firstActionFrame = pukeGame.getJu().getCurrentPan().findLatestActionFrame();
			readyToNextPanResult.setFirstActionFrame(firstActionFrame);
		}
		readyToNextPanResult.setPukeGame(new PukeGameValueObject(pukeGame));
		return readyToNextPanResult;
	}

}
