package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.state.PukeGamePlayerChaodiState;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.wanfa.ChaPai;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.wanfa.FaPai;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;
import com.dml.shuangkou.ju.JuResult;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class PukeGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {

	private int panshu;
	private int renshu;
	private BianXingWanFa bx;
	private boolean chaodi;
	private boolean shuangming;
	private boolean bxfd;// 八线封顶
	private boolean jxfd;// 九线封顶
	private boolean sxfd;// 十线封顶
	private ChaPai chapai;
	private FaPai fapai;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();
	private Map<String, Integer> playeGongxianfenMap = new HashMap<>();
	private Map<String, WenzhouShuangkouGongxianFen> playeTotalGongxianfenMap = new HashMap<>();
	private Map<String, Integer> playerMaxXianshuMap = new HashMap<>();
	private Map<String, Integer> playerOtherMaxXianshuMap = new HashMap<>();
	private Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap = new HashMap<>();
	private Map<String, Integer> playerMingciMap = new HashMap<>();
	private List<String> chaodiPlayerIdList = new ArrayList<>();
	private JuResult juResult;

	public PukeGameValueObject(PukeGame pukeGame) {
		super(pukeGame);
		panshu = pukeGame.getPanshu();
		renshu = pukeGame.getRenshu();
		bx = pukeGame.getBx();
		chaodi = pukeGame.isChaodi();
		shuangming = pukeGame.isShuangming();
		bxfd = pukeGame.isBxfd();
		jxfd = pukeGame.isJxfd();
		sxfd = pukeGame.isSxfd();
		chapai = pukeGame.getChapai();
		fapai = pukeGame.getFapai();
		playeTotalScoreMap.putAll(pukeGame.getPlayerTotalScoreMap());
		playeGongxianfenMap.putAll(pukeGame.getPlayerGongxianfenMap());
		playerChaodiStateMap.putAll(pukeGame.getPlayerChaodiStateMap());
		playeTotalGongxianfenMap.putAll(pukeGame.getPlayerTotalGongxianfenMap());
		playerMaxXianshuMap.putAll(pukeGame.getPlayerMaxXianshuMap());
		playerOtherMaxXianshuMap.putAll(pukeGame.getPlayerOtherMaxXianshuMap());
		chaodiPlayerIdList = new ArrayList<>(pukeGame.getChaodiPlayerIdList());
		playerMingciMap.putAll(pukeGame.getPlayerMingciMap());
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

	public boolean isBxfd() {
		return bxfd;
	}

	public void setBxfd(boolean bxfd) {
		this.bxfd = bxfd;
	}

	public boolean isJxfd() {
		return jxfd;
	}

	public void setJxfd(boolean jxfd) {
		this.jxfd = jxfd;
	}

	public boolean isSxfd() {
		return sxfd;
	}

	public void setSxfd(boolean sxfd) {
		this.sxfd = sxfd;
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

	public Map<String, Integer> getPlayerOtherMaxXianshuMap() {
		return playerOtherMaxXianshuMap;
	}

	public void setPlayerOtherMaxXianshuMap(Map<String, Integer> playerOtherMaxXianshuMap) {
		this.playerOtherMaxXianshuMap = playerOtherMaxXianshuMap;
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

	public Map<String, WenzhouShuangkouGongxianFen> getPlayeTotalGongxianfenMap() {
		return playeTotalGongxianfenMap;
	}

	public void setPlayeTotalGongxianfenMap(Map<String, WenzhouShuangkouGongxianFen> playeTotalGongxianfenMap) {
		this.playeTotalGongxianfenMap = playeTotalGongxianfenMap;
	}

	public Map<String, Integer> getPlayerMaxXianshuMap() {
		return playerMaxXianshuMap;
	}

	public void setPlayerMaxXianshuMap(Map<String, Integer> playerMaxXianshuMap) {
		this.playerMaxXianshuMap = playerMaxXianshuMap;
	}

	public Map<String, Integer> getPlayerMingciMap() {
		return playerMingciMap;
	}

	public void setPlayerMingciMap(Map<String, Integer> playerMingciMap) {
		this.playerMingciMap = playerMingciMap;
	}

}
