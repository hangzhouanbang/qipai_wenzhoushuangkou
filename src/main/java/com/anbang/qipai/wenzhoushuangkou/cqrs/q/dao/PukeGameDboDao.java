package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

public interface PukeGameDboDao {

	PukeGameDbo findById(String id);

	void save(PukeGameDbo pukeGameDbo);

	void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState);

}
