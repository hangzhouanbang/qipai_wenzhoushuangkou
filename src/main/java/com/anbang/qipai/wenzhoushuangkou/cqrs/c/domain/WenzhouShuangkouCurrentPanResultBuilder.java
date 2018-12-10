package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, Integer> playeTotalGongxianfenMap = new HashMap<>();
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
		XianshuCountDaActionStatisticsListener wenzhouShuangkouListener = ju.getActionStatisticsListenerManager()
				.findDaListener(XianshuCountDaActionStatisticsListener.class);
		Map<String, int[]> playerXianshuMap = wenzhouShuangkouListener.getPlayerXianshuMap();
		List<String> noPaiPlayerIdList = currentPan.getNoPaiPlayerIdList();
		if (renshu > 2) {// 4人游戏
			List<WenzhouShuangkouPanPlayerResult> panPlayerResultList = new ArrayList<>();
			String yingPlayerId = noPaiPlayerIdList.get(0);
			ShuangkouPlayer duijiaPlayer = currentPan.findDuijiaPlayer(yingPlayerId);
			String playerId1 = noPaiPlayerIdList.get(1);
			String playerId2 = noPaiPlayerIdList.get(2);
			String playerId3 = "";
			for (String pid : ju.getCurrentPan().getAllPlayerId()) {
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
			gongxianfen.calculateXianshu();
			gongxianfen.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen);
			WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
			bufen.setTotalScore(playeTotalGongxianfenMap.get(yingPlayerId));
			bufen.setScore(gongxianfen.getValue());
			bufen.calculate();
			yingPlayerResult.setBufen(bufen);
			panPlayerResultList.add(yingPlayerResult);

			// 玩家1
			WenzhouShuangkouPanPlayerResult playerResult1 = new WenzhouShuangkouPanPlayerResult();
			playerResult1.setPlayerId(playerId1);
			WenzhouShuangkouMingcifen mingcifen1 = new WenzhouShuangkouMingcifen();
			playerResult1.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen1.setYing(true);
				mingcifen1.setShuangkou(true);
				mingcifen1.setMingci(2);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen1.setYing(false);
				mingcifen1.setDankou(true);
				mingcifen1.setMingci(3);
			} else {
				mingcifen1.setYing(false);
				mingcifen1.setPingkou(true);
				mingcifen1.setMingci(4);
			}
			mingcifen1.calculate();
			playerResult1.setMingcifen(mingcifen1);
			int[] xianshuCount1 = playerXianshuMap.get(playerId1);
			WenzhouShuangkouGongxianFen gongxianfen1 = new WenzhouShuangkouGongxianFen(xianshuCount1);
			gongxianfen1.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(playerId1, ju));
			gongxianfen1.calculateXianshu();
			gongxianfen1.calculate(renshu);
			playerResult1.setGongxianfen(gongxianfen1);
			WenzhouShuangkouChaixianbufen bufen1 = new WenzhouShuangkouChaixianbufen();
			bufen1.setTotalScore(playeTotalGongxianfenMap.get(playerId1));
			bufen1.setScore(gongxianfen1.getValue());
			bufen1.calculate();
			playerResult1.setBufen(bufen1);
			panPlayerResultList.add(playerResult1);

			// 玩家2
			WenzhouShuangkouPanPlayerResult playerResult2 = new WenzhouShuangkouPanPlayerResult();
			playerResult2.setPlayerId(playerId2);
			WenzhouShuangkouMingcifen mingcifen2 = new WenzhouShuangkouMingcifen();
			playerResult2.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen2.setYing(false);
				mingcifen2.setShuangkou(true);
				mingcifen2.setMingci(2);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen2.setYing(true);
				mingcifen2.setDankou(true);
				mingcifen2.setMingci(3);
			} else {
				mingcifen2.setYing(false);
				playerResult2.setXianshubeishu(playerOtherMaxXianshuMap.get(playerId2));
				mingcifen2.setPingkou(true);
				mingcifen2.setMingci(4);
			}
			mingcifen2.calculate();
			playerResult2.setMingcifen(mingcifen2);
			int[] xianshuCount2 = playerXianshuMap.get(playerId2);
			WenzhouShuangkouGongxianFen gongxianfen2 = new WenzhouShuangkouGongxianFen(xianshuCount2);
			gongxianfen2.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(playerId2, ju));
			gongxianfen2.calculateXianshu();
			gongxianfen2.calculate(renshu);
			playerResult2.setGongxianfen(gongxianfen2);
			WenzhouShuangkouChaixianbufen bufen2 = new WenzhouShuangkouChaixianbufen();
			bufen2.setTotalScore(playeTotalGongxianfenMap.get(playerId2));
			bufen2.setScore(gongxianfen2.getValue());
			bufen2.calculate();
			playerResult2.setBufen(bufen2);
			panPlayerResultList.add(playerResult2);

			// 玩家3
			WenzhouShuangkouPanPlayerResult playerResult3 = new WenzhouShuangkouPanPlayerResult();
			playerResult3.setPlayerId(playerId3);
			WenzhouShuangkouMingcifen mingcifen3 = new WenzhouShuangkouMingcifen();
			playerResult3.setXianshubeishu(playerMaxXianshuMap.get(yingPlayerId));
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen3.setYing(false);
				mingcifen3.setShuangkou(true);
				mingcifen3.setMingci(2);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen3.setYing(false);
				mingcifen3.setDankou(true);
				mingcifen3.setMingci(3);
			} else {
				mingcifen3.setYing(true);
				playerResult3.setXianshubeishu(playerMaxXianshuMap.get(playerId3));
				mingcifen3.setPingkou(true);
				mingcifen3.setMingci(4);
			}
			mingcifen3.calculate();
			playerResult3.setMingcifen(mingcifen3);
			int[] xianshuCount3 = playerXianshuMap.get(playerId3);
			WenzhouShuangkouGongxianFen gongxianfen3 = new WenzhouShuangkouGongxianFen(xianshuCount3);
			gongxianfen3.calculateShouPaiXianshu(calculateShouPaiTotalGongxianfenForPlayer(playerId3, ju));
			gongxianfen3.calculateXianshu();
			gongxianfen3.calculate(renshu);
			playerResult3.setGongxianfen(gongxianfen3);
			WenzhouShuangkouChaixianbufen bufen3 = new WenzhouShuangkouChaixianbufen();
			bufen3.setTotalScore(playeTotalGongxianfenMap.get(playerId3));
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
							break;
						}
					}
				}
				if (playerId3.equals(playerResulti.getPlayerId())) {
					WenzhouShuangkouChaixianbufen chaixianBufen1 = playerResulti.getBufen();
					int bufeni = chaixianBufen1.getValue();
					ShuangkouPlayer duijiaPlayer1 = currentPan.findDuijiaPlayer(playerId3);
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
			gongxianfen.calculateXianshu();
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
			gongxianfen1.calculateXianshu();
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
				gongxianfen.setValue(playeTotalGongxianfenMap.get(playerId) + 4);
			} else {
				gongxianfen.setValue(playeTotalGongxianfenMap.get(playerId));
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

	private int[] calculateShouPaiTotalGongxianfenForPlayer(String playerId, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		ShuangkouPlayer player = currentPan.findPlayer(playerId);
		Map<Integer, PukePai> allShoupai = player.getAllShoupai();
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
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
			return calculatePaiXingWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount);
		} else {
			// 没有王牌
			return calculatePaiXingWithoutWangDang(dianshuCountArray);
		}
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

	/**
	 * 根据牌型和牌数计算最佳贡献分
	 */
	private int[] calculateBestGongxianfen(int[] dianshuCountArray, PaiXing paixing) {
		int[] xianshuCount = new int[9];
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = paixing.getWangZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(wangZhadanDianShuZuList);

		while (true) {//
			ZhadanDianShuZu bestZhadanDianShuZu = null;
			int bestXianShu = 0;
			for (ZhadanDianShuZu zhadanDianShuZu : zhadanDianShuZuList) {
				if (verifyZhadanConflict(dianshuCountArray, zhadanDianShuZu)) {
					int xianshu = calculateXianShu(zhadanDianShuZu);
					if (bestZhadanDianShuZu == null || xianshu >= bestXianShu) {// 相同线数优先取非连续牌组
						if (xianshu > bestXianShu) {
							bestZhadanDianShuZu = zhadanDianShuZu;
							bestXianShu = xianshu;
						} else {
							if (!(zhadanDianShuZu instanceof LianXuZhadanDianShuZu)) {
								bestZhadanDianShuZu = zhadanDianShuZu;
								bestXianShu = xianshu;
							}
						}
					}
				}
			}
			if (bestZhadanDianShuZu != null) {
				zhadanDianShuZuList.remove(bestZhadanDianShuZu);
				xianshuCount[bestXianShu - 4]++;
				if (bestZhadanDianShuZu instanceof DanGeZhadanDianShuZu) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) bestZhadanDianShuZu;
					dianshuCountArray[danGeZhadanDianShuZu.getDianShu().ordinal()] -= danGeZhadanDianShuZu.getSize();
				}
				if (bestZhadanDianShuZu instanceof LianXuZhadanDianShuZu) {
					LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) bestZhadanDianShuZu;
					DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
					int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
					for (int i = 0; i < lianXuDianShuArray.length; i++) {
						dianshuCountArray[lianXuDianShuArray[i].ordinal()] -= lianXuDianShuSizeArray[i];
					}
				}
				if (bestZhadanDianShuZu instanceof WangZhadanDianShuZu) {
					WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) bestZhadanDianShuZu;
					dianshuCountArray[13] -= wangZhadanDianShuZu.getXiaowangCount();
					dianshuCountArray[14] -= wangZhadanDianShuZu.getDawangCount();
				}
			} else {
				return xianshuCount;
			}
		}
	}

	/**
	 * 验算炸弹是否合理
	 */
	private boolean verifyZhadanConflict(int[] dianshuCountArray, ZhadanDianShuZu zhadanDianShuZu) {
		if (zhadanDianShuZu instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanDianShuZu;
			if (dianshuCountArray[danGeZhadanDianShuZu.getDianShu().ordinal()] - danGeZhadanDianShuZu.getSize() < 0) {
				return false;
			}
		}
		if (zhadanDianShuZu instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) zhadanDianShuZu;
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
			for (int i = 0; i < lianXuDianShuArray.length; i++) {
				if (dianshuCountArray[lianXuDianShuArray[i].ordinal()] - lianXuDianShuSizeArray[i] < 0) {
					return false;
				}
			}
		}
		if (zhadanDianShuZu instanceof WangZhadanDianShuZu) {
			WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) zhadanDianShuZu;
			if (dianshuCountArray[13] - wangZhadanDianShuZu.getXiaowangCount() < 0
					|| dianshuCountArray[14] - wangZhadanDianShuZu.getDawangCount() < 0) {
				return false;
			}
		}
		return true;
	}

	private int[] calculatePaiXingWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		return calculateBestGongxianfen(dianshuCountArray.clone(), paiXing);
	}

	private int[] calculatePaiXingWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount) {
		int bestScore = 0;
		int[] bestXianshuCount = new int[9];
		// 循环王的各种当法
		int maxZuheCode = (int) Math.pow(13, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(13, wangCount - 1 - m);
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
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
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
				PaiXing paixing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
				int[] xianshuCount = calculateBestGongxianfen(dianshuCountArray.clone(), paixing);
				WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
				gongxianfen.calculateXianshu();
				gongxianfen.calculate(renshu);
				int score = gongxianfen.getValue();
				if (score > bestScore) {
					bestScore = score;
					bestXianshuCount = xianshuCount;
				}
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return bestXianshuCount;
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

	public Map<String, Integer> getPlayeTotalGongxianfenMap() {
		return playeTotalGongxianfenMap;
	}

	public void setPlayeTotalGongxianfenMap(Map<String, Integer> playeTotalGongxianfenMap) {
		this.playeTotalGongxianfenMap = playeTotalGongxianfenMap;
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
