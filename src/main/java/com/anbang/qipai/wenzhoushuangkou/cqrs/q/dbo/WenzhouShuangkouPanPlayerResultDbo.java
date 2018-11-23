package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouPanPlayerResult;
import com.dml.shuangkou.player.ShuangkouPlayerValueObject;

public class WenzhouShuangkouPanPlayerResultDbo {

	private String playerId;
	private WenzhouShuangkouPanPlayerResult playerResult;
	private ShuangkouPlayerValueObject player;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public WenzhouShuangkouPanPlayerResult getPlayerResult() {
		return playerResult;
	}

	public void setPlayerResult(WenzhouShuangkouPanPlayerResult playerResult) {
		this.playerResult = playerResult;
	}

	public ShuangkouPlayerValueObject getPlayer() {
		return player;
	}

	public void setPlayer(ShuangkouPlayerValueObject player) {
		this.player = player;
	}

}
