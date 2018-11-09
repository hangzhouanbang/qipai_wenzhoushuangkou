package com.anbang.qipai.wenzhoushuangkou.plan.dao;

import com.anbang.qipai.wenzhoushuangkou.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceDao {

	void save(MemberGoldBalance memberGoldBalance);

	MemberGoldBalance findByMemberId(String memberId);
}
