package com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouPanResult;
import com.dml.shuangkou.pan.PanActionFrame;

public class PanResultDbo {
	private String id;
	private String gameId;
	private int panNo;
	private PanActionFrame panActionFrame;

	public PanResultDbo() {
	}

	public PanResultDbo(String gameId, WenzhouShuangkouPanResult wenzhouShuangkouPanResult) {
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

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

}
