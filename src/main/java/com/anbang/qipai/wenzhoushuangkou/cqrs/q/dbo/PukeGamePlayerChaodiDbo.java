package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import java.util.Map;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state.PukeGamePlayerChaodiState;

public class PukeGamePlayerChaodiDbo {

	private String id;
	private String gameId;
	private int panNO;
	private Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap;

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

	public int getPanNO() {
		return panNO;
	}

	public void setPanNO(int panNO) {
		this.panNO = panNO;
	}

	public Map<String, PukeGamePlayerChaodiState> getPlayerChaodiStateMap() {
		return playerChaodiStateMap;
	}

	public void setPlayerChaodiStateMap(Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap) {
		this.playerChaodiStateMap = playerChaodiStateMap;
	}

}
