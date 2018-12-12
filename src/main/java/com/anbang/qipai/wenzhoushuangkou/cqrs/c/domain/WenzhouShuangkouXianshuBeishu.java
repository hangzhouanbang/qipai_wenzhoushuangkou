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
	private int[] bestXianshuAmountArray = new int[9];
	private int[][] fenTable = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 4, 8, 16, 32, 64, 128, 256 },
			{ 0, 4, 8, 16, 32, 64, 128, 0, 0 }, { 0, 8, 16, 32, 64, 128, 0, 0, 0 }, { 4, 16, 32, 64, 0, 0, 0, 0, 0 },
			{ 8, 32, 0, 0, 0, 0, 0, 0, 0, }, { 16, 0, 0, 0, 0, 0, 0, 0, 0 }, { 32, 0, 0, 0, 0, 0, 0, 0, 0 } };

	public WenzhouShuangkouXianshuBeishu() {

	}

	public WenzhouShuangkouXianshuBeishu(int[] xianshuCount) {
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

	private void calculateBestXianshu(int[] xianshuAmountArray, int index, int size, int length) {
		if (length >= 8) {
			int score = fenTable[xianshuAmountArray[0]][0] + fenTable[xianshuAmountArray[1]][1]
					+ fenTable[xianshuAmountArray[2]][2] + fenTable[xianshuAmountArray[3]][3]
					+ fenTable[xianshuAmountArray[4]][4] + fenTable[xianshuAmountArray[5]][5]
					+ fenTable[xianshuAmountArray[6]][6] + fenTable[xianshuAmountArray[7]][7]
					+ fenTable[xianshuAmountArray[8]][8];
			if (score > value) {
				value = score;
				bestXianshuAmountArray = xianshuAmountArray;
			}
		} else {
			for (int i = 0; i <= 8 - index; i++) {
				for (int j = 0; j <= size; j++) {
					int[] copyArray = xianshuAmountArray.clone();
					copyArray[8 - index] -= j;
					copyArray[i] += j;
					calculateBestXianshu(copyArray, index + i, copyArray[8 - index - i], length + 1);
				}
			}
		}
	}

	public void calculateXianshu() {
		int[] xianshuAmountArray = new int[9];
		xianshuAmountArray[0] = sixian;
		xianshuAmountArray[1] = wuxian;
		xianshuAmountArray[2] = liuxian;
		xianshuAmountArray[3] = qixian;
		xianshuAmountArray[4] = baxian;
		xianshuAmountArray[5] = jiuxian;
		xianshuAmountArray[6] = shixian;
		xianshuAmountArray[7] = shiyixian;
		xianshuAmountArray[8] = shierxian;

		calculateBestXianshu(xianshuAmountArray.clone(), 0, xianshuAmountArray[8], 0);

		sixian = bestXianshuAmountArray[0];
		wuxian = bestXianshuAmountArray[1];
		liuxian = bestXianshuAmountArray[2];
		qixian = bestXianshuAmountArray[3];
		baxian = bestXianshuAmountArray[4];
		jiuxian = bestXianshuAmountArray[5];
		shixian = bestXianshuAmountArray[6];
		shiyixian = bestXianshuAmountArray[7];
		shierxian = bestXianshuAmountArray[8];
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

	public int[] getBestXianshuAmountArray() {
		return bestXianshuAmountArray;
	}

	public void setBestXianshuAmountArray(int[] bestXianshuAmountArray) {
		this.bestXianshuAmountArray = bestXianshuAmountArray;
	}

	public int[][] getFenTable() {
		return fenTable;
	}

	public void setFenTable(int[][] fenTable) {
		this.fenTable = fenTable;
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
