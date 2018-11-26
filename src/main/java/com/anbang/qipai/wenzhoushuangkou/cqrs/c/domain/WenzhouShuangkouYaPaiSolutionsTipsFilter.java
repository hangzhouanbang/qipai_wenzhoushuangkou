package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.ZhadanComparator;
import com.dml.shuangkou.player.action.da.YaPaiSolutionsTipsFilter;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;

/**
 * 提示顺序： 没有王牌代替的打法、不拆炸弹、同种类型、
 * 
 * @author lsc
 *
 */
public class WenzhouShuangkouYaPaiSolutionsTipsFilter implements YaPaiSolutionsTipsFilter {

	private ZhadanComparator zhadanComparator;

	@Override
	public List<DaPaiDianShuSolution> filter(List<DaPaiDianShuSolution> YaPaiSolutions) {
		List<DaPaiDianShuSolution> filtedSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSolutionList = new ArrayList<>();
		f1: for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			for (DianShu dianshu : solution.getDachuDianShuArray()) {
				if (DianShu.xiaowang.equals(dianshu) || DianShu.dawang.equals(dianshu)) {
					continue f1;
				}
			}
			noWangSolutionList.add(solution);
		}
		List<DaPaiDianShuSolution> danGeZhadanSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DanGeZhadanDianShuZu) {
				danGeZhadanSolutionList.add(solution);
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DanzhangDianShuZu) {
				DanzhangDianShuZu danzhangDianShuZu = (DanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (filtedSolutionList.isEmpty()) {
					filtedSolutionList.add(solution);
				} else {
					int length = filtedSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DanzhangDianShuZu) filtedSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(danzhangDianShuZu.getDianShu()) > 0) {
							filtedSolutionList.add(i, solution);
						}
						i++;
						filtedSolutionList.add(solution);
					}
				}
			}
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (filtedSolutionList.isEmpty()) {
					filtedSolutionList.add(solution);
				} else {
					int length = filtedSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DuiziDianShuZu) filtedSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(duiziDianShuZu.getDianShu()) > 0) {
							filtedSolutionList.add(i, solution);
						}
						i++;
						filtedSolutionList.add(solution);
					}
				}
			}
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (filtedSolutionList.isEmpty()) {
					filtedSolutionList.add(solution);
				} else {
					int length = filtedSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((SanzhangDianShuZu) filtedSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(sanzhangDianShuZu.getDianShu()) > 0) {
							filtedSolutionList.add(i, solution);
						}
						i++;
						filtedSolutionList.add(solution);
					}
				}
			}
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (filtedSolutionList.isEmpty()) {
					filtedSolutionList.add(solution);
				} else {
					int length = filtedSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LianduiDianShuZu) filtedSolutionList.get(i).getDianShuZu()).getLianXuDianShuArray()[0]
								.compareTo(((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							filtedSolutionList.add(i, solution);
						}
						i++;
						filtedSolutionList.add(solution);
					}
				}
			}
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (filtedSolutionList.isEmpty()) {
					filtedSolutionList.add(solution);
				} else {
					int length = filtedSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LiansanzhangDianShuZu) filtedSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0].compareTo(
										((LiansanzhangDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							filtedSolutionList.add(i, solution);
						}
						i++;
						filtedSolutionList.add(solution);
					}
				}
			}
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (filtedSolutionList.isEmpty()) {
					filtedSolutionList.add(solution);
				} else {
					int length = filtedSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((ShunziDianShuZu) filtedSolutionList.get(i).getDianShuZu()).getLianXuDianShuArray()[0]
								.compareTo(((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							filtedSolutionList.add(i, solution);
						}
						i++;
						filtedSolutionList.add(solution);
					}
				}
			}
		}
		List<DaPaiDianShuSolution> zhadanSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof ZhadanDianShuZu) {
				ZhadanDianShuZu zhadanDianShuZu = (ZhadanDianShuZu) dianshuZu;
				if (zhadanSolutionList.isEmpty()) {
					zhadanSolutionList.add(solution);
				} else {
					int length = zhadanSolutionList.size();
					int i = 0;
					while (i < length) {
						int compate = zhadanComparator
								.compare((ZhadanDianShuZu) zhadanSolutionList.get(i).getDianShuZu(), zhadanDianShuZu);
						if (compate > 0) {
							zhadanSolutionList.add(i, solution);
							break;
						}
						i++;
						if (i == length) {
							zhadanSolutionList.add(solution);
						}
					}
				}
			}
		}
		filtedSolutionList.addAll(zhadanSolutionList);
		return filtedSolutionList;
	}

	public ZhadanComparator getZhadanComparator() {
		return zhadanComparator;
	}

	public void setZhadanComparator(ZhadanComparator zhadanComparator) {
		this.zhadanComparator = zhadanComparator;
	}

}
