package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouChaixianbufen;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.WenzhouShuangkouPanPlayerResult;

public class CaseTest2 {
	public static void main(String[] args) {
		String yingPlayerId = "982807";
		String playerId1 = "235698";
		String playerId2 = "360208";
		String playerId3 = "520830";
		List<WenzhouShuangkouPanPlayerResult> panPlayerResultList = new ArrayList<>();
		// 赢家
		WenzhouShuangkouPanPlayerResult yingPlayerResult = new WenzhouShuangkouPanPlayerResult();
		yingPlayerResult.setPlayerId(yingPlayerId);
		WenzhouShuangkouChaixianbufen bufen = new WenzhouShuangkouChaixianbufen();
		bufen.setTotalScore(132);
		bufen.setScore(24);
		bufen.calculate();
		yingPlayerResult.setBufen(bufen);
		panPlayerResultList.add(yingPlayerResult);

		// 玩家1
		WenzhouShuangkouPanPlayerResult playerResult1 = new WenzhouShuangkouPanPlayerResult();
		playerResult1.setPlayerId(playerId1);
		WenzhouShuangkouChaixianbufen bufen1 = new WenzhouShuangkouChaixianbufen();
		bufen1.setTotalScore(12);
		bufen1.setScore(12);
		bufen1.calculate();
		playerResult1.setBufen(bufen1);
		panPlayerResultList.add(playerResult1);

		// 玩家2
		WenzhouShuangkouPanPlayerResult playerResult2 = new WenzhouShuangkouPanPlayerResult();
		playerResult2.setPlayerId(playerId2);
		WenzhouShuangkouChaixianbufen bufen2 = new WenzhouShuangkouChaixianbufen();
		bufen2.setTotalScore(36);
		bufen2.setScore(8);
		bufen2.calculate();
		playerResult2.setBufen(bufen2);
		panPlayerResultList.add(playerResult2);

		// 玩家3
		WenzhouShuangkouPanPlayerResult playerResult3 = new WenzhouShuangkouPanPlayerResult();
		playerResult3.setPlayerId(playerId3);
		WenzhouShuangkouChaixianbufen bufen3 = new WenzhouShuangkouChaixianbufen();
		bufen3.setTotalScore(24);
		bufen3.setScore(12);
		bufen3.calculate();
		playerResult3.setBufen(bufen3);
		panPlayerResultList.add(playerResult3);

		// 计算补分
		for (int i = 0; i < panPlayerResultList.size(); i++) {
			WenzhouShuangkouPanPlayerResult playerResulti = panPlayerResultList.get(i);
			if (yingPlayerId.equals(playerResulti.getPlayerId())) {
				WenzhouShuangkouChaixianbufen chaixianBufen1 = playerResulti.getBufen();
				int bufeni = chaixianBufen1.getValue();
				for (int j = 0; j < panPlayerResultList.size(); j++) {
					WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
					if ("520830".equals(playerResultj.getPlayerId())) {
						WenzhouShuangkouChaixianbufen chaixianBufen2 = playerResultj.getBufen();
						int bufenj = chaixianBufen2.getValue();
						// 结算补分
						chaixianBufen1.jiesuan(-bufenj);
						chaixianBufen2.jiesuan(-bufeni);
						break;
					}
				}
			}
			if ("235698".equals(playerResulti.getPlayerId())) {
				WenzhouShuangkouChaixianbufen chaixianBufen1 = playerResulti.getBufen();
				int bufeni = chaixianBufen1.getValue();
				for (int j = 0; j < panPlayerResultList.size(); j++) {
					WenzhouShuangkouPanPlayerResult playerResultj = panPlayerResultList.get(j);
					if ("360208".equals(playerResultj.getPlayerId())) {
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
		for (int i = 0; i < panPlayerResultList.size(); i++) {
			System.out.println(panPlayerResultList.get(i).getBufen().getValue());
		}
	}
}
