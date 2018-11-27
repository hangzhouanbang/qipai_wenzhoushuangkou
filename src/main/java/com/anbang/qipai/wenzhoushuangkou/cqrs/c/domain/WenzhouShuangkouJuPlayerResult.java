package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

public class WenzhouShuangkouJuPlayerResult {
	private String playerId;
	private int shuangkouCount;
	private int dankouCount;
	private int pingkouCount;
	private int maxXianshu;
	private int totalScore;

	public void increaseShuangkouCount() {
		shuangkouCount++;
	}

	public void increaseDankouCount() {
		dankouCount++;
	}

	public void increasePingkouCount() {
		pingkouCount++;
	}

	public void increaseTotalScore(int score) {
		totalScore += score;
	}

	public void tryAndUpdateMaxXianshu(int xianshu) {
		if (xianshu > maxXianshu) {
			maxXianshu = xianshu;
		}
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getShuangkouCount() {
		return shuangkouCount;
	}

	public void setShuangkouCount(int shuangkouCount) {
		this.shuangkouCount = shuangkouCount;
	}

	public int getDankouCount() {
		return dankouCount;
	}

	public void setDankouCount(int dankouCount) {
		this.dankouCount = dankouCount;
	}

	public int getPingkouCount() {
		return pingkouCount;
	}

	public void setPingkouCount(int pingkouCount) {
		this.pingkouCount = pingkouCount;
	}

	public int getMaxXianshu() {
		return maxXianshu;
	}

	public void setMaxXianshu(int maxXianshu) {
		this.maxXianshu = maxXianshu;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
