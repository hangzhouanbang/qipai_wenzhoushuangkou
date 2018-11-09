package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.BianXingWanFa;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ChaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.FaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.GameCmdService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.impl.GameCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "gameCmdService")
public class DisruptorGameCmdService extends DisruptorCmdServiceBase implements GameCmdService {

	@Autowired
	private GameCmdServiceImpl gameCmdServiceImpl;

	@Override
	public PukeGameValueObject newPukeGame(String gameId, String playerId, Integer panshu, Integer renshu,
			BianXingWanFa bx, Boolean chaodi, Boolean shuangming, Boolean fengding, ChaPai chapai, FaPai fapai) {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newPukeGame", gameId, playerId,
				panshu, renshu, bx, chaodi, shuangming, fengding, chapai, fapai);
		DeferredResult<PukeGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			PukeGameValueObject pukeGameValueObject = gameCmdServiceImpl.newPukeGame(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return pukeGameValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public PukeGameValueObject joinGame(String playerId, String gameId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "joinGame", playerId, gameId);
		DeferredResult<PukeGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			PukeGameValueObject pukeGameValueObject = gameCmdServiceImpl.joinGame(cmd.getParameter(),
					cmd.getParameter());
			return pukeGameValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
