package com.anbang.qipai.wenzhoushuangkou.web.vo;

import com.dml.shuangkou.pan.PanActionFrame;
import com.dml.shuangkou.player.action.ShuangkouPlayerAction;

public class PanActionFrameVO {
	private ShuangkouPlayerAction action;
	private PanValueObjectVO panAfterAction;
	private long actionTime;

	public PanActionFrameVO() {

	}

	public PanActionFrameVO(PanActionFrame panActionFrame) {
		action = panActionFrame.getAction();
		panAfterAction = new PanValueObjectVO(panActionFrame.getPanAfterAction());
		actionTime = panActionFrame.getActionTime();
	}

	public ShuangkouPlayerAction getAction() {
		return action;
	}

	public void setAction(ShuangkouPlayerAction action) {
		this.action = action;
	}

	public PanValueObjectVO getPanAfterAction() {
		return panAfterAction;
	}

	public void setPanAfterAction(PanValueObjectVO panAfterAction) {
		this.panAfterAction = panAfterAction;
	}

	public long getActionTime() {
		return actionTime;
	}

	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

}
