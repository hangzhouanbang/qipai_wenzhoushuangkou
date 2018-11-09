package com.anbang.qipai.wenzhoushuangkou.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.wenzhoushuangkou.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceRepository extends MongoRepository<MemberGoldBalance, String> {

	MemberGoldBalance findOneByMemberId(String memberId);
}
