package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.shuangkou.pan.PanActionFrame;
import com.dml.shuangkou.pan.PanValueObject;

public class PanActionFramePlayerViewFilter {
	public PanActionFrame filter(GameLatestPanActionFrameDbo frame, String playerId) {

		PanValueObject pan = frame.getPanActionFrame().getPanAfterAction();
		pan.getPaiListValueObject().setPaiList(null);
		pan.getShuangkouPlayerList().forEach((player) -> {
			if (player.getId().equals(playerId)) {// 是自己
				// 什么都不过滤，全要看
			} else {// 是其他玩家
				player.setAllShoupai(null);
				player.setShoupaiDianShuAmountArray(null);
				player.setShoupaiIdListForSortList(null);
				player.setYaPaiSolutionCandidates(null);
				player.setYaPaiSolutionsForTips(null);
			}
		});
		return frame.getPanActionFrame();
	}
}
