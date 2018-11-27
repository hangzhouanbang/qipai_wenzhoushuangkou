package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.listener.XianshuCountDaActionStatisticsListener;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pan.CurrentPanResultBuilder;
import com.dml.shuangkou.pan.Pan;
import com.dml.shuangkou.pan.PanResult;
import com.dml.shuangkou.pan.PanValueObject;
import com.dml.shuangkou.player.ShuangkouPlayer;

public class WenzhouShuangkouCurrentPanResultBuilder implements CurrentPanResultBuilder {
	private int renshu;
	private Map<String, Integer> playeTotalGongxianfenMap = new HashMap<>();
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
			mingcifen.setYing(true);
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen.setDankou(true);
			} else {
				mingcifen.setPingkou(true);
			}
			yingPlayerResult.setMingcifen(mingcifen);
			int[] xianshuCount = playerXianshuMap.get(yingPlayerId);
			WenzhouShuangkouXianshuBeishu xianshubeishu = new WenzhouShuangkouXianshuBeishu(xianshuCount);
			xianshubeishu.calculate();
			yingPlayerResult.setXianshubeishu(xianshubeishu);
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
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
			playerResult1.setMingcifen(mingcifen1);
			int[] xianshuCount1 = playerXianshuMap.get(playerId1);
			WenzhouShuangkouXianshuBeishu xianshubeishu1 = new WenzhouShuangkouXianshuBeishu(xianshuCount1);
			xianshubeishu1.calculate();
			playerResult1.setXianshubeishu(xianshubeishu1);
			WenzhouShuangkouGongxianFen gongxianfen1 = new WenzhouShuangkouGongxianFen(xianshuCount1);
			gongxianfen1.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen1);
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
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen2.setYing(false);
				mingcifen2.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen2.setYing(true);
				mingcifen2.setDankou(true);
			} else {
				mingcifen2.setYing(false);
				mingcifen2.setPingkou(true);
			}
			playerResult2.setMingcifen(mingcifen2);
			int[] xianshuCount2 = playerXianshuMap.get(playerId2);
			WenzhouShuangkouXianshuBeishu xianshubeishu2 = new WenzhouShuangkouXianshuBeishu(xianshuCount2);
			xianshubeishu2.calculate();
			playerResult2.setXianshubeishu(xianshubeishu2);
			WenzhouShuangkouGongxianFen gongxianfen2 = new WenzhouShuangkouGongxianFen(xianshuCount2);
			gongxianfen2.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen2);
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
			if (playerId1.equals(duijiaPlayer.getId())) {
				mingcifen3.setYing(false);
				mingcifen3.setShuangkou(true);
			} else if (playerId2.equals(duijiaPlayer.getId())) {
				mingcifen3.setYing(false);
				mingcifen3.setDankou(true);
			} else {
				mingcifen3.setYing(true);
				mingcifen3.setPingkou(true);
			}
			playerResult3.setMingcifen(mingcifen3);
			int[] xianshuCount3 = playerXianshuMap.get(playerId3);
			WenzhouShuangkouXianshuBeishu xianshubeishu3 = new WenzhouShuangkouXianshuBeishu(xianshuCount3);
			xianshubeishu3.calculate();
			playerResult3.setXianshubeishu(xianshubeishu3);
			WenzhouShuangkouGongxianFen gongxianfen3 = new WenzhouShuangkouGongxianFen(xianshuCount3);
			gongxianfen3.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen3);
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
						if (duijiaPlayer.getId().equals(playerResulti.getPlayerId())) {
							WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
							WenzhouShuangkouChaixianbufen chaixianBufen2 = playerResultj.getBufen();
							int bufenj = chaixianBufen2.getValue();
							// 结算补分
							chaixianBufen1.jiesuan(-bufenj);
							chaixianBufen2.jiesuan(-bufeni);
						}
					}
				}
				if (playerId3.equals(playerResulti.getPlayerId())) {
					WenzhouShuangkouChaixianbufen chaixianBufen1 = playerResulti.getBufen();
					int bufeni = chaixianBufen1.getValue();
					ShuangkouPlayer duijiaPlayer1 = currentPan.findDuijiaPlayer(playerId3);
					for (int j = 0; j < panPlayerResultList.size(); j++) {
						if (duijiaPlayer1.getId().equals(playerResulti.getPlayerId())) {
							WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
							WenzhouShuangkouChaixianbufen chaixianBufen2 = playerResultj.getBufen();
							int bufenj = chaixianBufen2.getValue();
							// 结算补分
							chaixianBufen1.jiesuan(-bufenj);
							chaixianBufen2.jiesuan(-bufeni);
						}
					}
				}
			}

			panPlayerResultList.forEach((playerResult) -> {
				// 计算当盘总分
				playerResult.setScore(playerResult.getGongxianfen().getTotalscore() + playerResult.getBufen().getValue()
						+ playerResult.getMingcifen().getValue() * playerResult.getXianshubeishu().getValue());
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
			String shuPlayerId = noPaiPlayerIdList.get(1);
			ShuangkouPlayer shuPlayer = currentPan.findPlayer(shuPlayerId);
			// 赢家
			WenzhouShuangkouPanPlayerResult yingPlayerResult = new WenzhouShuangkouPanPlayerResult();
			yingPlayerResult.setPlayerId(yingPlayerId);
			WenzhouShuangkouMingcifen mingcifen = new WenzhouShuangkouMingcifen();
			mingcifen.setYing(true);
			if (shuPlayer.getAllShoupai().size() > 10) {
				mingcifen.setShuangkou(true);
			} else if (shuPlayer.getAllShoupai().size() > 4) {
				mingcifen.setDankou(true);
			} else {
				mingcifen.setPingkou(true);
			}
			yingPlayerResult.setMingcifen(mingcifen);
			int[] xianshuCount = playerXianshuMap.get(yingPlayerId);
			WenzhouShuangkouXianshuBeishu xianshubeishu = new WenzhouShuangkouXianshuBeishu(xianshuCount);
			xianshubeishu.calculate();
			yingPlayerResult.setXianshubeishu(xianshubeishu);
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
			gongxianfen.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen);
			WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
			yingPlayerResult.setBufen(bufen);
			panPlayerResultList.add(yingPlayerResult);
			// 输家
			WenzhouShuangkouPanPlayerResult shuPlayerResult = new WenzhouShuangkouPanPlayerResult();
			shuPlayerResult.setPlayerId(shuPlayerId);
			WenzhouShuangkouMingcifen mingcifen1 = new WenzhouShuangkouMingcifen();
			mingcifen1.setYing(false);
			if (shuPlayer.getAllShoupai().size() > 10) {
				mingcifen1.setShuangkou(true);
			} else if (shuPlayer.getAllShoupai().size() > 4) {
				mingcifen1.setDankou(true);
			} else {
				mingcifen1.setPingkou(true);
			}
			shuPlayerResult.setMingcifen(mingcifen1);
			int[] xianshuCount1 = playerXianshuMap.get(shuPlayerId);
			WenzhouShuangkouXianshuBeishu xianshubeishu1 = new WenzhouShuangkouXianshuBeishu(xianshuCount1);
			xianshubeishu1.calculate();
			shuPlayerResult.setXianshubeishu(xianshubeishu1);
			WenzhouShuangkouGongxianFen gongxianfen1 = new WenzhouShuangkouGongxianFen(xianshuCount1);
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
						+ playerResult.getMingcifen().getValue() * playerResult.getXianshubeishu().getValue());
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
			WenzhouShuangkouXianshuBeishu xianshubeishu = new WenzhouShuangkouXianshuBeishu();
			playerResult.setXianshubeishu(xianshubeishu);
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
					+ playerResult.getMingcifen().getValue() * playerResult.getXianshubeishu().getValue());
			// 计算累计总分
			if (latestFinishedPanResult != null) {
				playerResult
						.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore());
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

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
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

}
