package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

/**
 * 贡献分
 * 
 * @author lsc
 *
 */
public class WenzhouShuangkouGongxianFen {
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
			xianshuCount = new int[8];
		}
		liuxian = xianshuCount[2];
		qixian = xianshuCount[3];
		baxian = xianshuCount[4];
		jiuxian = xianshuCount[5];
		shixian = xianshuCount[6];
		shiyixian = xianshuCount[7];
		shierxian = xianshuCount[8];
	}

	public void calculate(int renshu) {
		value = 4 * liuxian + 8 * qixian + 16 * baxian + 32 * jiuxian + 64 * shixian + 128 * shiyixian
				+ 256 * shierxian;
		totalscore = value * (renshu - 1);
	}

	public int jiesuan(int delta) {
		return totalscore += delta;
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
