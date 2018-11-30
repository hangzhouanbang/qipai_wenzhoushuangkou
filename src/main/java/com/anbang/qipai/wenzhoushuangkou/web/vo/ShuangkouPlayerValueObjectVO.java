package com.anbang.qipai.wenzhoushuangkou.web.vo;

import java.util.List;

import com.dml.puke.pai.PukePai;
import com.dml.puke.wanfa.dianshu.paizu.DianShuZuPaiZu;
import com.dml.puke.wanfa.position.Position;
import com.dml.shuangkou.player.ShuangkouPlayerValueObject;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;

public class ShuangkouPlayerValueObjectVO {
	private String id;
	private Position position;
	private boolean couldChaodi;
	private List<PukePai> allShoupai;
	private int[] shoupaiDianShuAmountArray;
	private List<List<Integer>> shoupaiIdListForSortList;
	private List<DianShuZuPaiZu> lishiDachuPaiZuList;
	private DianShuZuPaiZu publicDachuPaiZu;
	// private List<DaPaiDianShuSolution> daPaiSolutionsForTips;
	private List<DaPaiDianShuSolution> yaPaiSolutionCandidates;
	private List<DaPaiDianShuSolution> yaPaiSolutionsForTips;

	public ShuangkouPlayerValueObjectVO() {

	}

	public ShuangkouPlayerValueObjectVO(ShuangkouPlayerValueObject shuangkouPlayerValueObject) {
		id = shuangkouPlayerValueObject.getId();
		position = shuangkouPlayerValueObject.getPosition();
		couldChaodi = shuangkouPlayerValueObject.isCouldChaodi();
		allShoupai = shuangkouPlayerValueObject.getAllShoupai();
		shoupaiDianShuAmountArray = shuangkouPlayerValueObject.getShoupaiDianShuAmountArray();
		shoupaiIdListForSortList = shuangkouPlayerValueObject.getShoupaiIdListForSortList();
		lishiDachuPaiZuList = shuangkouPlayerValueObject.getLishiDachuPaiZuList();
		publicDachuPaiZu = shuangkouPlayerValueObject.getPublicDachuPaiZu();
		yaPaiSolutionCandidates = shuangkouPlayerValueObject.getYaPaiSolutionCandidates();
		yaPaiSolutionsForTips = shuangkouPlayerValueObject.getYaPaiSolutionCandidates();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean isCouldChaodi() {
		return couldChaodi;
	}

	public void setCouldChaodi(boolean couldChaodi) {
		this.couldChaodi = couldChaodi;
	}

	public List<PukePai> getAllShoupai() {
		return allShoupai;
	}

	public void setAllShoupai(List<PukePai> allShoupai) {
		this.allShoupai = allShoupai;
	}

	public int[] getShoupaiDianShuAmountArray() {
		return shoupaiDianShuAmountArray;
	}

	public void setShoupaiDianShuAmountArray(int[] shoupaiDianShuAmountArray) {
		this.shoupaiDianShuAmountArray = shoupaiDianShuAmountArray;
	}

	public List<List<Integer>> getShoupaiIdListForSortList() {
		return shoupaiIdListForSortList;
	}

	public void setShoupaiIdListForSortList(List<List<Integer>> shoupaiIdListForSortList) {
		this.shoupaiIdListForSortList = shoupaiIdListForSortList;
	}

	public List<DianShuZuPaiZu> getLishiDachuPaiZuList() {
		return lishiDachuPaiZuList;
	}

	public void setLishiDachuPaiZuList(List<DianShuZuPaiZu> lishiDachuPaiZuList) {
		this.lishiDachuPaiZuList = lishiDachuPaiZuList;
	}

	public DianShuZuPaiZu getPublicDachuPaiZu() {
		return publicDachuPaiZu;
	}

	public void setPublicDachuPaiZu(DianShuZuPaiZu publicDachuPaiZu) {
		this.publicDachuPaiZu = publicDachuPaiZu;
	}

	public List<DaPaiDianShuSolution> getYaPaiSolutionCandidates() {
		return yaPaiSolutionCandidates;
	}

	public void setYaPaiSolutionCandidates(List<DaPaiDianShuSolution> yaPaiSolutionCandidates) {
		this.yaPaiSolutionCandidates = yaPaiSolutionCandidates;
	}

	public List<DaPaiDianShuSolution> getYaPaiSolutionsForTips() {
		return yaPaiSolutionsForTips;
	}

	public void setYaPaiSolutionsForTips(List<DaPaiDianShuSolution> yaPaiSolutionsForTips) {
		this.yaPaiSolutionsForTips = yaPaiSolutionsForTips;
	}

}
