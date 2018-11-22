package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;

/**
 * 连续炸弹点数组。如4444455556666666为4相3连炸弹
 * 
 * @author lsc
 *
 */
public class LianXuZhadanDianShuZu extends ZhadanDianShuZu {
	private DianShu[] lianXuDianShuArray;
	private int[] lianXuDianShuSizeArray;

	public LianXuZhadanDianShuZu() {
	}

	public LianXuZhadanDianShuZu(DianShu[] lianXuDianShuArray, int[] lianXuDianShuSizeArray) {
		this.lianXuDianShuArray = lianXuDianShuArray;
		this.lianXuDianShuSizeArray = lianXuDianShuSizeArray;
	}

	public int getMinXianShu() {
		int xianshu = lianXuDianShuSizeArray[0];
		for (int i = 1; i < lianXuDianShuSizeArray.length; i++) {
			if (xianshu > lianXuDianShuSizeArray[i]) {
				xianshu = lianXuDianShuSizeArray[i];
			}
		}
		return xianshu;
	}

	public int getLastOrdinal() {
		int firstIndex = lianXuDianShuArray[0].ordinal();
		int lastIndex = lianXuDianShuArray[lianXuDianShuArray.length - 1].ordinal();
		if (lastIndex - firstIndex > 6) {// 有2和3，即重新开始循环
			return (firstIndex + lianXuDianShuArray.length - 1) % 13;
		} else {
			return lastIndex;
		}
	}

	public DianShu[] getLianXuDianShuArray() {
		return lianXuDianShuArray;
	}

	public void setLianXuDianShuArray(DianShu[] lianXuDianShuArray) {
		this.lianXuDianShuArray = lianXuDianShuArray;
	}

	public int[] getLianXuDianShuSizeArray() {
		return lianXuDianShuSizeArray;
	}

	public void setLianXuDianShuSizeArray(int[] lianXuDianShuSizeArray) {
		this.lianXuDianShuSizeArray = lianXuDianShuSizeArray;
	}

}
