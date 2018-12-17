package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.listener.XianshuCountDaActionStatisticsListener;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.WenzhouShuangkouPanPlayerResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.WenzhouShuangkouPanResult;
import com.dml.puke.pai.DianShu;
import com.dml.puke.pai.PukePai;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pai.dianshuzu.DianShuZuCalculator;
import com.dml.shuangkou.pai.dianshuzu.LianXuZhadanDianShuZu;
import com.dml.shuangkou.pai.dianshuzu.PaiXing;
import com.dml.shuangkou.pai.dianshuzu.WangZhadanDianShuZu;
import com.dml.shuangkou.pai.jiesuanpai.DawangDangPai;
import com.dml.shuangkou.pai.jiesuanpai.ShoupaiJiesuanPai;
import com.dml.shuangkou.pai.jiesuanpai.XiaowangDangPai;
import com.dml.shuangkou.pan.CurrentPanResultBuilder;
import com.dml.shuangkou.pan.Pan;
import com.dml.shuangkou.pan.PanResult;
import com.dml.shuangkou.pan.PanValueObject;
import com.dml.shuangkou.player.ShuangkouPlayer;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class WenzhouShuangkouCurrentPanResultBuilder implements CurrentPanResultBuilder {
	private int renshu;
	private BianXingWanFa bx;
	private Map<String, Integer> playerTotalGongxianfenMap = new HashMap<>();
	private Map<String, Integer> playerMaxXianshuMap = new HashMap<>();
	private Map<String, Integer> playerOtherMaxXianshuMap = new HashMap<>();
	private List<String> chaodiPlayerIdList = new ArrayList<>();

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		WenzhouShuangkouPanResult latestFinishedPanResult = (WenzhouShuangkouPanResult) ju
				.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (WenzhouShuangkouPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}
		List<String> playerIds = currentPan.getAllPlayerId();
		XianshuCountDaActionStatisticsListener wenzhouShuangkouListener = ju.getActionStatisticsListenerManager()
				.findDaListener(XianshuCountDaActionStatisticsListener.class);
		Map<String, int[]> playerXianshuMap = wenzhouShuangkouListener.getPlayerXianshuMap();
		Map<String, WenzhouShuangkouXianshuBeishu> maxXianshuMap = new HashMap<>();
		for (String pid : playerIds) {
			int[] xianshuCount = playerXianshuMap.get(pid);
			WenzhouShuangkouXianshuBeishu xianshubeishu = new WenzhouShuangkouXianshuBeishu(xianshuCount);
			xianshubeishu.calculate();
			maxXianshuMap.put(pid, xianshubeishu);
		}
		// 计算胜负分
		playerIds.forEach((pid) -> {
			if (renshu > 2) {
				ShuangkouPlayer duijiaPlayer = currentPan.findDuijiaPlayer(pid);
				String duijiaPlayerId = duijiaPlayer.getId();
				int maxXianshu = 1;
				int otherMaxXianshu = 1;
				for (String id : playerIds) {
					if (id.equals(pid) || id.equals(duijiaPlayerId)) {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						if (xianshubeishu.getValue() > maxXianshu) {
							maxXianshu = xianshubeishu.getValue();
						}
					} else {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						if (xianshubeishu.getValue() > otherMaxXianshu) {
							otherMaxXianshu = xianshubeishu.getValue();
						}
					}
				}
				playerMaxXianshuMap.put(pid, maxXianshu);
				playerOtherMaxXianshuMap.put(pid, otherMaxXianshu);
			} else {
				for (String id : playerIds) {
					if (id.equals(pid)) {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						playerMaxXianshuMap.put(pid, xianshubeishu.getValue());
					} else {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						playerOtherMaxXianshuMap.put(pid, xianshubeishu.getValue());
					}
				}
			}
		});

		List<String> noPaiPlayerIdList = currentPan.getNoPaiPlayerIdList();
		if (renshu > 2) {// 4人游戏
			List<WenzhouShuangkouPanPlayerResult> panPlayerResultList = new ArrayList<>();
			String yingPlayerId = noPaiPlayerIdList.get(0);
			ShuangkouPlayer duijiaPlayer = currentPan.findDuijiaPlayer(yingPlayerId);
			String playerId1 = noPaiPlayerIdList.get(1);
			String playerId2 = "";
			if (noPaiPlayerIdList.size() > 2) {
				playerId2 = noPaiPlayerIdList.get(2);
			} else {
				for (String pid : playerIds) {
					if (!pid.equals(yingPlayerId) && !pid.equals(playerId1)) {
						playerId2 = pid;
					}
				}
			}
			String playerId3 = "";
			for (String pid : playerIds) {
				if (!pid.equals(yingPlayerId) && !pid.equals(playerId1) && !pid.equals(playerId2)) {
					playerId3 = pid;
				}
			}
			// 赢家
			WenzhouShuangkouPanPlayerResult yingPlayerResult = new WenzhouShuangkouPanPlayerResult();
			yingPlayerResult.setPlayerId(yingPlayerId);
			WenzhouShuangkouMingcifen mingcifen = new WenzhouShuangkouMingcifen();
			yingPlayerResult.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			mingcifen.setYing(true);
			mingcifen.setMingci(1);
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen.setDankou(true);
			} else {
				mingcifen.setPingkou(true);
			}
			mingcifen.calculate();
			yingPlayerResult.setMingcifen(mingcifen);
			int[] xianshuCount = playerXianshuMap.get(yingPlayerId);
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
			gongxianfen.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(yingPlayerId, ju));
			gongxianfen.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen);
			WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
			bufen.setTotalScore(playerTotalGongxianfenMap.get(yingPlayerId));
			bufen.setScore(gongxianfen.getValue());
			bufen.calculate();
			yingPlayerResult.setBufen(bufen);
			panPlayerResultList.add(yingPlayerResult);

			// 玩家1
			WenzhouShuangkouPanPlayerResult playerResult1 = new WenzhouShuangkouPanPlayerResult();
			playerResult1.setPlayerId(playerId1);
			WenzhouShuangkouMingcifen mingcifen1 = new WenzhouShuangkouMingcifen();
			mingcifen1.setMingci(2);
			playerResult1.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen1.setYing(true);
				mingcifen1.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen1.setYing(false);
				mingcifen1.setDankou(true);
			} else {
				mingcifen1.setYing(false);
				mingcifen1.setPingkou(true);
			}
			mingcifen1.calculate();
			playerResult1.setMingcifen(mingcifen1);
			int[] xianshuCount1 = playerXianshuMap.get(playerId1);
			WenzhouShuangkouGongxianFen gongxianfen1 = new WenzhouShuangkouGongxianFen(xianshuCount1);
			gongxianfen1.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(playerId1, ju));
			gongxianfen1.calculate(renshu);
			playerResult1.setGongxianfen(gongxianfen1);
			WenzhouShuangkouChaixianbufen bufen1 = new WenzhouShuangkouChaixianbufen();
			bufen1.setTotalScore(playerTotalGongxianfenMap.get(playerId1));
			bufen1.setScore(gongxianfen1.getValue());
			bufen1.calculate();
			playerResult1.setBufen(bufen1);
			panPlayerResultList.add(playerResult1);

			// 玩家2
			WenzhouShuangkouPanPlayerResult playerResult2 = new WenzhouShuangkouPanPlayerResult();
			playerResult2.setPlayerId(playerId2);
			WenzhouShuangkouMingcifen mingcifen2 = new WenzhouShuangkouMingcifen();
			mingcifen2.setMingci(3);
			playerResult2.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen2.setYing(false);
				mingcifen2.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen2.setYing(true);
				mingcifen2.setDankou(true);
			} else {
				mingcifen2.setYing(false);
				playerResult2.setXianshubeishu(playerOtherMaxXianshuMap.get(playerId2));
				mingcifen2.setPingkou(true);
			}
			mingcifen2.calculate();
			playerResult2.setMingcifen(mingcifen2);
			int[] xianshuCount2 = playerXianshuMap.get(playerId2);
			WenzhouShuangkouGongxianFen gongxianfen2 = new WenzhouShuangkouGongxianFen(xianshuCount2);
			gongxianfen2.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(playerId2, ju));
			gongxianfen2.calculate(renshu);
			playerResult2.setGongxianfen(gongxianfen2);
			WenzhouShuangkouChaixianbufen bufen2 = new WenzhouShuangkouChaixianbufen();
			bufen2.setTotalScore(playerTotalGongxianfenMap.get(playerId2));
			bufen2.setScore(gongxianfen2.getValue());
			bufen2.calculate();
			playerResult2.setBufen(bufen2);
			panPlayerResultList.add(playerResult2);

			// 玩家3
			WenzhouShuangkouPanPlayerResult playerResult3 = new WenzhouShuangkouPanPlayerResult();
			playerResult3.setPlayerId(playerId3);
			WenzhouShuangkouMingcifen mingcifen3 = new WenzhouShuangkouMingcifen();
			mingcifen3.setMingci(4);
			playerResult3.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen3.setYing(false);
				mingcifen3.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen3.setYing(false);
				mingcifen3.setDankou(true);
			} else {
				mingcifen3.setYing(true);
				playerResult3.setXianshubeishu(playerMaxXianshuMap.get(playerId3));
				mingcifen3.setPingkou(true);
			}
			mingcifen3.calculate();
			playerResult3.setMingcifen(mingcifen3);
			int[] xianshuCount3 = playerXianshuMap.get(playerId3);
			WenzhouShuangkouGongxianFen gongxianfen3 = new WenzhouShuangkouGongxianFen(xianshuCount3);
			gongxianfen3.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(playerId3, ju));
			gongxianfen3.calculate(renshu);
			playerResult3.setGongxianfen(gongxianfen3);
			WenzhouShuangkouChaixianbufen bufen3 = new WenzhouShuangkouChaixianbufen();
			bufen3.setTotalScore(playerTotalGongxianfenMap.get(playerId3));
			bufen3.setScore(gongxianfen3.getValue());
			bufen3.calculate();
			playerResult3.setBufen(bufen3);
			panPlayerResultList.add(playerResult3);

			// 两两结算贡献分
			for (int i = 0; i < panPlayerResultList.size(); i++) {
				WenzhouShuangkouPanPlayerResult playerResulti = panPlayerResultList.get(i);
				WenzhouShuangkouGongxianFen gongxiani = playerResulti.getGongxianfen();
				for (int j = (i + 1); j < panPlayerResultList.size(); j++) {
					WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
					WenzhouShuangkouGongxianFen gongxianj = playerResultj.getGongxianfen();
					// 结算贡献分
					int feni = gongxiani.getValue();
					int fenj = gongxianj.getValue();
					gongxiani.jiesuan(-fenj);
					gongxianj.jiesuan(-feni);
				}
			}

			// 计算补分
			Set<String> yingjiaPlayerId = new HashSet<>();
			for (int i = 0; i < panPlayerResultList.size(); i++) {
				WenzhouShuangkouPanPlayerResult playerResulti = panPlayerResultList.get(i);
				if (yingPlayerId.equals(playerResulti.getPlayerId())) {
					WenzhouShuangkouChaixianbufen chaixianBufen1 = playerResulti.getBufen();
					int bufeni = chaixianBufen1.getValue();
					for (int j = 0; j < panPlayerResultList.size(); j++) {
						WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
						if (duijiaPlayer.getId().equals(playerResultj.getPlayerId())) {
							WenzhouShuangkouChaixianbufen chaixianBufen2 = playerResultj.getBufen();
							int bufenj = chaixianBufen2.getValue();
							// 结算补分
							chaixianBufen1.jiesuan(-bufenj);
							chaixianBufen2.jiesuan(-bufeni);
							yingjiaPlayerId.add(yingPlayerId);
							yingjiaPlayerId.add(duijiaPlayer.getId());
							break;
						}
					}
				}
			}
			for (int i = 0; i < panPlayerResultList.size(); i++) {
				WenzhouShuangkouPanPlayerResult playerResulti = panPlayerResultList.get(i);
				if (!yingjiaPlayerId.contains(playerResulti.getPlayerId())) {
					WenzhouShuangkouChaixianbufen chaixianBufen1 = playerResulti.getBufen();
					int bufeni = chaixianBufen1.getValue();
					ShuangkouPlayer duijiaPlayer1 = currentPan.findDuijiaPlayer(playerResulti.getPlayerId());
					for (int j = 0; j < panPlayerResultList.size(); j++) {
						WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
						if (duijiaPlayer1.getId().equals(playerResultj.getPlayerId())) {
							WenzhouShuangkouChaixianbufen chaixianBufen2 = playerResultj.getBufen();
							int bufenj = chaixianBufen2.getValue();
							// 结算补分
							chaixianBufen1.jiesuan(-bufenj);
							chaixianBufen2.jiesuan(-bufeni);
							break;
						}
					}
					break;
				}
			}
			panPlayerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				playerResult.setScore(playerResult.getGongxianfen().getTotalscore() + playerResult.getBufen().getValue()
						+ playerResult.getMingcifen().getValue() * playerResult.getXianshubeishu());
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(
							playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore());
				} else {
					playerResult.setTotalScore(playerResult.getScore());
				}
			});
			WenzhouShuangkouPanResult wenzhouShuangkouPanResult = new WenzhouShuangkouPanResult();
			wenzhouShuangkouPanResult.setPan(new PanValueObject(currentPan));
			wenzhouShuangkouPanResult.setPanFinishTime(panFinishTime);
			wenzhouShuangkouPanResult.setPanPlayerResultList(panPlayerResultList);
			return wenzhouShuangkouPanResult;
		} else {
			List<WenzhouShuangkouPanPlayerResult> panPlayerResultList = new ArrayList<>();
			String yingPlayerId = noPaiPlayerIdList.get(0);
			ShuangkouPlayer shuPlayer = currentPan.findDuijiaPlayer(yingPlayerId);
			String shuPlayerId = shuPlayer.getId();
			// 赢家
			WenzhouShuangkouPanPlayerResult yingPlayerResult = new WenzhouShuangkouPanPlayerResult();
			yingPlayerResult.setPlayerId(yingPlayerId);
			WenzhouShuangkouMingcifen mingcifen = new WenzhouShuangkouMingcifen();
			mingcifen.setMingci(1);
			mingcifen.setYing(true);
			if (shuPlayer.getAllShoupai().size() > 10) {
				mingcifen.setShuangkou(true);
			} else if (shuPlayer.getAllShoupai().size() > 4) {
				mingcifen.setDankou(true);
			} else {
				mingcifen.setPingkou(true);
			}
			mingcifen.calculate();
			yingPlayerResult.setMingcifen(mingcifen);
			yingPlayerResult.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			int[] xianshuCount = playerXianshuMap.get(yingPlayerId);
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
			gongxianfen.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(yingPlayerId, ju));
			gongxianfen.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen);
			WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
			yingPlayerResult.setBufen(bufen);
			panPlayerResultList.add(yingPlayerResult);
			// 输家
			WenzhouShuangkouPanPlayerResult shuPlayerResult = new WenzhouShuangkouPanPlayerResult();
			shuPlayerResult.setPlayerId(shuPlayerId);
			WenzhouShuangkouMingcifen mingcifen1 = new WenzhouShuangkouMingcifen();
			mingcifen1.setMingci(2);
			mingcifen1.setYing(false);
			if (shuPlayer.getAllShoupai().size() > 10) {
				mingcifen1.setShuangkou(true);
			} else if (shuPlayer.getAllShoupai().size() > 4) {
				mingcifen1.setDankou(true);
			} else {
				mingcifen1.setPingkou(true);
			}
			mingcifen1.calculate();
			shuPlayerResult.setMingcifen(mingcifen1);
			int[] xianshuCount1 = playerXianshuMap.get(shuPlayerId);
			shuPlayerResult.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			WenzhouShuangkouGongxianFen gongxianfen1 = new WenzhouShuangkouGongxianFen(xianshuCount1);
			gongxianfen1.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(shuPlayerId, ju));
			gongxianfen1.calculate(renshu);
			shuPlayerResult.setGongxianfen(gongxianfen1);
			WenzhouShuangkouChaixianbufen bufen1 = new WenzhouShuangkouChaixianbufen();
			shuPlayerResult.setBufen(bufen1);
			panPlayerResultList.add(shuPlayerResult);

			// 两两结算贡献分
			for (int i = 0; i < panPlayerResultList.size(); i++) {
				WenzhouShuangkouPanPlayerResult playerResult1 = panPlayerResultList.get(i);
				WenzhouShuangkouGongxianFen gongxian1 = playerResult1.getGongxianfen();
				for (int j = (i + 1); j < panPlayerResultList.size(); j++) {
					WenzhouShuangkouPanPlayerResult playerResult2 = panPlayerResultList.get(j);
					WenzhouShuangkouGongxianFen gongxian2 = playerResult2.getGongxianfen();
					// 结算贡献分
					int fen1 = gongxian1.getValue();
					int fen2 = gongxian2.getValue();
					gongxian1.jiesuan(-fen2);
					gongxian2.jiesuan(-fen1);
				}
			}

			panPlayerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				playerResult.setScore(playerResult.getScore() + playerResult.getGongxianfen().getTotalscore()
						+ playerResult.getBufen().getValue()
						+ playerResult.getMingcifen().getValue() * playerResult.getXianshubeishu());
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(
							playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore());
				} else {
					playerResult.setTotalScore(playerResult.getScore());
				}
			});
			WenzhouShuangkouPanResult wenzhouShuangkouPanResult = new WenzhouShuangkouPanResult();
			wenzhouShuangkouPanResult.setPan(new PanValueObject(currentPan));
			wenzhouShuangkouPanResult.setPanFinishTime(panFinishTime);
			wenzhouShuangkouPanResult.setPanPlayerResultList(panPlayerResultList);
			return wenzhouShuangkouPanResult;
		}
	}

	public PanResult buildCurrentPanResultByChaodi(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		WenzhouShuangkouPanResult latestFinishedPanResult = (WenzhouShuangkouPanResult) ju
				.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (WenzhouShuangkouPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}
		List<WenzhouShuangkouPanPlayerResult> panPlayerResultList = new ArrayList<>();
		List<String> playerIdList = ju.getCurrentPan().getAllPlayerId();
		playerIdList.forEach((playerId) -> {
			WenzhouShuangkouPanPlayerResult playerResult = new WenzhouShuangkouPanPlayerResult();
			playerResult.setPlayerId(playerId);
			WenzhouShuangkouMingcifen mingcifen = new WenzhouShuangkouMingcifen();
			playerResult.setMingcifen(mingcifen);
			playerResult.setXianshubeishu(1);
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen();
			if (chaodiPlayerIdList.contains(playerId)) {
				gongxianfen.setValue(playerTotalGongxianfenMap.get(playerId) + 4);
			} else {
				gongxianfen.setValue(playerTotalGongxianfenMap.get(playerId));
			}
			gongxianfen.setTotalscore(gongxianfen.getValue() * (renshu - 1));
			playerResult.setGongxianfen(gongxianfen);
			WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
			playerResult.setBufen(bufen);
			playerResult.setChaodi(true);
			panPlayerResultList.add(playerResult);
		});

		// 两两结算贡献分
		for (int i = 0; i < panPlayerResultList.size(); i++) {
			WenzhouShuangkouPanPlayerResult playerResult1 = panPlayerResultList.get(i);
			WenzhouShuangkouGongxianFen gongxian1 = playerResult1.getGongxianfen();
			for (int j = (i + 1); j < panPlayerResultList.size(); j++) {
				WenzhouShuangkouPanPlayerResult playerResult2 = panPlayerResultList.get(j);
				WenzhouShuangkouGongxianFen gongxian2 = playerResult2.getGongxianfen();
				// 结算贡献分
				int fen1 = gongxian1.getValue();
				int fen2 = gongxian2.getValue();
				gongxian1.jiesuan(-fen2);
				gongxian2.jiesuan(-fen1);
			}
		}

		panPlayerResultList.forEach((playerResult) -> {
			// 计算当盘总分
			playerResult.setScore(playerResult.getGongxianfen().getTotalscore() + playerResult.getBufen().getValue()
					+ playerResult.getMingcifen().getValue() * playerResult.getXianshubeishu());
			// 计算累计总分
			if (latestFinishedPanResult != null) {
				playerResult
						.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore());
			} else {
				playerResult.setTotalScore(playerResult.getScore());
			}
		});
		WenzhouShuangkouPanResult wenzhouShuangkouPanResult = new WenzhouShuangkouPanResult();
		wenzhouShuangkouPanResult.setChaodi(true);
		wenzhouShuangkouPanResult.setPan(new PanValueObject(currentPan));
		wenzhouShuangkouPanResult.setPanFinishTime(panFinishTime);
		wenzhouShuangkouPanResult.setPanPlayerResultList(panPlayerResultList);
		return wenzhouShuangkouPanResult;
	}

	private List<int[]> calculateShouPaiTotalGongxianfenForPlayer(String playerId, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		ShuangkouPlayer player = currentPan.findPlayer(playerId);
		Map<Integer, PukePai> allShoupai = player.getAllShoupai();
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
		List<int[]> xianshuList = new ArrayList<>();
		int xiaowangCount = dianshuCountArray[13];
		int dawangCount = dianshuCountArray[14];
		if (xiaowangCount + dawangCount == 4) {// 有天王炸
			List<int[]> xianshuList1 = calculatePaiXingWithWangDang(1, dianshuCountArray, xiaowangCount, dawangCount);
			for (int[] xianshuCount1 : xianshuList1) {
				xianshuCount1[2] += 1;
			}
			xianshuList.addAll(xianshuList1);
			List<int[]> xianshuList2 = calculatePaiXingWithoutWangDang(dianshuCountArray);
			for (int[] xianshuCount2 : xianshuList2) {
				xianshuCount2[3] += 1;
			}
			xianshuList.addAll(xianshuList2);
		}
		if (xiaowangCount + dawangCount == 3) {// 有三王炸
			List<int[]> xianshuList3 = calculatePaiXingWithoutWangDang(dianshuCountArray);
			for (int[] xianshuCount3 : xianshuList3) {
				xianshuCount3[2] += 1;
			}
			xianshuList.addAll(xianshuList3);
		}
		int wangCount = 0;
		if (BianXingWanFa.qianbian.equals(bx)) {// 千变
			wangCount = xiaowangCount + dawangCount;
			// 减去王牌的数量
			dianshuCountArray[13] = dianshuCountArray[13] - xiaowangCount;
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
			wangCount = dawangCount;
			// 减去王牌的数量
			if (xiaowangCount > 0 && xiaowangCount % 2 == 0) {
				wangCount++;
				dianshuCountArray[13] = dianshuCountArray[13] - 2;
			}
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
			wangCount = dawangCount;
			// 减去王牌的数量
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else {

		}
		if (wangCount > 0) {
			// 有王牌
			xianshuList.addAll(calculatePaiXingWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount));
		} else {
			// 没有王牌
			xianshuList.addAll(calculatePaiXingWithoutWangDang(dianshuCountArray));
		}
		return xianshuList;
	}

	private List<int[]> calculatePaiXingWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = new PaiXing();
		// 普通炸弹
		DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray, paiXing);
		// 连续炸弹
		DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray, paiXing);
		return calculateBestGongxianfen(dianshuCountArray.clone(), paiXing);
	}

	private List<DianShu> verifyDangFa(int wangCount, int[] dianshuCountArray) {
		List<DianShu> kedangDianShuList = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			if (dianshuCountArray[i] + wangCount >= 4) {
				kedangDianShuList.add(DianShu.getDianShuByOrdinal(i));
			}
		}
		return kedangDianShuList;
	}

	private List<int[]> calculatePaiXingWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount) {
		List<int[]> xianshuList = new ArrayList<>();
		// 计算王可以当哪些牌，提高性能
		List<DianShu> kedangDianShuList = verifyDangFa(dawangCount, dianshuCountArray);
		// 循环王的各种当法
		if (kedangDianShuList.isEmpty()) {
			return xianshuList;
		}
		int size = kedangDianShuList.size();
		int maxZuheCode = (int) Math.pow(size, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(size, wangCount - 1 - m);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			ShoupaiJiesuanPai[] wangDangPaiArray = new ShoupaiJiesuanPai[wangCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int n = 0; n < wangCount; n++) {
				int mod = modArray[n];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {// 计算王的各种当法，排除效果相同的当法
					int yu = temp % mod;
					if (BianXingWanFa.qianbian.equals(bx)) {// 千变
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, kedangDianShuList.get(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, kedangDianShuList.get(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
					} else {

					}
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					wangDangPaiArray = null;
					break;
				}
			}
			if (wangDangPaiArray != null) {
				// 加上当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]++;
				}
				PaiXing paiXing = new PaiXing();
				// 普通炸弹
				DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray, paiXing);
				// 连续炸弹
				DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray, paiXing);
				xianshuList.addAll(calculateBestGongxianfen(dianshuCountArray.clone(), paiXing));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return xianshuList;
	}

	private int calculateXianShu(ZhadanDianShuZu zhadan) {
		if (zhadan instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadan = (DanGeZhadanDianShuZu) zhadan;
			return danGeZhadan.getSize();
		} else if (zhadan instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadan = (LianXuZhadanDianShuZu) zhadan;
			return lianXuZhadan.getXianShu();
		} else {
			WangZhadanDianShuZu wangZhadan = (WangZhadanDianShuZu) zhadan;
			int xiaowangCount = wangZhadan.getXiaowangCount();
			int dawangCount = wangZhadan.getDawangCount();
			if (xiaowangCount + dawangCount == 4) {
				return 7;
			} else {
				return 6;
			}
		}
	}

	private void calculateGongxianfen(int[] dianshuCountArray, int[] xianshuArray,
			List<ZhadanDianShuZu> zhadanDianShuZuList, List<int[]> xianshuList) {
		if (zhadanDianShuZuList.isEmpty()) {
			xianshuList.add(xianshuArray);
		} else {
			for (int i = 0; i < zhadanDianShuZuList.size(); i++) {
				int[] xianshuArray1 = xianshuArray.clone();
				ZhadanDianShuZu zhadanDianShuZu = zhadanDianShuZuList.get(i);
				int xianshu = calculateXianShu(zhadanDianShuZu);
				xianshuArray1[xianshu - 4]++;
				int[] dianshuCountArray1 = removeZhadan(dianshuCountArray.clone(), zhadanDianShuZu);
				List<ZhadanDianShuZu> zhadanDianShuZuList1 = calculateTotalGongxianfenForDianshuCountArray(
						dianshuCountArray1);
				calculateGongxianfen(dianshuCountArray1, xianshuArray1, zhadanDianShuZuList1, xianshuList);
			}
		}
	}

	/**
	 * 根据牌型和牌数计算最佳贡献分
	 */
	private List<int[]> calculateBestGongxianfen(int[] dianshuCountArray, PaiXing paixing) {
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);

		List<int[]> xianshuList = new ArrayList<>();
		int[] xianshuArray = new int[9];
		calculateGongxianfen(dianshuCountArray, xianshuArray, zhadanDianShuZuList, xianshuList);

		return xianshuList;
	}

	private List<ZhadanDianShuZu> calculateTotalGongxianfenForDianshuCountArray(int[] dianshuCountArray) {
		int xiaowangCount = dianshuCountArray[13];
		int dawangCount = dianshuCountArray[14];
		int wangCount = 0;
		if (BianXingWanFa.qianbian.equals(bx)) {// 千变
			wangCount = xiaowangCount + dawangCount;
			// 减去王牌的数量
			dianshuCountArray[13] = dianshuCountArray[13] - xiaowangCount;
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
			wangCount = dawangCount;
			// 减去王牌的数量
			if (xiaowangCount > 0 && xiaowangCount % 2 == 0) {
				wangCount++;
				dianshuCountArray[13] = dianshuCountArray[13] - 2;
			}
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
			wangCount = dawangCount;
			// 减去王牌的数量
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else {

		}
		if (wangCount > 0) {
			// 有王牌
			return calculateZhadanDianShuZuWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount);
		} else {
			// 没有王牌
			return calculateListZhadanDianShuZuWithoutWangDang(dianshuCountArray);
		}
	}

	private List<ZhadanDianShuZu> calculateListZhadanDianShuZuWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = new PaiXing();
		// 普通炸弹
		DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray, paiXing);
		// 连续炸弹
		DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray, paiXing);
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paiXing.getZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paiXing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
		return zhadanDianShuZuList;
	}

	private List<ZhadanDianShuZu> calculateZhadanDianShuZuWithWangDang(int wangCount, int[] dianshuCountArray,
			int xiaowangCount, int dawangCount) {
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		// 计算王可以当哪些牌，提高性能
		List<DianShu> kedangDianShuList = verifyDangFa(dawangCount, dianshuCountArray);
		// 循环王的各种当法
		if (kedangDianShuList.isEmpty()) {
			return zhadanDianShuZuList;
		}
		int size = kedangDianShuList.size();
		int maxZuheCode = (int) Math.pow(size, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(size, wangCount - 1 - m);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			ShoupaiJiesuanPai[] wangDangPaiArray = new ShoupaiJiesuanPai[wangCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int n = 0; n < wangCount; n++) {
				int mod = modArray[n];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {// 计算王的各种当法，排除效果相同的当法
					int yu = temp % mod;
					if (BianXingWanFa.qianbian.equals(bx)) {// 千变
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, kedangDianShuList.get(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, kedangDianShuList.get(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
					} else {

					}
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					wangDangPaiArray = null;
					break;
				}
			}
			if (wangDangPaiArray != null) {
				// 加上当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]++;
				}
				PaiXing paiXing = new PaiXing();
				// 普通炸弹
				DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray, paiXing);
				// 连续炸弹
				DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray, paiXing);
				List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paiXing.getZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
				List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paiXing.getLianXuZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return zhadanDianShuZuList;
	}

	/**
	 * 从手牌中移除打出的炸弹
	 */
	private int[] removeZhadan(int[] dianshuCountArray, ZhadanDianShuZu zhadanDianShuZu) {
		if (zhadanDianShuZu instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanDianShuZu;
			dianshuCountArray[danGeZhadanDianShuZu.getDianShu().ordinal()] -= danGeZhadanDianShuZu.getSize();
		}
		if (zhadanDianShuZu instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) zhadanDianShuZu;
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
			for (int i = 0; i < lianXuDianShuArray.length; i++) {
				dianshuCountArray[lianXuDianShuArray[i].ordinal()] -= lianXuDianShuSizeArray[i];
			}
		}
		if (zhadanDianShuZu instanceof WangZhadanDianShuZu) {
			WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) zhadanDianShuZu;
			dianshuCountArray[13] -= wangZhadanDianShuZu.getXiaowangCount();
			dianshuCountArray[14] -= wangZhadanDianShuZu.getDawangCount();
		}
		return dianshuCountArray;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public Map<String, Integer> getPlayerTotalGongxianfenMap() {
		return playerTotalGongxianfenMap;
	}

	public void setPlayerTotalGongxianfenMap(Map<String, Integer> playerTotalGongxianfenMap) {
		this.playerTotalGongxianfenMap = playerTotalGongxianfenMap;
	}

	public List<String> getChaodiPlayerIdList() {
		return chaodiPlayerIdList;
	}

	public void setChaodiPlayerIdList(List<String> chaodiPlayerIdList) {
		this.chaodiPlayerIdList = chaodiPlayerIdList;
	}

	public Map<String, Integer> getPlayerMaxXianshuMap() {
		return playerMaxXianshuMap;
	}

	public void setPlayerMaxXianshuMap(Map<String, Integer> playerMaxXianshuMap) {
		this.playerMaxXianshuMap = playerMaxXianshuMap;
	}

	public Map<String, Integer> getPlayerOtherMaxXianshuMap() {
		return playerOtherMaxXianshuMap;
	}

	public void setPlayerOtherMaxXianshuMap(Map<String, Integer> playerOtherMaxXianshuMap) {
		this.playerOtherMaxXianshuMap = playerOtherMaxXianshuMap;
	}

}
