package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

public class WenzhouShuangkouXianshuBeishu {
	private int sixian;
	private int wuxian;
	private int liuxian;
	private int qixian;
	private int baxian;
	private int jiuxian;
	private int shixian;
	private int shiyixian;
	private int shierxian;
	private int value;// 单人线数倍数

	public WenzhouShuangkouXianshuBeishu() {

	}

	public WenzhouShuangkouXianshuBeishu(int[] xianshuCount) {
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

	public void calculate() {
		int beishu = 1;
		if (shierxian > 0) {
			beishu = 256;
		} else if (shiyixian > 0 || shixian >= 2) {
			beishu = 128;
		} else if (shixian > 0 || jiuxian >= 2 || baxian >= 3 || qixian >= 4) {
			beishu = 64;
		} else if (jiuxian > 0 || baxian >= 2 || qixian >= 3 || liuxian >= 4 || wuxian >= 5 || sixian >= 7) {
			beishu = 32;
		} else if (baxian > 0 || qixian >= 2 || liuxian >= 3 || wuxian >= 4 || sixian >= 6) {
			beishu = 16;
		} else if (qixian > 0 || liuxian >= 2 || wuxian >= 3 || sixian >= 5) {
			beishu = 8;
		} else if (liuxian > 0 || wuxian >= 2 || sixian >= 4) {
			beishu = 4;
		} else if (wuxian > 0 || sixian >= 3) {
			beishu = 2;
		} else if (sixian > 0) {
			beishu = 1;
		}
		value = beishu;
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
