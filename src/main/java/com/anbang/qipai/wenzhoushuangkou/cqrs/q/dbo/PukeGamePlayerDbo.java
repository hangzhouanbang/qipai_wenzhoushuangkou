package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.GamePlayerState;

public class PukeGamePlayerDbo {
	private String playerId;
	private String nickname;
	private String gender;// 会员性别:男:male,女:female
	private String headimgurl;
	private GamePlayerState state;// 原来是 joined, readyToStart, playing, panFinished, finished
	private GamePlayerOnlineState onlineState;
	private int totalScore;
	private int gongxianfen;
	private int maxXianshu;
	private int otherMaxXianshu;
	private int totalGongxianfen;

	public int getOtherMaxXianshu() {
		return otherMaxXianshu;
	}

	public void setOtherMaxXianshu(int otherMaxXianshu) {
		this.otherMaxXianshu = otherMaxXianshu;
	}

	public int getMaxXianshu() {
		return maxXianshu;
	}

	public void setMaxXianshu(int maxXianshu) {
		this.maxXianshu = maxXianshu;
	}

	public int getTotalGongxianfen() {
		return totalGongxianfen;
	}

	public void setTotalGongxianfen(int totalGongxianfen) {
		this.totalGongxianfen = totalGongxianfen;
	}

	public int getGongxianfen() {
		return gongxianfen;
	}

	public void setGongxianfen(int gongxianfen) {
		this.gongxianfen = gongxianfen;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public GamePlayerState getState() {
		return state;
	}

	public void setState(GamePlayerState state) {
		this.state = state;
	}

	public GamePlayerOnlineState getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(GamePlayerOnlineState onlineState) {
		this.onlineState = onlineState;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
