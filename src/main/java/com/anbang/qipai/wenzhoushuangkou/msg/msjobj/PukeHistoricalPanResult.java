package com.anbang.qipai.wenzhoushuangkou.msg.msjobj;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.WenzhouShuangkouPanPlayerResultDbo;

public class PukeHistoricalPanResult {
	private List<WenzhouShuangkouPanPlayerResultMO> playerResultList;

	private boolean chaodi;

	private int panNo;

	private long finishTime;

	public PukeHistoricalPanResult() {

	}

	public PukeHistoricalPanResult(PanResultDbo panResultDbo, PukeGameDbo pukeGameDbo) {
		List<WenzhouShuangkouPanPlayerResultDbo> list = panResultDbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList.add(new WenzhouShuangkouPanPlayerResultMO(
					pukeGameDbo.findPlayer(panPlayerResult.getPlayerId()), panPlayerResult)));
		}
		chaodi = panResultDbo.isChaodi();
		panNo = panResultDbo.getPanNo();
		finishTime = panResultDbo.getFinishTime();
	}

	public List<WenzhouShuangkouPanPlayerResultMO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<WenzhouShuangkouPanPlayerResultMO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public boolean isChaodi() {
		return chaodi;
	}

	public void setChaodi(boolean chaodi) {
		this.chaodi = chaodi;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
