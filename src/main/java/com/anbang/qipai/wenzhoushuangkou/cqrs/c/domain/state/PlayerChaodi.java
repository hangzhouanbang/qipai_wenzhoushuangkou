package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerChaodi implements GamePlayerState {
	public static final String name = "PlayerChaodi";

	@Override
	public String name() {
		return name;
	}

}
