package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;

/**
 * 所有的点数组不包括单张点数组
 * 
 * @author lsc
 *
 */
public class PaiXing {
	private List<DuiziDianShuZu> duiziDianShuZuList = new ArrayList<>();
	private List<LianduiDianShuZu> lianduiDianShuZuList = new ArrayList<>();
	private List<LiansanzhangDianShuZu> liansanzhangDianShuZuList = new ArrayList<>();
	private List<SanzhangDianShuZu> sanzhangDianShuZuList = new ArrayList<>();
	private List<ShunziDianShuZu> shunziDianShuZuList = new ArrayList<>();
	private List<DanGeZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
	private List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = new ArrayList<>();
	private List<WangZhadanDianShuZu> wangZhadanDianShuZuList = new ArrayList<>();

	public PaiXing() {

	}

	public PaiXing(List<DuiziDianShuZu> duiziDianShuZuList, List<LianduiDianShuZu> lianduiDianShuZuList,
			List<LiansanzhangDianShuZu> liansanzhangDianShuZuList, List<SanzhangDianShuZu> sanzhangDianShuZuList,
			List<ShunziDianShuZu> shunziDianShuZuList, List<DanGeZhadanDianShuZu> zhadanDianShuZuList,
			List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList, List<WangZhadanDianShuZu> wangZhadanDianShuZuList) {
		this.duiziDianShuZuList = duiziDianShuZuList;
		this.lianduiDianShuZuList = lianduiDianShuZuList;
		this.liansanzhangDianShuZuList = liansanzhangDianShuZuList;
		this.sanzhangDianShuZuList = sanzhangDianShuZuList;
		this.shunziDianShuZuList = shunziDianShuZuList;
		this.zhadanDianShuZuList = zhadanDianShuZuList;
		this.lianXuZhadanDianShuZuList = lianXuZhadanDianShuZuList;
		this.wangZhadanDianShuZuList = wangZhadanDianShuZuList;
	}

	public boolean hasZhadan() {
		if (!wangZhadanDianShuZuList.isEmpty() || !lianXuZhadanDianShuZuList.isEmpty()
				|| !zhadanDianShuZuList.isEmpty()) {
			return true;
		}
		return false;
	}

	public List<DuiziDianShuZu> getDuiziDianShuZuList() {
		return duiziDianShuZuList;
	}

	public void setDuiziDianShuZuList(List<DuiziDianShuZu> duiziDianShuZuList) {
		this.duiziDianShuZuList = duiziDianShuZuList;
	}

	public List<LianduiDianShuZu> getLianduiDianShuZuList() {
		return lianduiDianShuZuList;
	}

	public void setLianduiDianShuZuList(List<LianduiDianShuZu> lianduiDianShuZuList) {
		this.lianduiDianShuZuList = lianduiDianShuZuList;
	}

	public List<LiansanzhangDianShuZu> getLiansanzhangDianShuZuList() {
		return liansanzhangDianShuZuList;
	}

	public void setLiansanzhangDianShuZuList(List<LiansanzhangDianShuZu> liansanzhangDianShuZuList) {
		this.liansanzhangDianShuZuList = liansanzhangDianShuZuList;
	}

	public List<SanzhangDianShuZu> getSanzhangDianShuZuList() {
		return sanzhangDianShuZuList;
	}

	public void setSanzhangDianShuZuList(List<SanzhangDianShuZu> sanzhangDianShuZuList) {
		this.sanzhangDianShuZuList = sanzhangDianShuZuList;
	}

	public List<ShunziDianShuZu> getShunziDianShuZuList() {
		return shunziDianShuZuList;
	}

	public void setShunziDianShuZuList(List<ShunziDianShuZu> shunziDianShuZuList) {
		this.shunziDianShuZuList = shunziDianShuZuList;
	}

	public List<DanGeZhadanDianShuZu> getZhadanDianShuZuList() {
		return zhadanDianShuZuList;
	}

	public void setZhadanDianShuZuList(List<DanGeZhadanDianShuZu> zhadanDianShuZuList) {
		this.zhadanDianShuZuList = zhadanDianShuZuList;
	}

	public List<LianXuZhadanDianShuZu> getLianXuZhadanDianShuZuList() {
		return lianXuZhadanDianShuZuList;
	}

	public void setLianXuZhadanDianShuZuList(List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList) {
		this.lianXuZhadanDianShuZuList = lianXuZhadanDianShuZuList;
	}

	public List<WangZhadanDianShuZu> getWangZhadanDianShuZuList() {
		return wangZhadanDianShuZuList;
	}

	public void setWangZhadanDianShuZuList(List<WangZhadanDianShuZu> wangZhadanDianShuZuList) {
		this.wangZhadanDianShuZuList = wangZhadanDianShuZuList;
	}

}
