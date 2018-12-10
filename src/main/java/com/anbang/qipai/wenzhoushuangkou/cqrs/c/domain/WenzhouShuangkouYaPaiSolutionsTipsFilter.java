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
				if (danGeZhadanSolutionList.isEmpty()) {
					danGeZhadanSolutionList.add(solution);
				} else {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) dianshuZu;
					int length = danGeZhadanSolutionList.size();
					boolean flag = false;
					for (int i = 0; i < length; i++) {
						DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) danGeZhadanSolutionList
								.get(i).getDianShuZu();
						if (danGeZhadanDianShuZu1.getDianShu().equals(danGeZhadanDianShuZu2.getDianShu())) {
							if (danGeZhadanDianShuZu1.getSize() > danGeZhadanDianShuZu2.getSize()) {
								danGeZhadanSolutionList.set(i, solution);
							}
							flag = true;
						}
					}
					if (!flag) {
						for (int i = 0; i < length; i++) {
							DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) danGeZhadanSolutionList
									.get(i).getDianShuZu();
							int compare = zhadanComparator.compare(danGeZhadanDianShuZu2, danGeZhadanDianShuZu1);
							if (compare > 0) {
								danGeZhadanSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								danGeZhadanSolutionList.add(solution);
							}
						}
					}
				}
			}
		}
		LinkedList<DaPaiDianShuSolution> zhadanSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
			if (zhadanSolutionList.isEmpty()) {
				zhadanSolutionList.add(zhadanSolution);
			} else {
				DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
				int length = zhadanSolutionList.size();
				for (int i = 0; i < length; i++) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) zhadanSolutionList.get(i)
							.getDianShuZu();
					if (danGeZhadanDianShuZu1.getSize() < danGeZhadanDianShuZu2.getSize()) {
						zhadanSolutionList.add(i, zhadanSolution);
						break;
					}
					if (i == length - 1) {
						zhadanSolutionList.add(zhadanSolution);
					}
				}
			}
		}
		DaPaiDianShuSolution maxDanGeZhadanSolution = null;
		for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DanGeZhadanDianShuZu) {
				DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) dianshuZu;
				if (maxDanGeZhadanSolution != null && zhadanComparator.compare(danGeZhadanDianShuZu1,
						(ZhadanDianShuZu) maxDanGeZhadanSolution.getDianShuZu()) > 0) {
					maxDanGeZhadanSolution = solution;
				} else {
					maxDanGeZhadanSolution = solution;
				}
			}
		}
		if (maxDanGeZhadanSolution != null && !zhadanSolutionList.isEmpty()) {
			DaPaiDianShuSolution solution = zhadanSolutionList.getLast();
			if (!solution.getDianshuZuheIdx().equals(maxDanGeZhadanSolution.getDianshuZuheIdx())) {
				zhadanSolutionList.add(maxDanGeZhadanSolution);
			}
		}
		List<DaPaiDianShuSolution> danzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> duiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> sanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> shunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> lianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> liansanzhangSolutionList = new LinkedList<>();
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
				if (danzhangSolutionList.isEmpty()) {
					danzhangSolutionList.add(solution);
				} else {
					int length = danzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DanzhangDianShuZu) danzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(danzhangDianShuZu.getDianShu()) > 0) {
							danzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							danzhangSolutionList.add(solution);
						}
						i++;
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
				if (duiziSolutionList.isEmpty()) {
					duiziSolutionList.add(solution);
				} else {
					int length = duiziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DuiziDianShuZu) duiziSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(duiziDianShuZu.getDianShu()) > 0) {
							duiziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							duiziSolutionList.add(solution);
						}
						i++;
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
				if (sanzhangSolutionList.isEmpty()) {
					sanzhangSolutionList.add(solution);
				} else {
					int length = sanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((SanzhangDianShuZu) sanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(sanzhangDianShuZu.getDianShu()) > 0) {
							sanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							sanzhangSolutionList.add(solution);
						}
						i++;
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
				if (lianduiSolutionList.isEmpty()) {
					lianduiSolutionList.add(solution);
				} else {
					int length = lianduiSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LianduiDianShuZu) lianduiSolutionList.get(i).getDianShuZu()).getLianXuDianShuArray()[0]
								.compareTo(((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							lianduiSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							lianduiSolutionList.add(solution);
						}
						i++;
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
				if (liansanzhangSolutionList.isEmpty()) {
					liansanzhangSolutionList.add(solution);
				} else {
					int length = liansanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LiansanzhangDianShuZu) liansanzhangSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0].compareTo(
										((LiansanzhangDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							liansanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							liansanzhangSolutionList.add(solution);
						}
						i++;
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
				if (shunziSolutionList.isEmpty()) {
					shunziSolutionList.add(solution);
				} else {
					int length = shunziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((ShunziDianShuZu) shunziSolutionList.get(i).getDianShuZu()).getLianXuDianShuArray()[0]
								.compareTo(((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							shunziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							shunziSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		// List<DaPaiDianShuSolution> zhadanSolutionList = new LinkedList<>();
		// for (DaPaiDianShuSolution solution : noWangSolutionList) {
		// DianShuZu dianshuZu = solution.getDianShuZu();
		// if (dianshuZu instanceof ZhadanDianShuZu) {
		// ZhadanDianShuZu zhadanDianShuZu = (ZhadanDianShuZu) dianshuZu;
		// if (zhadanSolutionList.isEmpty()) {
		// zhadanSolutionList.add(solution);
		// } else {
		// int length = zhadanSolutionList.size();
		// int i = 0;
		// while (i < length) {
		// int compate = zhadanComparator
		// .compare((ZhadanDianShuZu) zhadanSolutionList.get(i).getDianShuZu(),
		// zhadanDianShuZu);
		// if (compate > 0) {
		// zhadanSolutionList.add(i, solution);
		// break;
		// }
		// i++;
		// if (i == length) {
		// zhadanSolutionList.add(solution);
		// }
		// }
		// }
		// }
		// }
		filtedSolutionList.addAll(danzhangSolutionList);
		filtedSolutionList.addAll(duiziSolutionList);
		filtedSolutionList.addAll(sanzhangSolutionList);
		filtedSolutionList.addAll(shunziSolutionList);
		filtedSolutionList.addAll(lianduiSolutionList);
		filtedSolutionList.addAll(liansanzhangSolutionList);
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
