package com.anbang.qipai.wenzhoushuangkou.web.vo;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGamePlayerDbo;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerPanFinishedAndVoted;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerPanFinishedAndVoting;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerReadyToStartNextPanAndVoted;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerReadyToStartNextPanAndVoting;
import com.dml.mpgame.game.extend.multipan.player.PlayerPanFinished;
import com.dml.mpgame.game.extend.multipan.player.PlayerReadyToStartNextPan;
import com.dml.mpgame.game.extend.vote.player.PlayerPlayingAndVoted;
import com.dml.mpgame.game.extend.vote.player.PlayerPlayingAndVoting;
import com.dml.mpgame.game.player.PlayerFinished;
import com.dml.mpgame.game.player.PlayerJoined;
import com.dml.mpgame.game.player.PlayerPlaying;
import com.dml.mpgame.game.player.PlayerReadyToStart;

public class PukeGamePlayerVO {
	private String playerId;
	private String nickname;
	private String gender;// 会员性别:男:male,女:female
	private String headimgurl;
	private String state;
	private String onlineState;
	private int totalScore;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public PukeGamePlayerVO(PukeGamePlayerDbo dbo) {
		playerId = dbo.getPlayerId();
		nickname = dbo.getNickname();
		gender = dbo.getGender();
		headimgurl = dbo.getHeadimgurl();
		onlineState = dbo.getOnlineState().name();
		totalScore = dbo.getTotalScore();
		String sn = dbo.getState().name();
		if (sn.equals(PlayerFinished.name)) {
			state = "finished";
		} else if (sn.equals(PlayerJoined.name)) {
			state = "joined";
		} else if (sn.equals(PlayerPanFinished.name)) {
			state = "panFinished";
		} else if (sn.equals(PlayerPlaying.name)) {
			state = "playing";
		} else if (sn.equals(PlayerReadyToStart.name)) {
			state = "readyToStart";
		} else if (sn.equals(PlayerReadyToStartNextPan.name)) {
			state = "readyToStart";
		} else if (sn.equals(PlayerPlayingAndVoted.name)) {
			state = "playing";
		} else if (sn.equals(PlayerPlayingAndVoting.name)) {
			state = sn;
		} else if (sn.equals(PlayerPanFinishedAndVoted.name)) {
			state = sn;
		} else if (sn.equals(PlayerPanFinishedAndVoting.name)) {
			state = sn;
		} else if (sn.equals(PlayerReadyToStartNextPanAndVoted.name)) {
			state = sn;
		} else if (sn.equals(PlayerReadyToStartNextPanAndVoting.name)) {
			state = sn;
		} else {
		}
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
