package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerVotedWhenAfterChaodi implements GamePlayerState {

	public static final String name = "PlayerVotedWhenAfterChaodi";

	@Override
	public String name() {
		return name;
	}

}
