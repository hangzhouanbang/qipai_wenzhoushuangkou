package com.anbang.qipai.wenzhoushuangkou.plan.dao;

import com.anbang.qipai.wenzhoushuangkou.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

	void save(PlayerInfo playerInfo);
}
