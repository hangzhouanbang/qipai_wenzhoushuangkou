package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

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

	public void calculateShouPaiXianshu(int[] xianshuCount) {
		if (xianshuCount == null) {
			xianshuCount = new int[9];
		}
		sixian += xianshuCount[0];
		wuxian += xianshuCount[1];
		liuxian += xianshuCount[2];
		qixian += xianshuCount[3];
		baxian += xianshuCount[4];
		jiuxian += xianshuCount[5];
		shixian += xianshuCount[6];
		shiyixian += xianshuCount[7];
		shierxian += xianshuCount[8];
	}

	public void calculateXianshu() {
		if (shixian >= 2) {
			int shang = shixian / 2;
			shiyixian += shang;
			int yu = shixian % 2;
			shixian = yu;
		}
		if (jiuxian >= 2 || baxian >= 3 || qixian >= 4) {
			int shangjiu = jiuxian / 2;
			shixian += shangjiu;
			int yujiu = jiuxian % 2;
			jiuxian = yujiu;

			int shangba = baxian / 3;
			shixian += shangba;
			int yuba = baxian % 3;
			baxian = yuba;

			int shangqi = qixian / 4;
			shixian += shangqi;
			int yuqi = qixian % 4;
			qixian = yuqi;
		}
		if (baxian >= 2 || qixian >= 3 || liuxian >= 4 || wuxian >= 5 || sixian >= 7) {
			int shangba = baxian / 2;
			jiuxian += shangba;
			int yuba = baxian % 2;
			baxian = yuba;

			int shangqi = qixian / 3;
			jiuxian += shangqi;
			int yuqi = qixian % 3;
			qixian = yuqi;

			int shangliu = liuxian / 4;
			jiuxian += shangliu;
			int yuliu = liuxian % 4;
			liuxian = yuliu;

			int shangwu = wuxian / 5;
			jiuxian += shangwu;
			int yuwu = wuxian % 5;
			wuxian = yuwu;

			int shangsi = sixian / 7;
			jiuxian += shangsi;
			int yusi = sixian % 7;
			sixian = yusi;
		}
		if (qixian >= 2 || liuxian >= 3 || wuxian >= 4 || sixian >= 6) {

			int shangqi = qixian / 2;
			baxian += shangqi;
			int yuqi = qixian % 2;
			qixian = yuqi;

			int shangliu = liuxian / 3;
			baxian += shangliu;
			int yuliu = liuxian % 3;
			liuxian = yuliu;

			int shangwu = wuxian / 4;
			baxian += shangwu;
			int yuwu = wuxian % 4;
			wuxian = yuwu;

			int shangsi = sixian / 6;
			baxian += shangsi;
			int yusi = sixian % 6;
			sixian = yusi;
		}
		if (liuxian >= 2 || wuxian >= 3 || sixian >= 5) {
			int shangliu = liuxian / 2;
			qixian += shangliu;
			int yuliu = liuxian % 2;
			liuxian = yuliu;

			int shangwu = wuxian / 3;
			qixian += shangwu;
			int yuwu = wuxian % 3;
			wuxian = yuwu;

			int shangsi = sixian / 5;
			qixian += shangsi;
			int yusi = sixian % 5;
			sixian = yusi;
		}
		if (wuxian >= 2 || sixian >= 4) {
			int shangwu = wuxian / 2;
			liuxian += shangwu;
			int yuwu = wuxian % 2;
			wuxian = yuwu;

			int shangsi = sixian / 4;
			liuxian += shangsi;
			int yusi = sixian % 4;
			sixian = yusi;
		}
		if (sixian >= 3) {
			int shangsi = sixian / 3;
			wuxian += shangsi;
			int yusi = baxian % 3;
			sixian = yusi;
		}
	}

	public void calculate(int renshu) {
		value = 4 * liuxian + 8 * qixian + 16 * baxian + 32 * jiuxian + 64 * shixian + 128 * shiyixian
				+ 256 * shierxian;
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
