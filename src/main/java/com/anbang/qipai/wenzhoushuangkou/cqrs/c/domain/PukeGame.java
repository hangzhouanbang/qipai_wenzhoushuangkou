package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.preparedapai.luanpai.BianXingWanFa;

public class PukeGame extends FixedPlayersMultipanAndVotetofinishGame {
	private int panshu;
	private int renshu;
	private BianXingWanFa bx;
	private boolean chaodi;
	private boolean shuangming;
	private boolean fengding;
	private ChaPai chapai;
	private FaPai fapai;
	private Ju ju;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();

	@Override
	protected boolean checkToFinishGame() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void startNextPan() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToExtendedVotingState() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToVoteNotPassStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends GameValueObject> T toValueObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(long currentTime) throws Exception {
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	public void finish() throws Exception {
		// TODO Auto-generated method stub

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

	public Ju getJu() {
		return ju;
	}

	public void setJu(Ju ju) {
		this.ju = ju;
	}

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

}
