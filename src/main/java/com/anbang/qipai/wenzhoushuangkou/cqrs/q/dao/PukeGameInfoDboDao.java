package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameInfoDbo;

public interface PukeGameInfoDboDao {
	void save(PukeGameInfoDbo dbo);

	List<PukeGameInfoDbo> findByGameIdAndPanNo(String gameId, int panNo);

}
