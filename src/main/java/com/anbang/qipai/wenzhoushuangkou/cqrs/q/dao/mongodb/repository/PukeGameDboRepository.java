package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;

public interface PukeGameDboRepository extends MongoRepository<PukeGameDbo, String> {

}
