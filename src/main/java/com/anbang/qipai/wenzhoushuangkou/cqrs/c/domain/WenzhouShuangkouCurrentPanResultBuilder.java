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
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pan.CurrentPanResultBuilder;
import com.dml.shuangkou.pan.Pan;
import com.dml.shuangkou.pan.PanResult;
import com.dml.shuangkou.pan.PanValueObject;
import com.dml.shuangkou.player.ShuangkouPlayer;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class WenzhouShuangkouCurrentPanResultBuilder implements CurrentPanResultBuilder {
	private int renshu;
	private BianXingWanFa bx;
	private boolean fengding;
	private Map<String, WenzhouShuangkouGongxianFen> playerTotalGongxianfenMap = new HashMap<>();
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
		List<String> playerIds = currentPan.findAllPlayerId();
		XianshuCountDaActionStatisticsListener wenzhouShuangkouListener = ju.getActionStatisticsListenerManager()
				.findDaListener(XianshuCountDaActionStatisticsListener.class);
		Map<String, int[]> playerXianshuMap = wenzhouShuangkouListener.getPlayerXianshuMap();

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
			WenzhouShuangkouGongxianFen gongxianfen = calculateTotalGongxianfenForPlayer(yingPlayerId, xianshuCount,
					ju);
			gongxianfen.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen);
			int[] xianshuCountArray = new int[9];
			xianshuCountArray[0] = gongxianfen.getSixian();
			xianshuCountArray[1] = gongxianfen.getWuxian();
			xianshuCountArray[2] = gongxianfen.getLiuxian();
			xianshuCountArray[3] = gongxianfen.getQixian();
			xianshuCountArray[4] = gongxianfen.getBaxian();
			xianshuCountArray[5] = gongxianfen.getJiuxian();
			xianshuCountArray[6] = gongxianfen.getShixian();
			xianshuCountArray[7] = gongxianfen.getShiyixian();
			xianshuCountArray[8] = gongxianfen.getShierxian();
			WenzhouShuangkouXianshuBeishu beishu = new WenzhouShuangkouXianshuBeishu(xianshuCountArray);
			beishu.calculate(fengding);
			yingPlayerResult.setXianshubeishu(beishu.getValue());
			WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
			bufen.setTotalScore(playerTotalGongxianfenMap.get(yingPlayerId).getValue());
			bufen.setScore(gongxianfen.getValue());
			bufen.calculate();
			yingPlayerResult.setBufen(bufen);
			panPlayerResultList.add(yingPlayerResult);

			// 玩家1
			WenzhouShuangkouPanPlayerResult playerResult1 = new WenzhouShuangkouPanPlayerResult();
			playerResult1.setPlayerId(playerId1);
			WenzhouShuangkouMingcifen mingcifen1 = new WenzhouShuangkouMingcifen();
			mingcifen1.setMingci(2);
			playerResult1.setXianshubeishu(beishu.getValue());
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
			WenzhouShuangkouGongxianFen gongxianfen1 = calculateTotalGongxianfenForPlayer(playerId1, xianshuCount1, ju);
			gongxianfen1.calculate(renshu);
			playerResult1.setGongxianfen(gongxianfen1);
			WenzhouShuangkouChaixianbufen bufen1 = new WenzhouShuangkouChaixianbufen();
			bufen1.setTotalScore(playerTotalGongxianfenMap.get(playerId1).getValue());
			bufen1.setScore(gongxianfen1.getValue());
			bufen1.calculate();
			playerResult1.setBufen(bufen1);
			panPlayerResultList.add(playerResult1);

			// 玩家2
			WenzhouShuangkouPanPlayerResult playerResult2 = new WenzhouShuangkouPanPlayerResult();
			playerResult2.setPlayerId(playerId2);
			WenzhouShuangkouMingcifen mingcifen2 = new WenzhouShuangkouMingcifen();
			mingcifen2.setMingci(3);
			playerResult2.setXianshubeishu(beishu.getValue());
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
			mingcifen2.calculate();
			playerResult2.setMingcifen(mingcifen2);
			int[] xianshuCount2 = playerXianshuMap.get(playerId2);
			WenzhouShuangkouGongxianFen gongxianfen2 = calculateTotalGongxianfenForPlayer(playerId2, xianshuCount2, ju);
			gongxianfen2.calculate(renshu);
			playerResult2.setGongxianfen(gongxianfen2);
			WenzhouShuangkouChaixianbufen bufen2 = new WenzhouShuangkouChaixianbufen();
			bufen2.setTotalScore(playerTotalGongxianfenMap.get(playerId2).getValue());
			bufen2.setScore(gongxianfen2.getValue());
			bufen2.calculate();
			playerResult2.setBufen(bufen2);
			panPlayerResultList.add(playerResult2);

			// 玩家3
			WenzhouShuangkouPanPlayerResult playerResult3 = new WenzhouShuangkouPanPlayerResult();
			playerResult3.setPlayerId(playerId3);
			WenzhouShuangkouMingcifen mingcifen3 = new WenzhouShuangkouMingcifen();
			mingcifen3.setMingci(4);
			playerResult3.setXianshubeishu(beishu.getValue());
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
			mingcifen3.calculate();
			playerResult3.setMingcifen(mingcifen3);
			int[] xianshuCount3 = playerXianshuMap.get(playerId3);
			WenzhouShuangkouGongxianFen gongxianfen3 = calculateTotalGongxianfenForPlayer(playerId3, xianshuCount3, ju);
			gongxianfen3.calculate(renshu);
			playerResult3.setGongxianfen(gongxianfen3);
			WenzhouShuangkouChaixianbufen bufen3 = new WenzhouShuangkouChaixianbufen();
			bufen3.setTotalScore(playerTotalGongxianfenMap.get(playerId3).getValue());
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
			int[] xianshuCount = playerXianshuMap.get(yingPlayerId);
			WenzhouShuangkouGongxianFen gongxianfen = calculateTotalGongxianfenForPlayer(yingPlayerId, xianshuCount,
					ju);
			gongxianfen.calculate(renshu);
			yingPlayerResult.setGongxianfen(gongxianfen);
			int[] xianshuCountArray = new int[9];
			xianshuCountArray[0] = gongxianfen.getSixian();
			xianshuCountArray[1] = gongxianfen.getWuxian();
			xianshuCountArray[2] = gongxianfen.getLiuxian();
			xianshuCountArray[3] = gongxianfen.getQixian();
			xianshuCountArray[4] = gongxianfen.getBaxian();
			xianshuCountArray[5] = gongxianfen.getJiuxian();
			xianshuCountArray[6] = gongxianfen.getShixian();
			xianshuCountArray[7] = gongxianfen.getShiyixian();
			xianshuCountArray[8] = gongxianfen.getShierxian();
			WenzhouShuangkouXianshuBeishu beishu = new WenzhouShuangkouXianshuBeishu(xianshuCountArray);
			beishu.calculate(fengding);
			yingPlayerResult.setXianshubeishu(beishu.getValue());
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
			shuPlayerResult.setXianshubeishu(beishu.getValue());
			WenzhouShuangkouGongxianFen gongxianfen1 = calculateTotalGongxianfenForPlayer(shuPlayerId, xianshuCount1,
					ju);
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
		List<String> playerIdList = ju.getCurrentPan().findAllPlayerId();
		playerIdList.forEach((playerId) -> {
			WenzhouShuangkouPanPlayerResult playerResult = new WenzhouShuangkouPanPlayerResult();
			playerResult.setPlayerId(playerId);
			WenzhouShuangkouMingcifen mingcifen = new WenzhouShuangkouMingcifen();
			playerResult.setMingcifen(mingcifen);
			playerResult.setXianshubeishu(1);
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen();
			if (chaodiPlayerIdList.contains(playerId)) {
				gongxianfen.setValue(playerTotalGongxianfenMap.get(playerId).getValue() + 4);
			} else {
				gongxianfen.setValue(playerTotalGongxianfenMap.get(playerId).getValue());
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

	private WenzhouShuangkouGongxianFen calculateTotalGongxianfenForPlayer(String playerId, int[] xianshuCount, Ju ju) {
		WenzhouShuangkouGongxianFen bestGongxianfen = WenzhouShuangkouShoupaiGongxianfenCalculator
				.calculateTotalGongxianfenWithShouPaiForPlayer(playerId, ju, bx, xianshuCount);
		return bestGongxianfen;
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

	public Map<String, WenzhouShuangkouGongxianFen> getPlayerTotalGongxianfenMap() {
		return playerTotalGongxianfenMap;
	}

	public void setPlayerTotalGongxianfenMap(Map<String, WenzhouShuangkouGongxianFen> playerTotalGongxianfenMap) {
		this.playerTotalGongxianfenMap = playerTotalGongxianfenMap;
	}

	public List<String> getChaodiPlayerIdList() {
		return chaodiPlayerIdList;
	}

	public void setChaodiPlayerIdList(List<String> chaodiPlayerIdList) {
		this.chaodiPlayerIdList = chaodiPlayerIdList;
	}

	public boolean isFengding() {
		return fengding;
	}

	public void setFengding(boolean fengding) {
		this.fengding = fengding;
	}

}
