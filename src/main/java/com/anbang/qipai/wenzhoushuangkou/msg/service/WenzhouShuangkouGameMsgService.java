package com.anbang.qipai.wenzhoushuangkou.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.msg.channel.WenzhouShuangkouGameSource;
import com.anbang.qipai.wenzhoushuangkou.msg.msjobj.CommonMO;

@EnableBinding(WenzhouShuangkouGameSource.class)
public class WenzhouShuangkouGameMsgService {

	@Autowired
	private WenzhouShuangkouGameSource wenzhouShuangkouGameSource;

	public void gamePlayerLeave(PukeGameValueObject pukeGameValueObject, String playerId) {
		boolean playerIsQuit = true;
		for (String pid : pukeGameValueObject.allPlayerIds()) {
			if (pid.equals(playerId)) {
				playerIsQuit = false;
				break;
			}
		}
		if (playerIsQuit) {
			CommonMO mo = new CommonMO();
			mo.setMsg("playerQuit");
			Map data = new HashMap();
			data.put("gameId", pukeGameValueObject.getId());
			data.put("playerId", playerId);
			mo.setData(data);
			wenzhouShuangkouGameSource.wenzhouShuangkouGame().send(MessageBuilder.withPayload(mo).build());
		}
	}
}
