package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

public class WenzhouShuangkouPanPlayerResult {
	private String playerId;
	private boolean chaodi;
	private WenzhouShuangkouMingcifen mingcifen;
	private WenzhouShuangkouXianshuBeishu xianshubeishu;
	private WenzhouShuangkouGongxianFen gongxianfen;
	private WenzhouShuangkouChaixianbufen bufen;
	private int score;// 一盘结算分
	private int totalScore;// 总分

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean isChaodi() {
		return chaodi;
	}

	public void setChaodi(boolean chaodi) {
		this.chaodi = chaodi;
	}

	public WenzhouShuangkouMingcifen getMingcifen() {
		return mingcifen;
	}

	public void setMingcifen(WenzhouShuangkouMingcifen mingcifen) {
		this.mingcifen = mingcifen;
	}

	public WenzhouShuangkouXianshuBeishu getXianshubeishu() {
		return xianshubeishu;
	}

	public void setXianshubeishu(WenzhouShuangkouXianshuBeishu xianshubeishu) {
		this.xianshubeishu = xianshubeishu;
	}

	public WenzhouShuangkouGongxianFen getGongxianfen() {
		return gongxianfen;
	}

	public void setGongxianfen(WenzhouShuangkouGongxianFen gongxianfen) {
		this.gongxianfen = gongxianfen;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public WenzhouShuangkouChaixianbufen getBufen() {
		return bufen;
	}

	public void setBufen(WenzhouShuangkouChaixianbufen bufen) {
		this.bufen = bufen;
	}

}
