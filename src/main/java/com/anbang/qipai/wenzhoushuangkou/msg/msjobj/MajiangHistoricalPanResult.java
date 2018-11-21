package com.anbang.qipai.wenzhoushuangkou.msg.msjobj;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;

public class MajiangHistoricalPanResult {
	private String gameId;
	private int no;// 盘数
	private long finishTime;// 完成时间

	public MajiangHistoricalPanResult() {

	}

	public MajiangHistoricalPanResult(PanResultDbo panResultDbo, PukeGameDbo pukeGameDbo) {

	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
