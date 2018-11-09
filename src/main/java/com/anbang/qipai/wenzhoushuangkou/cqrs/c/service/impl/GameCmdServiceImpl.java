package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.BianXingWanFa;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ChaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.FaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGame;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.GameCmdService;
import com.dml.mpgame.game.extend.fpmpv.back.FpmpvBackStrategy;
import com.dml.mpgame.game.extend.vote.OnlineVotePlayersFilter;
import com.dml.mpgame.game.extend.vote.leave.VoteWaiverLeaveStrategy;
import com.dml.mpgame.game.join.FixedNumberOfPlayersGameJoinStrategy;
import com.dml.mpgame.game.leave.HostGameLeaveStrategy;
import com.dml.mpgame.game.leave.OfflineGameLeaveStrategy;
import com.dml.mpgame.game.ready.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.server.GameServer;

@Component
public class GameCmdServiceImpl extends CmdServiceBase implements GameCmdService {

	@Override
	public PukeGameValueObject newPukeGame(String gameId, String playerId, Integer panshu, Integer renshu,
			BianXingWanFa bx, Boolean chaodi, Boolean shuangming, Boolean fengding, ChaPai chapai, FaPai fapai) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);

		PukeGame newGame = new PukeGame();

		newGame.setPanshu(panshu);
		newGame.setRenshu(renshu);
		newGame.setFixedPlayerCount(renshu);
		newGame.setBx(bx);
		newGame.setChaodi(chaodi);
		newGame.setShuangming(shuangming);
		newGame.setFengding(fengding);
		newGame.setChapai(chapai);
		newGame.setFapai(fapai);

		newGame.setVotePlayersFilter(new OnlineVotePlayersFilter());

		newGame.setJoinStrategy(new FixedNumberOfPlayersGameJoinStrategy(renshu));
		newGame.setReadyStrategy(new FixedNumberOfPlayersGameReadyStrategy(renshu));

		newGame.setLeaveByOfflineStrategyAfterStart(new VoteWaiverLeaveStrategy());
		newGame.setLeaveByOfflineStrategyBeforeStart(new OfflineGameLeaveStrategy());

		newGame.setLeaveByHangupStrategyAfterStart(new VoteWaiverLeaveStrategy());
		newGame.setLeaveByHangupStrategyBeforeStart(new OfflineGameLeaveStrategy());

		newGame.setLeaveByPlayerStrategyAfterStart(new VoteWaiverLeaveStrategy());
		newGame.setLeaveByPlayerStrategyBeforeStart(new HostGameLeaveStrategy(playerId));

		newGame.setBackStrategy(new FpmpvBackStrategy());
		newGame.create(gameId, playerId);
		gameServer.playerCreateGame(newGame, playerId);

		return new PukeGameValueObject(newGame);
	}

	@Override
	public PukeGameValueObject joinGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.join(playerId, gameId);
	}

}
