package com.anbang.qipai.wenzhoushuangkou.cqrs.c.service;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.BianXingWanFa;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ChaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.FaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;

public interface GameCmdService {

	PukeGameValueObject newPukeGame(String gameId, String playerId, Integer panshu, Integer renshu, BianXingWanFa bx,
			Boolean chaodi, Boolean shuangming, Boolean fengding, ChaPai chapai, FaPai fapai);

	PukeGameValueObject joinGame(String playerId, String gameId) throws Exception;
}
