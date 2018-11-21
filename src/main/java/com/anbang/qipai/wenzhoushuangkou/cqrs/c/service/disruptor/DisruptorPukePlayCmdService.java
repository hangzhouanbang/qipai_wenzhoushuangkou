package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.disruptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeActionResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.PukePlayCmdService;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.service.impl.PukePlayCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "pukePlayCmdService")
public class DisruptorPukePlayCmdService extends DisruptorCmdServiceBase implements PukePlayCmdService {

	@Autowired
	private PukePlayCmdServiceImpl pukePlayCmdServiceImpl;

	@Override
	public PukeActionResult action(String playerId, List<Integer> paiIds, String dianshuZuheIdx, Long actionTime)
			throws Exception {
		CommonCommand cmd = new CommonCommand(PukePlayCmdServiceImpl.class.getName(), "action", playerId, paiIds,
				dianshuZuheIdx, actionTime);
		DeferredResult<PukeActionResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			PukeActionResult pukeActionResult = pukePlayCmdServiceImpl.action(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
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

}
