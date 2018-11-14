package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ChaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.FaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ReadyForGameResult;
import com.dml.mpgame.game.GameValueObject;
import com.dml.shuangkou.preparedapai.luanpai.BianXingWanFa;

public interface GameCmdService {

	PukeGameValueObject newPukeGame(String gameId, String playerId, Integer panshu, Integer renshu, BianXingWanFa bx,
			Boolean chaodi, Boolean shuangming, Boolean fengding, ChaPai chapai, FaPai fapai);

	PukeGameValueObject joinGame(String playerId, String gameId) throws Exception;

	PukeGameValueObject leaveGame(String playerId) throws Exception;

	PukeGameValueObject leaveGameByHangup(String playerId) throws Exception;

	PukeGameValueObject leaveGameByOffline(String playerId) throws Exception;

	PukeGameValueObject backToGame(String playerId, String gameId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	PukeGameValueObject finish(String playerId) throws Exception;

	PukeGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception;

	GameValueObject finishGameImmediately(String gameId) throws Exception;

	void bindPlayer(String playerId, String gameId);
}
