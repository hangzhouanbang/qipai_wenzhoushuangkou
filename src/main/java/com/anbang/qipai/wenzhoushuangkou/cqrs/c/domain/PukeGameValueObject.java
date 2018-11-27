package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;
import com.dml.shuangkou.BianXingWanFa;
import com.dml.shuangkou.ju.JuResult;

public class PukeGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {

	private int panshu;
	private int renshu;
	private BianXingWanFa bx;
	private boolean chaodi;
	private boolean shuangming;
	private boolean fengding;
	private ChaPai chapai;
	private FaPai fapai;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();
	private Map<String, Integer> playeGongxianfenMap = new HashMap<>();
	private Map<String, Integer> playeTotalGongxianfenMap = new HashMap<>();
	private Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap = new HashMap<>();
	private List<String> chaodiPlayerIdList = new ArrayList<>();
	private JuResult juResult;

	public PukeGameValueObject(PukeGame pukeGame) {
		super(pukeGame);
		panshu = pukeGame.getPanshu();
		renshu = pukeGame.getRenshu();
		bx = pukeGame.getBx();
		chaodi = pukeGame.isChaodi();
		shuangming = pukeGame.isShuangming();
		fengding = pukeGame.isFengding();
		chapai = pukeGame.getChapai();
		fapai = pukeGame.getFapai();
		playeTotalScoreMap.putAll(pukeGame.getPlayeTotalScoreMap());
		playeGongxianfenMap.putAll(pukeGame.getPlayeGongxianfenMap());
		playerChaodiStateMap.putAll(pukeGame.getPlayerChaodiStateMap());
		playeTotalGongxianfenMap.putAll(pukeGame.getPlayeTotalGongxianfenMap());
		chaodiPlayerIdList = new ArrayList<>(pukeGame.getChaodiPlayerIdList());
		if (pukeGame.getJu() != null) {
			juResult = pukeGame.getJu().getJuResult();
		}
	}

	public List<String> getChaodiPlayerIdList() {
		return chaodiPlayerIdList;
	}

	public void setChaodiPlayerIdList(List<String> chaodiPlayerIdList) {
		this.chaodiPlayerIdList = chaodiPlayerIdList;
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

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

	public Map<String, PukeGamePlayerChaodiState> getPlayerChaodiStateMap() {
		return playerChaodiStateMap;
	}

	public void setPlayerChaodiStateMap(Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap) {
		this.playerChaodiStateMap = playerChaodiStateMap;
	}

	public JuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(JuResult juResult) {
		this.juResult = juResult;
	}

	public Map<String, Integer> getPlayeGongxianfenMap() {
		return playeGongxianfenMap;
	}

	public void setPlayeGongxianfenMap(Map<String, Integer> playeGongxianfenMap) {
		this.playeGongxianfenMap = playeGongxianfenMap;
	}

	public Map<String, Integer> getPlayeTotalGongxianfenMap() {
		return playeTotalGongxianfenMap;
	}

	public void setPlayeTotalGongxianfenMap(Map<String, Integer> playeTotalGongxianfenMap) {
		this.playeTotalGongxianfenMap = playeTotalGongxianfenMap;
	}

}
