package com.anbang.qipai.wenzhoushuangkou.web.vo;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouMingcifen;

public class WenzhouShuangkouMingcifenVO {
	private boolean shuangkou;
	private boolean dankou;
	private boolean pingkou;

	public WenzhouShuangkouMingcifenVO() {

	}

	public WenzhouShuangkouMingcifenVO(WenzhouShuangkouMingcifen mingcifen) {
		if (mingcifen.isYing()) {
			if (mingcifen.isShuangkou()) {
				shuangkou = true;
			} else if (mingcifen.isDankou()) {
				dankou = true;
			} else if (mingcifen.isPingkou()) {
				pingkou = true;
			}
		}
	}

	public boolean isShuangkou() {
		return shuangkou;
	}

	public void setShuangkou(boolean shuangkou) {
		this.shuangkou = shuangkou;
	}

	public boolean isDankou() {
		return dankou;
	}

	public void setDankou(boolean dankou) {
		this.dankou = dankou;
	}

	public boolean isPingkou() {
		return pingkou;
	}

	public void setPingkou(boolean pingkou) {
		this.pingkou = pingkou;
	}

}
