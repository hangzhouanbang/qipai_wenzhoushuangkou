package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import com.dml.shuangkou.pan.PanActionFrame;

public class PanActionFrameDbo {

	private String id;
	private String gameId;
	private int panNo;
	private int actionNo;
	private PanActionFrame panActionFrame;

	public PanActionFrameDbo() {

	}

	public PanActionFrameDbo(String gameId, int panNo, int actionNo) {
		this.gameId = gameId;
		this.panNo = panNo;
		this.actionNo = actionNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public int getActionNo() {
		return actionNo;
	}

	public void setActionNo(int actionNo) {
		this.actionNo = actionNo;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

}
