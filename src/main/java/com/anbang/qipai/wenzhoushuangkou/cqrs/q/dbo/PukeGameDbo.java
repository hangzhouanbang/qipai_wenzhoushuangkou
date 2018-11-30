package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.ChaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.FaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.wenzhoushuangkou.plan.bean.PlayerInfo;
import com.dml.mpgame.game.GamePlayerValueObject;
import com.dml.mpgame.game.GameState;
import com.dml.shuangkou.BianXingWanFa;

public class PukeGameDbo {
	private String id;
	private int panshu;
	private int renshu;
	private BianXingWanFa bx;
	private boolean chaodi;
	private boolean shuangming;
	private boolean fengding;
	private ChaPai chapai;
	private FaPai fapai;
	private GameState state;// 原来是 waitingStart, playing, waitingNextPan, finished
	private int panNo;
	private List<PukeGamePlayerDbo> players;
	private List<String> chaodiPlayerIdList;

	public PukeGameDbo() {
	}

	public PukeGameDbo(PukeGameValueObject pukeGame, Map<String, PlayerInfo> playerInfoMap) {
		id = pukeGame.getId();
		panshu = pukeGame.getPanshu();
		renshu = pukeGame.getRenshu();
		bx = pukeGame.getBx();
		chaodi = pukeGame.isChaodi();
		shuangming = pukeGame.isShuangming();
		fengding = pukeGame.isFengding();
		chapai = pukeGame.getChapai();
		fapai = pukeGame.getFapai();
		state = pukeGame.getState();
		panNo = pukeGame.getPanNo();
		chaodiPlayerIdList = new ArrayList<>(pukeGame.getChaodiPlayerIdList());
		players = new ArrayList<>();
		Map<String, Integer> playeTotalScoreMap = pukeGame.getPlayeTotalScoreMap();
		Map<String, Integer> playerMaxXianshuMap = pukeGame.getPlayerMaxXianshuMap();
		Map<String, Integer> playerOtherMaxXianshuMap = pukeGame.getPlayerOtherMaxXianshuMap();
		Map<String, Integer> playeGongxianfenMap = pukeGame.getPlayeGongxianfenMap();
		Map<String, Integer> playeTotalGongxianfenMap = pukeGame.getPlayeTotalGongxianfenMap();
		for (GamePlayerValueObject playerValueObject : pukeGame.getPlayers()) {
			String playerId = playerValueObject.getId();
			PlayerInfo playerInfo = playerInfoMap.get(playerId);
			PukeGamePlayerDbo playerDbo = new PukeGamePlayerDbo();
			playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
			playerDbo.setNickname(playerInfo.getNickname());
			playerDbo.setGender(playerInfo.getGender());
			playerDbo.setOnlineState(playerValueObject.getOnlineState());
			playerDbo.setPlayerId(playerId);
			playerDbo.setState(playerValueObject.getState());
			if (playeTotalScoreMap.get(playerId) != null) {
				playerDbo.setTotalScore(playeTotalScoreMap.get(playerId));
			}
			if (playeGongxianfenMap.get(playerId) != null) {
				playerDbo.setGongxianfen(playeGongxianfenMap.get(playerId));
			}
			if (playeTotalGongxianfenMap.get(playerId) != null) {
				playerDbo.setTotalGongxianfen(playeTotalGongxianfenMap.get(playerId));
			}
			if (playerMaxXianshuMap.get(playerId) != null) {
				playerDbo.setMaxXianshu(playerMaxXianshuMap.get(playerId));
			}
			if (playerOtherMaxXianshuMap.get(playerId) != null) {
				playerDbo.setOtherMaxXianshu(playerOtherMaxXianshuMap.get(playerId));
			}
			players.add(playerDbo);
		}

	}

	public PukeGamePlayerDbo findPlayer(String playerId) {
		for (PukeGamePlayerDbo player : players) {
			if (player.getPlayerId().equals(playerId)) {
				return player;
			}
		}
		return null;
	}

	public List<String> getChaodiPlayerIdList() {
		return chaodiPlayerIdList;
	}

	public void setChaodiPlayerIdList(List<String> chaodiPlayerIdList) {
		this.chaodiPlayerIdList = chaodiPlayerIdList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public boolean isChaodi() {
		return chaodi;
	}

	public void setChaodi(boolean chaodi) {
		this.chaodi = chaodi;
	}

	public boolean isShuangming() {
		return shuangming;
	}

	public void setShuangming(boolean shuangming) {
		this.shuangming = shuangming;
	}

	public boolean isFengding() {
		return fengding;
	}

	public void setFengding(boolean fengding) {
		this.fengding = fengding;
	}

	public ChaPai getChapai() {
		return chapai;
	}

	public void setChapai(ChaPai chapai) {
		this.chapai = chapai;
	}

	public FaPai getFapai() {
		return fapai;
	}

	public void setFapai(FaPai fapai) {
		this.fapai = fapai;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public List<PukeGamePlayerDbo> getPlayers() {
		return players;
	}

	public void setPlayers(List<PukeGamePlayerDbo> players) {
		this.players = players;
	}

}
