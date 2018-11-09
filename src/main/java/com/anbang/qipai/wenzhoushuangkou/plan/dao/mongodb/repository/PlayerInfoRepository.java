package com.anbang.qipai.wenzhoushuangkou.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.wenzhoushuangkou.plan.bean.PlayerInfo;

public interface PlayerInfoRepository extends MongoRepository<PlayerInfo, String> {

}
