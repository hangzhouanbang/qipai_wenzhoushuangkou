package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.result;

import java.util.List;

import com.dml.shuangkou.pan.PanResult;

public class WenzhouShuangkouPanResult extends PanResult {
	private boolean chaodi;
	private List<WenzhouShuangkouPanPlayerResult> panPlayerResultList;

	public boolean isChaodi() {
		return chaodi;
	}

	public void setChaodi(boolean chaodi) {
		this.chaodi = chaodi;
	}

	public List<WenzhouShuangkouPanPlayerResult> getPanPlayerResultList() {
		return panPlayerResultList;
	}

	public void setPanPlayerResultList(List<WenzhouShuangkouPanPlayerResult> panPlayerResultList) {
		this.panPlayerResultList = panPlayerResultList;
	}

}
