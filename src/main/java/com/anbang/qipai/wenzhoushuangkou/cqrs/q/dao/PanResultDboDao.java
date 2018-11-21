package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanResultDbo;

public interface PanResultDboDao {

	void save(PanResultDbo panResultDbo);

	PanResultDbo findByGameIdAndPanNo(String gameId, int panNo);

}
