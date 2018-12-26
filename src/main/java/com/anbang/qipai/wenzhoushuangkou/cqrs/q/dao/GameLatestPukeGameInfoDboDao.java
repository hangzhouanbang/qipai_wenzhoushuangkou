package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameLatestPukeGameInfoDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameInfoDbo;

public interface GameLatestPukeGameInfoDboDao {
	GameLatestPukeGameInfoDbo findById(String id);

	void save(String id, PukeGameInfoDbo gameInfoDbo);
}
