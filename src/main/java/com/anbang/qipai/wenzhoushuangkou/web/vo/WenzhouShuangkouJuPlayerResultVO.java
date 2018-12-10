package com.anbang.qipai.wenzhoushuangkou.web.vo;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result.WenzhouShuangkouJuPlayerResult;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGamePlayerDbo;

public class WenzhouShuangkouJuPlayerResultVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int shuangkouCount;
	private int dankouCount;
	private int pingkouCount;
	private int maxXianshu;
	private int totalScore;

	public WenzhouShuangkouJuPlayerResultVO(PukeGamePlayerDbo playerDbo) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		shuangkouCount = 0;
		dankouCount = 0;
		pingkouCount = 0;
		maxXianshu = 0;
		totalScore = 0;
	}

	public WenzhouShuangkouJuPlayerResultVO(WenzhouShuangkouJuPlayerResult juPlayerResult,
			PukeGamePlayerDbo playerDbo) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		shuangkouCount = juPlayerResult.getShuangkouCount();
		dankouCount = juPlayerResult.getDankouCount();
		pingkouCount = juPlayerResult.getPingkouCount();
		maxXianshu = juPlayerResult.getMaxXianshu();
		totalScore = juPlayerResult.getTotalScore();
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
