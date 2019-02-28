package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.disruptor;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ChaodiResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.PukeActionResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ReadyToNextPanResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.PukePlayCmdService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.impl.PukePlayCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "pukePlayCmdService")
public class DisruptorPukePlayCmdService extends DisruptorCmdServiceBase implements PukePlayCmdService {

	@Autowired
	private PukePlayCmdServiceImpl pukePlayCmdServiceImpl;

	@Override
	public PukeActionResult da(String playerId, ArrayList<Integer> paiIds, String dianshuZuheIdx, Integer actionNo,
			Long actionTime) throws Exception {
		CommonCommand cmd = new CommonCommand(PukePlayCmdServiceImpl.class.getName(), "da", playerId, paiIds,
				dianshuZuheIdx, actionNo, actionTime);
		DeferredResult<PukeActionResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			PukeActionResult pukeActionResult = pukePlayCmdServiceImpl.da(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return pukeActionResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ReadyToNextPanResult readyToNextPan(String playerId) throws Exception {
		CommonCommand cmd = new CommonCommand(PukePlayCmdServiceImpl.class.getName(), "readyToNextPan", playerId);
		DeferredResult<ReadyToNextPanResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			ReadyToNextPanResult readyToNextPanResult = pukePlayCmdServiceImpl.readyToNextPan(cmd.getParameter());
			return readyToNextPanResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ChaodiResult chaodi(String playerId, Boolean chaodi, Long actionTime) throws Exception {
		CommonCommand cmd = new CommonCommand(PukePlayCmdServiceImpl.class.getName(), "chaodi", playerId, chaodi,
				actionTime);
		DeferredResult<ChaodiResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			ChaodiResult chaodiResult = pukePlayCmdServiceImpl.chaodi(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter());
			return chaodiResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public PukeActionResult guo(String playerId, Integer actionNo, Long actionTime) throws Exception {
		CommonCommand cmd = new CommonCommand(PukePlayCmdServiceImpl.class.getName(), "guo", playerId, actionNo,
				actionTime);
		DeferredResult<PukeActionResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			PukeActionResult pukeActionResult = pukePlayCmdServiceImpl.guo(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter());
			return pukeActionResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
