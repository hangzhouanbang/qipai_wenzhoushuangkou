package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboDao {

	void save(JuResultDbo juResultDbo);

	JuResultDbo findByGameId(String gameId);

}
