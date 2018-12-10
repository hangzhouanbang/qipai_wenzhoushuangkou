package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerAfterChaodi implements GamePlayerState {
	public static final String name = "PlayerAfterChaodi";

	@Override
	public String name() {
		return name;
	}

}
