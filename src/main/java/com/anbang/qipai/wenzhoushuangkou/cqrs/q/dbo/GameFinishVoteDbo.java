package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

public class GameFinishVoteDbo {

	private String id;
	private String gameId;
	private GameFinishVoteValueObject vote;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public GameFinishVoteValueObject getVote() {
		return vote;
	}

	public void setVote(GameFinishVoteValueObject vote) {
		this.vote = vote;
	}

}
