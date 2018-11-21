package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.GameLatestPanActionFrameDboDao;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.mongodb.repository.GameLatestPanActionFrameDboRepository;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.shuangkou.pan.PanActionFrame;

@Component
public class MongodbGameLatestPanActionFrameDboDao implements GameLatestPanActionFrameDboDao {

	@Autowired
	private GameLatestPanActionFrameDboRepository repository;

	@Override
	public GameLatestPanActionFrameDbo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public void save(String id, PanActionFrame panActionFrame) {
		GameLatestPanActionFrameDbo dbo = new GameLatestPanActionFrameDbo();
		dbo.setId(id);
		dbo.setPanActionFrame(panActionFrame);
		repository.save(dbo);
	}

}
