package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouGongxianFen;

public class PukeGamePlayerInfoDbo {
	private String playerId;
	private int gongxianfen;
	private int maxXianshu;
	private int otherMaxXianshu;
	private WenzhouShuangkouGongxianFen totalGongxianfen;
	private boolean nopai;
	private int mingci;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getGongxianfen() {
		return gongxianfen;
	}

	public void setGongxianfen(int gongxianfen) {
		this.gongxianfen = gongxianfen;
	}

	public int getMaxXianshu() {
		return maxXianshu;
	}

	public void setMaxXianshu(int maxXianshu) {
		this.maxXianshu = maxXianshu;
	}

	public int getOtherMaxXianshu() {
		return otherMaxXianshu;
	}

	public void setOtherMaxXianshu(int otherMaxXianshu) {
		this.otherMaxXianshu = otherMaxXianshu;
	}

	public WenzhouShuangkouGongxianFen getTotalGongxianfen() {
		return totalGongxianfen;
	}

	public void setTotalGongxianfen(WenzhouShuangkouGongxianFen totalGongxianfen) {
		this.totalGongxianfen = totalGongxianfen;
	}

	public boolean isNopai() {
		return nopai;
	}

	public void setNopai(boolean nopai) {
		this.nopai = nopai;
	}

	public int getMingci() {
		return mingci;
	}

	public void setMingci(int mingci) {
		this.mingci = mingci;
	}

}
