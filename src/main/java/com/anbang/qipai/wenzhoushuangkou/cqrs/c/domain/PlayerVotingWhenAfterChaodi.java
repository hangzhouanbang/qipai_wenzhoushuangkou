package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerVotingWhenAfterChaodi implements GamePlayerState {

	public static final String name = "PlayerVotingWhenAfterChaodi";

	@Override
	public String name() {
		return name;
	}

}
