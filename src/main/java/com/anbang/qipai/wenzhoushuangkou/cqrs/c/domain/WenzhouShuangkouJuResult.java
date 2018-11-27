package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.List;

import com.dml.shuangkou.ju.JuResult;

public class WenzhouShuangkouJuResult implements JuResult {
	private int finishedPanCount;

	private List<WenzhouShuangkouJuPlayerResult> playerResultList;

	private String dayingjiaId;

	private String datuhaoId;

	public int getFinishedPanCount() {
		return finishedPanCount;
	}

	public void setFinishedPanCount(int finishedPanCount) {
		this.finishedPanCount = finishedPanCount;
	}

	public List<WenzhouShuangkouJuPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<WenzhouShuangkouJuPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public String getDayingjiaId() {
		return dayingjiaId;
	}

	public void setDayingjiaId(String dayingjiaId) {
		this.dayingjiaId = dayingjiaId;
	}

	public String getDatuhaoId() {
		return datuhaoId;
	}

	public void setDatuhaoId(String datuhaoId) {
		this.datuhaoId = datuhaoId;
	}

}
