package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameLatestPukeGameInfoDbo;

public interface GameLatestPukeGameInfoDboRepository extends MongoRepository<GameLatestPukeGameInfoDbo, String> {

}
