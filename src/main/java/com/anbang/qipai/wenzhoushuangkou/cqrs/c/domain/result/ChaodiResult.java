package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.PukeGameValueObject;
import com.dml.shuangkou.pan.PanActionFrame;

public class ChaodiResult {
	private PukeGameValueObject pukeGame;
	private PanActionFrame panActionFrame;
	private WenzhouShuangkouPanResult panResult;
	private WenzhouShuangkouJuResult juResult;

	public PukeGameValueObject getPukeGame() {
		return pukeGame;
	}

	public void setPukeGame(PukeGameValueObject pukeGame) {
		this.pukeGame = pukeGame;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public WenzhouShuangkouPanResult getPanResult() {
		return panResult;
	}

	public void setPanResult(WenzhouShuangkouPanResult panResult) {
		this.panResult = panResult;
	}

	public WenzhouShuangkouJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(WenzhouShuangkouJuResult juResult) {
		this.juResult = juResult;
	}

}
