package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service;

import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ChaodiResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.PukeActionResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.ReadyToNextPanResult;

public interface PukePlayCmdService {

	PukeActionResult da(String playerId, List<Integer> paiIds, String dianshuZuheIdx, Long actionTime) throws Exception;

	PukeActionResult guo(String playerId, Long actionTime) throws Exception;

	ReadyToNextPanResult readyToNextPan(String playerId) throws Exception;

	ChaodiResult chaodi(String playerId, Boolean chaodi, Long actionTime) throws Exception;

}
