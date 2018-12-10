package com.anbang.qipai.wenzhoushuangkou.cqrs.q.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.WenzhouShuangkouJuResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.PukeGameDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.wenzhoushuangkou.plan.bean.PlayerInfo;
import com.anbang.qipai.wenzhoushuangkou.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

@Service
public class PukeGameQueryService {

	@Autowired
	private PukeGameDboDao pukeGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GameFinishVoteDboDao gameFinishVoteDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	public PukeGameDbo findPukeGameDboById(String gameId) {
		return pukeGameDboDao.findById(gameId);
	}

	public void newPukeGame(PukeGameValueObject pukeGame) {

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

	}

	public void joinGame(PukeGameValueObject pukeGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);
	}

	public void leaveGame(PukeGameValueObject pukeGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

		gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(pukeGame.getId());
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGame.getVote();
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
		gameFinishVoteDbo.setGameId(pukeGame.getId());
		gameFinishVoteDboDao.save(gameFinishVoteDbo);

		WenzhouShuangkouJuResult wenzhouShuangkouJuResult = (WenzhouShuangkouJuResult) pukeGame.getJuResult();
		if (wenzhouShuangkouJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(pukeGame.getId(), null, wenzhouShuangkouJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public void backToGame(String playerId, PukeGameValueObject pukeGameValueObject) {
		pukeGameDboDao.updatePlayerOnlineState(pukeGameValueObject.getId(), playerId,
				pukeGameValueObject.findPlayerOnlineState(playerId));
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGameValueObject.getVote();
		gameFinishVoteDboDao.update(pukeGameValueObject.getId(), gameFinishVoteValueObject);
	}

	public void finish(PukeGameValueObject pukeGameValueObject) {
		gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(pukeGameValueObject.getId());
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGameValueObject.getVote();
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
		gameFinishVoteDbo.setGameId(pukeGameValueObject.getId());
		gameFinishVoteDboDao.save(gameFinishVoteDbo);

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGameValueObject, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

		WenzhouShuangkouJuResult wenzhouShuangkouJuResult = (WenzhouShuangkouJuResult) pukeGameValueObject
				.getJuResult();
		if (wenzhouShuangkouJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(pukeGameValueObject.getId(), null, wenzhouShuangkouJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public void voteToFinish(PukeGameValueObject pukeGameValueObject) {
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGameValueObject.getVote();
		gameFinishVoteDboDao.update(pukeGameValueObject.getId(), gameFinishVoteValueObject);

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGameValueObject, playerInfoMap);
		pukeGameDboDao.save(pukeGameDbo);

		WenzhouShuangkouJuResult wenzhouShuangkouJuResult = (WenzhouShuangkouJuResult) pukeGameValueObject
				.getJuResult();
		if (wenzhouShuangkouJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(pukeGameValueObject.getId(), null, wenzhouShuangkouJuResult);
			juResultDboDao.save(juResultDbo);
		}
	}

	public GameFinishVoteDbo findGameFinishVoteDbo(String gameId) {
		return gameFinishVoteDboDao.findByGameId(gameId);
	}
}
