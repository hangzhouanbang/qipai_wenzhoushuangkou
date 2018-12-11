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

		value = countSiXian(xianshuAmountArray);
	}

	public void calculate(int renshu) {
		totalscore = value * (renshu - 1);
	}

	public int jiesuan(int delta) {
		return totalscore += delta;
	}

	private int countSiXian(int[] xianshuAmountArray) {
		int bestScore = 0;
		if (xianshuAmountArray[0] >= 7) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[5] += 1;
			xianshuAmountArray[0] -= 7;
			int score = countWuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[0] >= 6) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[4] += 1;
			xianshuAmountArray[0] -= 6;
			int score = countWuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[0] >= 5) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[3] += 1;
			xianshuAmountArray[0] -= 5;
			int score = countWuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[0] >= 4) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[2] += 1;
			xianshuAmountArray[0] -= 4;
			int score = countWuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[0] >= 3) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[1] += 1;
			xianshuAmountArray[0] -= 3;
			int score = countWuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		return bestScore;
	}

	private int countWuXian(int[] xianshuAmountArray) {
		int bestScore = 0;
		if (xianshuAmountArray[1] >= 5) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[5] += 1;
			xianshuAmountArray[1] -= 5;
			int score = countLiuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[1] >= 4) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[4] += 1;
			xianshuAmountArray[1] -= 4;
			int score = countLiuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[1] >= 3) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[3] += 1;
			xianshuAmountArray[1] -= 3;
			int score = countLiuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[1] >= 2) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[2] += 1;
			xianshuAmountArray[1] -= 2;
			int score = countLiuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		return bestScore;
	}

	private int countLiuXian(int[] xianshuAmountArray) {
		int bestScore = 0;
		if (xianshuAmountArray[2] >= 4) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[5] += 1;
			xianshuAmountArray[2] -= 4;
			int score = countQiXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[2] >= 3) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[4] += 1;
			xianshuAmountArray[2] -= 3;
			int score = countQiXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[2] >= 2) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[3] += 1;
			xianshuAmountArray[2] -= 2;
			int score = countQiXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		return bestScore;
	}

	private int countQiXian(int[] xianshuAmountArray) {
		int bestScore = 0;
		if (xianshuAmountArray[3] >= 4) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[6] += 1;
			xianshuAmountArray[3] -= 4;
			int score = countBaXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[3] >= 3) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[5] += 1;
			xianshuAmountArray[3] -= 3;
			int score = countBaXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[3] >= 2) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[4] += 1;
			xianshuAmountArray[3] -= 2;
			int score = countBaXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		return bestScore;
	}

	private int countBaXian(int[] xianshuAmountArray) {
		int bestScore = 0;
		if (xianshuAmountArray[4] >= 3) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[6] += 1;
			xianshuAmountArray[4] -= 3;
			int score = countJiuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		if (xianshuAmountArray[4] >= 2) {
			int[] copyArray = xianshuAmountArray.clone();
			xianshuAmountArray[5] += 1;
			xianshuAmountArray[4] -= 2;
			int score = countJiuXian(copyArray);
			if (score > bestScore) {
				bestScore = score;
			}
		}
		return bestScore;
	}

	private int countJiuXian(int[] xianshuAmountArray) {
		xianshuAmountArray[6] += 1;
		xianshuAmountArray[5] -= 2;
		return countShiXian(xianshuAmountArray);
	}

	private int countShiXian(int[] xianshuAmountArray) {
		xianshuAmountArray[7] += 1;
		xianshuAmountArray[6] -= 2;
		return calculateBestScore(xianshuAmountArray);
	}

	public int calculateBestScore(int[] xianshuAmountArray) {
		int score = 4 * xianshuAmountArray[2] + 8 * xianshuAmountArray[3] + 16 * xianshuAmountArray[4]
				+ 32 * xianshuAmountArray[5] + 64 * xianshuAmountArray[6] + 128 * xianshuAmountArray[7]
				+ 256 * xianshuAmountArray[8];
		return score;
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
