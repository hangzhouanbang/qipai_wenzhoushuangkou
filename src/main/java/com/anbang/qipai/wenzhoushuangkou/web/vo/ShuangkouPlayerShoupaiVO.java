package com.anbang.qipai.wenzhoushuangkou.web.vo;

import java.util.List;

import com.dml.puke.pai.PukePai;

public class ShuangkouPlayerShoupaiVO {
	private List<PukePai> allShoupai;
	private int totalShoupai;

	public ShuangkouPlayerShoupaiVO() {

	}

	public ShuangkouPlayerShoupaiVO(List<PukePai> allShoupai, int totalShoupai) {
		this.allShoupai = allShoupai;
		this.totalShoupai = totalShoupai;
	}

	public List<PukePai> getAllShoupai() {
		return allShoupai;
	}

	public void setAllShoupai(List<PukePai> allShoupai) {
		this.allShoupai = allShoupai;
	}

	public int getTotalShoupai() {
		return totalShoupai;
	}

	public void setTotalShoupai(int totalShoupai) {
		this.totalShoupai = totalShoupai;
	}

}
