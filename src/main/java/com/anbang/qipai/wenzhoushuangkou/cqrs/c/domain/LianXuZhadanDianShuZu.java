package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianXuDianShuZu;

/**
 * 连续炸弹点数组。如4444455556666666为4相3连炸弹
 * 
 * @author lsc
 *
 */
public class LianXuZhadanDianShuZu extends LianXuDianShuZu implements DianShuZu {
	private int[] lianXuDianShuSizeArray;

	public LianXuZhadanDianShuZu() {
	}

	public LianXuZhadanDianShuZu(DianShu[] lianXuDianShuArray, int[] lianXuDianShuSizeArray) {
		super(lianXuDianShuArray);
		this.lianXuDianShuSizeArray = lianXuDianShuSizeArray;
	}

	public int[] getLianXuDianShuSizeArray() {
		return lianXuDianShuSizeArray;
	}

	public void setLianXuDianShuSizeArray(int[] lianXuDianShuSizeArray) {
		this.lianXuDianShuSizeArray = lianXuDianShuSizeArray;
	}

}
