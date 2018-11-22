package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;

/**
 * 王炸，包括三王炸和天王炸
 * 
 * @author lsc
 *
 */
public class WangZhadanDianShuZu extends ZhadanDianShuZu {
	private DianShu[] dianShuZu = { DianShu.xiaowang, DianShu.dawang };
	private int xiaowangCount;
	private int dawangCount;

	public WangZhadanDianShuZu() {
	}

	public WangZhadanDianShuZu(int xiaowangCount, int dawangCount) {
		this.xiaowangCount = xiaowangCount;
		this.dawangCount = dawangCount;
	}

	public DianShu[] getDianShuZu() {
		return dianShuZu;
	}

	public void setDianShuZu(DianShu[] dianShuZu) {
		this.dianShuZu = dianShuZu;
	}

	public int getXiaowangCount() {
		return xiaowangCount;
	}

	public void setXiaowangCount(int xiaowangCount) {
		this.xiaowangCount = xiaowangCount;
	}

	public int getDawangCount() {
		return dawangCount;
	}

	public void setDawangCount(int dawangCount) {
		this.dawangCount = dawangCount;
	}

}
