package com.anbang.qipai.wenzhoushuangkou.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.wenzhoushuangkou.msg.channel.WenzhouShuangkouResultSource;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.CommonMO;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.PukeHistoricalJuResult;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.PukeHistoricalPanResult;

@EnableBinding(WenzhouShuangkouResultSource.class)
public class WenzhouShuangkouResultMsgService {

	@Autowired
	private WenzhouShuangkouResultSource wenzhouShuangkouResultSource;

	public void recordJuResult(PukeHistoricalJuResult juResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("wenzhoushuangkou ju result");
		mo.setData(juResult);
		wenzhouShuangkouResultSource.wenzhouShuangkouResult().send(MessageBuilder.withPayload(mo).build());
	}

	public void recordPanResult(PukeHistoricalPanResult panResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("wenzhoushuangkou pan result");
		mo.setData(panResult);
		wenzhouShuangkouResultSource.wenzhouShuangkouResult().send(MessageBuilder.withPayload(mo).build());
	}
}
