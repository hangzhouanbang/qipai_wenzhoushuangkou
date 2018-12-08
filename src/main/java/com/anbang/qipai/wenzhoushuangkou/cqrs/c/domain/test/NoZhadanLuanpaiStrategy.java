package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.List;

import com.dml.puke.pai.PukePai;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pan.Pan;
import com.dml.shuangkou.preparedapai.luanpai.LuanpaiStrategy;

/**
 * 没有炸弹的乱牌方法
 * 
 * @author lsc
 *
 */
public class NoZhadanLuanpaiStrategy implements LuanpaiStrategy {

	@Override
	public void luanpai(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		List<PukePai> allPaiList = currentPan.getAvaliablePaiList();
		// 最终的牌堆
		List<PukePai> finalPaiList = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 13; j++) {
				finalPaiList.add(allPaiList.get(j * 8 + i));
			}
		}
		finalPaiList.add(allPaiList.get(104));
		finalPaiList.add(allPaiList.get(105));
		finalPaiList.add(allPaiList.get(106));
		finalPaiList.add(allPaiList.get(107));
		currentPan.setAvaliablePaiList(finalPaiList);
	}

}
