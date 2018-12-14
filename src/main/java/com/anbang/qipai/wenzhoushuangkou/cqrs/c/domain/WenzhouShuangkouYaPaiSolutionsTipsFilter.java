package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dml.puke.pai.DianShu;
import com.dml.puke.pai.PukePai;
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
	public List<DaPaiDianShuSolution> filter(List<DaPaiDianShuSolution> YaPaiSolutions,
			Map<Integer, PukePai> allShoupai) {
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
		List<DaPaiDianShuSolution> filtedSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSolutionList = new ArrayList<>();
		List<DaPaiDianShuSolution> hasWangSolutionList = new ArrayList<>();
		f1: for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			for (DianShu dianshu : solution.getDachuDianShuArray()) {
				if (DianShu.xiaowang.equals(dianshu) || DianShu.dawang.equals(dianshu)) {
					hasWangSolutionList.add(solution);
					continue f1;
				}
			}
			noWangSolutionList.add(solution);
		}

		LinkedList<DaPaiDianShuSolution> danGeZhadanSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DanGeZhadanDianShuZu) {
				DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) dianshuZu;
				DianShu dianshu = danGeZhadanDianShuZu1.getDianShu();
				if (danGeZhadanDianShuZu1.getSize() == dianshuCountArray[dianshu.ordinal()]) {
					if (danGeZhadanSolutionList.isEmpty()) {
						danGeZhadanSolutionList.add(solution);
					} else {
						int length = danGeZhadanSolutionList.size();
						for (int i = 0; i < length; i++) {
							DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) danGeZhadanSolutionList
									.get(i).getDianShuZu();
							int compare = danGeZhadanDianShuZu2.getDianShu().compareTo(dianshu);
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

		List<DaPaiDianShuSolution> noWangDanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangDuiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangShunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangLianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangLiansanzhangSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			// 三张
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				DianShu dianshu = sanzhangDianShuZu.getDianShu();
				if (dianshuCountArray[dianshu.ordinal()] < 4) {
					if (noWangSanzhangSolutionList.isEmpty()) {
						noWangSanzhangSolutionList.add(solution);
					} else {
						int length = noWangSanzhangSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((SanzhangDianShuZu) noWangSanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
									.compareTo(sanzhangDianShuZu.getDianShu()) > 0) {
								noWangSanzhangSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								noWangSanzhangSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 对子
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				DianShu dianshu = duiziDianShuZu.getDianShu();
				if (dianshuCountArray[dianshu.ordinal()] < 4) {
					if (noWangDuiziSolutionList.isEmpty()) {
						noWangDuiziSolutionList.add(solution);
					} else {
						int length = noWangDuiziSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((DuiziDianShuZu) noWangDuiziSolutionList.get(i).getDianShuZu()).getDianShu()
									.compareTo(duiziDianShuZu.getDianShu()) > 0) {
								noWangDuiziSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								noWangDuiziSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 单张
			if (dianshuZu instanceof DanzhangDianShuZu) {
				DanzhangDianShuZu danzhangDianShuZu = (DanzhangDianShuZu) dianshuZu;
				DianShu dianshu = danzhangDianShuZu.getDianShu();
				if (dianshuCountArray[dianshu.ordinal()] < 4) {
					if (noWangDanzhangSolutionList.isEmpty()) {
						noWangDanzhangSolutionList.add(solution);
					} else {
						int length = noWangDanzhangSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((DanzhangDianShuZu) noWangDanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
									.compareTo(danzhangDianShuZu.getDianShu()) > 0) {
								noWangDanzhangSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								noWangDanzhangSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 连三张
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				DianShu[] lianXuDianShuArray = liansanzhangDianShuZu.getLianXuDianShuArray();
				boolean allSuccess = true;
				for (DianShu dianshu : lianXuDianShuArray) {
					if (dianshuCountArray[dianshu.ordinal()] != 3) {
						allSuccess = false;
						break;
					}
				}
				if (allSuccess) {
					if (noWangLiansanzhangSolutionList.isEmpty()) {
						noWangLiansanzhangSolutionList.add(solution);
					} else {
						int length = noWangLiansanzhangSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((LiansanzhangDianShuZu) noWangLiansanzhangSolutionList.get(i).getDianShuZu())
									.getLianXuDianShuArray()[0].compareTo(
											((LiansanzhangDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
								noWangLiansanzhangSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								noWangLiansanzhangSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 连对
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				DianShu[] lianXuDianShuArray = lianduiDianShuZu.getLianXuDianShuArray();
				boolean allSuccess = true;
				for (DianShu dianshu : lianXuDianShuArray) {
					if (dianshuCountArray[dianshu.ordinal()] != 2) {
						allSuccess = false;
						break;
					}
				}
				if (allSuccess) {
					if (noWangLianduiSolutionList.isEmpty()) {
						noWangLianduiSolutionList.add(solution);
					} else {
						int length = noWangLianduiSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((LianduiDianShuZu) noWangLianduiSolutionList.get(i).getDianShuZu())
									.getLianXuDianShuArray()[0]
											.compareTo(((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
								noWangLianduiSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								noWangLianduiSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 顺子
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				DianShu[] lianXuDianShuArray = shunziDianShuZu.getLianXuDianShuArray();
				boolean allSuccess = true;
				for (DianShu dianshu : lianXuDianShuArray) {
					if (dianshuCountArray[dianshu.ordinal()] != 1) {
						allSuccess = false;
						break;
					}
				}
				if (allSuccess) {
					if (noWangShunziSolutionList.isEmpty()) {
						noWangShunziSolutionList.add(solution);
					} else {
						int length = noWangShunziSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((ShunziDianShuZu) noWangShunziSolutionList.get(i).getDianShuZu())
									.getLianXuDianShuArray()[0]
											.compareTo(((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
								noWangShunziSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								noWangShunziSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
		}

		List<DaPaiDianShuSolution> hasWangDanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangDuiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangSanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangShunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangLianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangLiansanzhangSolutionList = new LinkedList<>();
		f2: for (DaPaiDianShuSolution solution : hasWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			// 三张
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				DianShu dianshu = sanzhangDianShuZu.getDianShu();
				if (dianshuCountArray[dianshu.ordinal()] >= 3) {
					continue f2;
				}
				if (hasWangSanzhangSolutionList.isEmpty()) {
					hasWangSanzhangSolutionList.add(solution);
				} else {
					int length = hasWangSanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((SanzhangDianShuZu) hasWangSanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(sanzhangDianShuZu.getDianShu()) > 0) {
							hasWangSanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangSanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
			// 对子
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				DianShu dianshu = duiziDianShuZu.getDianShu();
				if (dianshuCountArray[dianshu.ordinal()] >= 2) {
					continue f2;
				}
				if (hasWangDuiziSolutionList.isEmpty()) {
					hasWangDuiziSolutionList.add(solution);
				} else {
					int length = hasWangDuiziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DuiziDianShuZu) hasWangDuiziSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(duiziDianShuZu.getDianShu()) > 0) {
							hasWangDuiziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangDuiziSolutionList.add(solution);
						}
						i++;
					}
				}
			}
			// 单张
			if (dianshuZu instanceof DanzhangDianShuZu) {
				DanzhangDianShuZu danzhangDianShuZu = (DanzhangDianShuZu) dianshuZu;
				DianShu dianshu = danzhangDianShuZu.getDianShu();
				if (dianshuCountArray[dianshu.ordinal()] >= 1) {
					continue f2;
				}
				if (hasWangDanzhangSolutionList.isEmpty()) {
					hasWangDanzhangSolutionList.add(solution);
				} else {
					int length = hasWangDanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DanzhangDianShuZu) hasWangDanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(danzhangDianShuZu.getDianShu()) > 0) {
							hasWangDanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangDanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
			// 连三张
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				DianShu[] lianXuDianShuArray = liansanzhangDianShuZu.getLianXuDianShuArray();
				boolean allSuccess = true;
				for (DianShu dianshu : lianXuDianShuArray) {
					if (dianshuCountArray[dianshu.ordinal()] >= 3) {
						allSuccess = false;
						break;
					}
				}
				if (allSuccess) {
					if (hasWangLiansanzhangSolutionList.isEmpty()) {
						hasWangLiansanzhangSolutionList.add(solution);
					} else {
						int length = hasWangLiansanzhangSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((LiansanzhangDianShuZu) hasWangLiansanzhangSolutionList.get(i).getDianShuZu())
									.getLianXuDianShuArray()[0].compareTo(
											((LiansanzhangDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
								hasWangLiansanzhangSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								hasWangLiansanzhangSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 连对
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				DianShu[] lianXuDianShuArray = lianduiDianShuZu.getLianXuDianShuArray();
				boolean allSuccess = true;
				for (DianShu dianshu : lianXuDianShuArray) {
					if (dianshuCountArray[dianshu.ordinal()] >= 2) {
						allSuccess = false;
						break;
					}
				}
				if (allSuccess) {
					if (hasWangLianduiSolutionList.isEmpty()) {
						hasWangLianduiSolutionList.add(solution);
					} else {
						int length = hasWangLianduiSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((LianduiDianShuZu) hasWangLianduiSolutionList.get(i).getDianShuZu())
									.getLianXuDianShuArray()[0]
											.compareTo(((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
								hasWangLianduiSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								hasWangLianduiSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
			// 顺子
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				DianShu[] lianXuDianShuArray = shunziDianShuZu.getLianXuDianShuArray();
				boolean allSuccess = true;
				for (DianShu dianshu : lianXuDianShuArray) {
					if (dianshuCountArray[dianshu.ordinal()] >= 1) {
						allSuccess = false;
						break;
					}
				}
				if (allSuccess) {
					if (hasWangShunziSolutionList.isEmpty()) {
						hasWangShunziSolutionList.add(solution);
					} else {
						int length = hasWangShunziSolutionList.size();
						int i = 0;
						while (i < length) {
							if (((ShunziDianShuZu) hasWangShunziSolutionList.get(i).getDianShuZu())
									.getLianXuDianShuArray()[0]
											.compareTo(((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
								hasWangShunziSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								hasWangShunziSolutionList.add(solution);
							}
							i++;
						}
					}
				}
			}
		}
		DaPaiDianShuSolution maxZhadanSolution = null;
		for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof ZhadanDianShuZu) {
				ZhadanDianShuZu zhadanDianShuZu1 = (ZhadanDianShuZu) dianshuZu;
				if (maxZhadanSolution == null || zhadanComparator.compare(zhadanDianShuZu1,
						(ZhadanDianShuZu) maxZhadanSolution.getDianShuZu()) > 0) {
					maxZhadanSolution = solution;
				}
			}
		}
		if (maxZhadanSolution != null) {
			if (!zhadanSolutionList.isEmpty()) {
				DaPaiDianShuSolution solution = zhadanSolutionList.getLast();
				if (!solution.getDianshuZuheIdx().equals(maxZhadanSolution.getDianshuZuheIdx())) {
					zhadanSolutionList.add(maxZhadanSolution);
				}
			} else {
				zhadanSolutionList.add(maxZhadanSolution);
			}
		}
		filtedSolutionList.addAll(noWangDanzhangSolutionList);
		filtedSolutionList.addAll(noWangDuiziSolutionList);
		filtedSolutionList.addAll(noWangSanzhangSolutionList);
		filtedSolutionList.addAll(noWangShunziSolutionList);
		filtedSolutionList.addAll(noWangLianduiSolutionList);
		filtedSolutionList.addAll(noWangLiansanzhangSolutionList);
		filtedSolutionList.addAll(hasWangDanzhangSolutionList);
		filtedSolutionList.addAll(hasWangDuiziSolutionList);
		filtedSolutionList.addAll(hasWangSanzhangSolutionList);
		filtedSolutionList.addAll(hasWangShunziSolutionList);
		filtedSolutionList.addAll(hasWangLianduiSolutionList);
		filtedSolutionList.addAll(hasWangLiansanzhangSolutionList);
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
