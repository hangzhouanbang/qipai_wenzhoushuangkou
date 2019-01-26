package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.init.XianshuCalculatorHelper;

/**
 * 贡献分
 * 
 * @author lsc
 *
 */
public class WenzhouShuangkouGongxianFen {
	private int sixian;
	private int wuxian;
	private int liuxian;
	private int qixian;
	private int baxian;
	private int jiuxian;
	private int shixian;
	private int shiyixian;
	private int shierxian;
	private int totalscore;// 总分
	private int value;// 单人结算分

	public WenzhouShuangkouGongxianFen() {

	}

	public WenzhouShuangkouGongxianFen(int[] xianshuCount) {
		if (xianshuCount == null) {
			xianshuCount = new int[9];
		}
		sixian = xianshuCount[0];
		wuxian = xianshuCount[1];
		liuxian = xianshuCount[2];
		qixian = xianshuCount[3];
		baxian = xianshuCount[4];
		jiuxian = xianshuCount[5];
		shixian = xianshuCount[6];
		shiyixian = xianshuCount[7];
		shierxian = xianshuCount[8];
	}

	public void calculateShouPaiXianshu(List<int[]> xianshuList, boolean fengding) {
		if (xianshuList == null || xianshuList.isEmpty()) {
			String key = "" + sixian + wuxian + liuxian + qixian + baxian + jiuxian + shixian + shiyixian + shierxian;
			Integer score = XianshuCalculatorHelper.getGongxianFenCountMap().get(key);
			if (score != null) {
				value = score;
			}
		} else {
			int bestScore = 0;
			int[] bestXianShuCount = new int[9];
			for (int[] xianshuCount : xianshuList) {
				sixian += xianshuCount[0];
				wuxian += xianshuCount[1];
				liuxian += xianshuCount[2];
				qixian += xianshuCount[3];
				baxian += xianshuCount[4];
				jiuxian += xianshuCount[5];
				shixian += xianshuCount[6];
				shiyixian += xianshuCount[7];
				shierxian += xianshuCount[8];
				String key = "" + sixian + wuxian + liuxian + qixian + baxian + jiuxian + shixian + shiyixian
						+ shierxian;
				Integer score = XianshuCalculatorHelper.getGongxianFenCountMap().get(key);
				if (score != null && score > bestScore) {
					bestScore = score;
					bestXianShuCount = xianshuCount;
				}
				sixian -= xianshuCount[0];
				wuxian -= xianshuCount[1];
				liuxian -= xianshuCount[2];
				qixian -= xianshuCount[3];
				baxian -= xianshuCount[4];
				jiuxian -= xianshuCount[5];
				shixian -= xianshuCount[6];
				shiyixian -= xianshuCount[7];
				shierxian -= xianshuCount[8];

			}
			sixian += bestXianShuCount[0];
			wuxian += bestXianShuCount[1];
			liuxian += bestXianShuCount[2];
			qixian += bestXianShuCount[3];
			baxian += bestXianShuCount[4];
			jiuxian += bestXianShuCount[5];
			shixian += bestXianShuCount[6];
			shiyixian += bestXianShuCount[7];
			shierxian += bestXianShuCount[8];
			value = bestScore;
		}
	}

	public void calculateXianshu(boolean fengding) {
		String key = "" + sixian + wuxian + liuxian + qixian + baxian + jiuxian + shixian + shiyixian + shierxian;
		Integer score = XianshuCalculatorHelper.getGongxianFenCountMap().get(key);
		if (score != null) {
			value = score;
		}
	}

	public void calculate(int renshu) {
		totalscore = value * (renshu - 1);
	}

	public int jiesuan(int delta) {
		return totalscore += delta;
	}

	public int getSixian() {
		return sixian;
	}

	public void setSixian(int sixian) {
		this.sixian = sixian;
	}

	public int getWuxian() {
		return wuxian;
	}

	public void setWuxian(int wuxian) {
		this.wuxian = wuxian;
	}

	public int getLiuxian() {
		return liuxian;
	}

	public void setLiuxian(int liuxian) {
		this.liuxian = liuxian;
	}

	public int getQixian() {
		return qixian;
	}

	public void setQixian(int qixian) {
		this.qixian = qixian;
	}

	public int getBaxian() {
		return baxian;
	}

	public void setBaxian(int baxian) {
		this.baxian = baxian;
	}

	public int getJiuxian() {
		return jiuxian;
	}

	public void setJiuxian(int jiuxian) {
		this.jiuxian = jiuxian;
	}

	public int getShixian() {
		return shixian;
	}

	public void setShixian(int shixian) {
		this.shixian = shixian;
	}

	public int getShiyixian() {
		return shiyixian;
	}

	public void setShiyixian(int shiyixian) {
		this.shiyixian = shiyixian;
	}

	public int getShierxian() {
		return shierxian;
	}

	public void setShierxian(int shierxian) {
		this.shierxian = shierxian;
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
