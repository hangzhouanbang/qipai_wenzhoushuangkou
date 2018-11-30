package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.puke.pai.PukePai;
import com.dml.puke.pai.QiShouLiangPaiMark;
import com.dml.puke.pai.ZuDuiLiangPaiMark;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.puke.wanfa.position.Position;
import com.dml.puke.wanfa.position.PositionUtil;
import com.dml.shuangkou.pan.PanActionFrame;
import com.dml.shuangkou.pan.PanValueObject;
import com.dml.shuangkou.player.ShuangkouPlayerValueObject;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;

public class PanActionFramePlayerViewFilter {
	public PanActionFrame filter(GameLatestPanActionFrameDbo frame, String playerId, boolean shuangming) {

		PanValueObject pan = frame.getPanActionFrame().getPanAfterAction();
		pan.getPaiListValueObject().setPaiList(null);
		Position duijiaPosition = null;
		for (ShuangkouPlayerValueObject player : pan.getShuangkouPlayerList()) {
			if (player.getId().equals(playerId)) {// 是自己
				duijiaPosition = PositionUtil.nextPositionClockwise(player.getPosition());
				duijiaPosition = PositionUtil.nextPositionClockwise(duijiaPosition);
			}
		}
		if (pan.getShuangkouPlayerList().size() > 2) {
			for (ShuangkouPlayerValueObject player : pan.getShuangkouPlayerList()) {
				if (player.getId().equals(playerId) || (player.getPosition().equals(duijiaPosition) && shuangming)) {// 是自己或者对家
					if (player.getId().equals(playerId)) {// 是否可以抄底
						if (player.getAllShoupai().size() == 27) {// 未打牌
							boolean couldChaodi = true;
							for (DaPaiDianShuSolution solution : player.getYaPaiSolutionCandidates()) {
								DianShuZu dianShuZu = solution.getDianShuZu();
								if (dianShuZu instanceof ZhadanDianShuZu) {
									couldChaodi = false;
									break;
								}
							}
							player.setCouldChaodi(couldChaodi);
						}
					}
					// 什么都不过滤，全要看
				} else {// 是其他玩家
					List<PukePai> allShoupaiVO = new ArrayList<>();
					List<PukePai> allShoupai = player.getAllShoupai();
					for (PukePai pukePai : allShoupai) {
						if (pukePai.getMark() != null && pukePai.getMark() instanceof ZuDuiLiangPaiMark
								|| pukePai.getMark() instanceof QiShouLiangPaiMark) {
							allShoupaiVO.add(pukePai);
						}
					}
					player.setAllShoupai(allShoupaiVO);
					player.setShoupaiDianShuAmountArray(null);
					player.setShoupaiIdListForSortList(null);
					player.setYaPaiSolutionCandidates(null);
					player.setYaPaiSolutionsForTips(null);
				}
			}
		} else {
			for (ShuangkouPlayerValueObject player : pan.getShuangkouPlayerList()) {
				if (player.getId().equals(playerId)) {// 是自己
					if (player.getId().equals(playerId)) {// 是否可以抄底
						if (player.getAllShoupai().size() == 27) {// 未打牌
							boolean couldChaodi = true;
							for (DaPaiDianShuSolution solution : player.getYaPaiSolutionCandidates()) {
								DianShuZu dianShuZu = solution.getDianShuZu();
								if (dianShuZu instanceof ZhadanDianShuZu) {
									couldChaodi = false;
									break;
								}
							}
							player.setCouldChaodi(couldChaodi);
						}
					}
					// 什么都不过滤，全要看
				} else {// 是其他玩家
					List<PukePai> allShoupaiVO = new ArrayList<>();
					List<PukePai> allShoupai = player.getAllShoupai();
					for (PukePai pukePai : allShoupai) {
						if (pukePai.getMark() != null && pukePai.getMark() instanceof ZuDuiLiangPaiMark
								|| pukePai.getMark() instanceof QiShouLiangPaiMark) {
							allShoupaiVO.add(pukePai);
						}
					}
					player.setAllShoupai(allShoupaiVO);
					player.setShoupaiDianShuAmountArray(null);
					player.setShoupaiIdListForSortList(null);
					player.setYaPaiSolutionCandidates(null);
					player.setYaPaiSolutionsForTips(null);
				}
			}
		}
		return frame.getPanActionFrame();
	}
}
