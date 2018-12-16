package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouZhadanComparator;
import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZuGenerator;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianXuDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.CanNotCompareException;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.DanGeDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.LianXuDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.NoZhadanDanGeDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.TongDengLianXuDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.ZhadanComparator;
import com.dml.shuangkou.pai.dianshuzu.DianShuZuCalculator;
import com.dml.shuangkou.pai.dianshuzu.LianXuZhadanDianShuZu;
import com.dml.shuangkou.pai.dianshuzu.PaiXing;
import com.dml.shuangkou.pai.dianshuzu.WangZhadanDianShuZu;
import com.dml.shuangkou.pai.jiesuanpai.DawangDangPai;
import com.dml.shuangkou.pai.jiesuanpai.ShoupaiJiesuanPai;
import com.dml.shuangkou.pai.jiesuanpai.XiaowangDangPai;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class CaseTest {
	private static BianXingWanFa bx = BianXingWanFa.banqianbian;
	private static ZhadanComparator zhadanComparator = new WenzhouShuangkouZhadanComparator();
	private static DanGeDianShuZuComparator danGeDianShuZuComparator = new NoZhadanDanGeDianShuZuComparator();
	private static LianXuDianShuZuComparator lianXuDianShuZuComparator = new TongDengLianXuDianShuZuComparator();

	// 炸弹压牌
	public static void main(String[] args) {
		DianShuZu beiYaDianShuZu = new DuiziDianShuZu(DianShu.Q);
		int[] dianShuAmountArray = { 1, 2, 0, 3, 3, 0, 3, 0, 4, 5, 3, 0, 0, 2, 1 };
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>(
				calculateForZhadan(beiYaDianShuZu, dianShuAmountArray).values());
		solutionList = filter(solutionList, dianShuAmountArray.clone(), true);
		System.out.println(solutionList);
	}

	// 普通压牌
	// public static void main(String[] args) {
	// long s1 = System.currentTimeMillis();
	// DianShuZu beiYaDianShuZu = new DanzhangDianShuZu(DianShu.qi);
	// int[] dianShuAmountArray = { 1, 1, 8, 0, 3, 3, 2, 2, 1, 3, 3, 0, 0, 0, 0 };
	// List<DaPaiDianShuSolution> solutionList = new ArrayList<>(
	// calculate(beiYaDianShuZu, dianShuAmountArray.clone()).values());
	// solutionList.addAll(calculateForZhadan(beiYaDianShuZu,
	// dianShuAmountArray.clone()).values());
	// solutionList = filter(solutionList, dianShuAmountArray.clone(), true);
	// long s2 = System.currentTimeMillis();
	// System.out.println(s2 - s1 + "ms");
	// }

	// 所有可打的牌
	// public static void main(String[] args) {
	// int[] dianShuAmountArray = { 0, 0, 0, 0, 2, 0, 3, 2, 2, 0, 0, 0, 0, 2, 1 };
	// long s1 = System.currentTimeMillis();
	// List<DaPaiDianShuSolution> solutionList = new ArrayList<>(
	// generateAllKedaPaiSolutions(dianShuAmountArray.clone()).values());
	// solutionList = filter(solutionList, dianShuAmountArray, false);
	// long s2 = System.currentTimeMillis();
	// System.out.println(s2 - s1);
	// }

	public static List<DaPaiDianShuSolution> filter(List<DaPaiDianShuSolution> YaPaiSolutions, int[] dianshuCountArray,
			boolean yapai) {
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

		LinkedList<DaPaiDianShuSolution> noWangDanzhangSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> noWangDuiziSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> noWangSanzhangSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> noWangShunziSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> noWangLianduiSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> noWangLiansanzhangSolutionList = new LinkedList<>();

		// 单张至两张的单张
		LinkedList<DaPaiDianShuSolution> noWangDanzhangYiShangSolutionList = new LinkedList<>();
		// 两张以上的单张
		LinkedList<DaPaiDianShuSolution> noWangDanzhangDuiziYiShangSolutionList = new LinkedList<>();
		// 有三张的对子
		LinkedList<DaPaiDianShuSolution> noWangDuiziYiShangSolutionList = new LinkedList<>();

		LinkedList<DaPaiDianShuSolution> hasWangDanzhangSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> hasWangDuiziSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> hasWangSanzhangSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> hasWangShunziSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> hasWangLianduiSolutionList = new LinkedList<>();
		LinkedList<DaPaiDianShuSolution> hasWangLiansanzhangSolutionList = new LinkedList<>();
		if (!yapai) {
			for (DaPaiDianShuSolution solution : noWangSolutionList) {
				DianShuZu dianshuZu = solution.getDianShuZu();
				// 三张
				if (dianshuZu instanceof SanzhangDianShuZu) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
					DianShu dianshu = sanzhangDianShuZu.getDianShu();
					if (dianshuCountArray[dianshu.ordinal()] == 3) {
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
					if (dianshuCountArray[dianshu.ordinal()] == 2) {
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
					if (dianshuCountArray[dianshu.ordinal()] == 1) {
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
										.getLianXuDianShuArray()[0].compareTo(
												((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
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
										.getLianXuDianShuArray()[0].compareTo(
												((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
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
		} else {
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
						if (dianshuCountArray[dianshu.ordinal()] > 2) {
							if (noWangDuiziYiShangSolutionList.isEmpty()) {
								noWangDuiziYiShangSolutionList.add(solution);
							} else {
								int length1 = noWangDuiziYiShangSolutionList.size();
								int j = 0;
								while (j < length1) {
									if (((DuiziDianShuZu) noWangDuiziYiShangSolutionList.get(j).getDianShuZu())
											.getDianShu().compareTo(duiziDianShuZu.getDianShu()) > 0) {
										noWangDuiziYiShangSolutionList.add(j, solution);
										break;
									}
									if (j == length1 - 1) {
										noWangDuiziYiShangSolutionList.add(solution);
									}
									j++;
								}
							}
						} else {
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
				}
				// 单张
				if (dianshuZu instanceof DanzhangDianShuZu) {
					DanzhangDianShuZu danzhangDianShuZu = (DanzhangDianShuZu) dianshuZu;
					DianShu dianshu = danzhangDianShuZu.getDianShu();
					if (dianshuCountArray[dianshu.ordinal()] < 4) {
						if (dianshuCountArray[dianshu.ordinal()] > 2) {
							if (noWangDanzhangDuiziYiShangSolutionList.isEmpty()) {
								noWangDanzhangDuiziYiShangSolutionList.add(solution);
							} else {
								int length1 = noWangDanzhangDuiziYiShangSolutionList.size();
								int j = 0;
								while (j < length1) {
									if (((DanzhangDianShuZu) noWangDanzhangDuiziYiShangSolutionList.get(j)
											.getDianShuZu()).getDianShu()
													.compareTo(danzhangDianShuZu.getDianShu()) > 0) {

										noWangDanzhangDuiziYiShangSolutionList.add(j, solution);
										break;
									}
									if (j == length1 - 1) {
										noWangDanzhangDuiziYiShangSolutionList.add(solution);
									}
									j++;
								}
							}
						} else if (dianshuCountArray[dianshu.ordinal()] > 1) {
							if (noWangDanzhangYiShangSolutionList.isEmpty()) {
								noWangDanzhangYiShangSolutionList.add(solution);
							} else {
								int length1 = noWangDanzhangYiShangSolutionList.size();
								int j = 0;
								while (j < length1) {
									if (((DanzhangDianShuZu) noWangDanzhangYiShangSolutionList.get(j).getDianShuZu())
											.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) > 0) {
										noWangDanzhangYiShangSolutionList.add(j, solution);
										break;
									}
									if (j == length1 - 1) {
										noWangDanzhangYiShangSolutionList.add(solution);
									}
									j++;
								}
							}
						} else {
							if (noWangDanzhangSolutionList.isEmpty()) {
								noWangDanzhangSolutionList.add(solution);
							} else {
								int length = noWangDanzhangSolutionList.size();
								int i = 0;
								while (i < length) {
									if (((DanzhangDianShuZu) noWangDanzhangSolutionList.get(i).getDianShuZu())
											.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) > 0) {
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
										.getLianXuDianShuArray()[0].compareTo(
												((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
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
										.getLianXuDianShuArray()[0].compareTo(
												((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
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
		}
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
				if (dianshuCountArray[dianshu.ordinal()] >= 1 && !dianshu.equals(DianShu.xiaowang)
						&& !dianshu.equals(DianShu.dawang)) {
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
		noWangDanzhangSolutionList.addAll(noWangDanzhangYiShangSolutionList);
		noWangDanzhangSolutionList.addAll(noWangDanzhangDuiziYiShangSolutionList);
		noWangDuiziSolutionList.addAll(noWangDuiziYiShangSolutionList);

		filtedSolutionList.addAll(noWangDanzhangSolutionList);
		filtedSolutionList.addAll(noWangDuiziSolutionList);
		filtedSolutionList.addAll(noWangSanzhangSolutionList);
		filtedSolutionList.addAll(noWangShunziSolutionList);
		filtedSolutionList.addAll(noWangLianduiSolutionList);
		filtedSolutionList.addAll(noWangLiansanzhangSolutionList);
		if (noWangDanzhangSolutionList.isEmpty()) {
			filtedSolutionList.addAll(hasWangDanzhangSolutionList);
		}
		if (noWangDuiziSolutionList.isEmpty()) {
			filtedSolutionList.addAll(hasWangDuiziSolutionList);
		}
		if (noWangSanzhangSolutionList.isEmpty()) {
			filtedSolutionList.addAll(hasWangSanzhangSolutionList);
		}
		if (noWangShunziSolutionList.isEmpty()) {
			filtedSolutionList.addAll(hasWangShunziSolutionList);
		}
		if (noWangLianduiSolutionList.isEmpty()) {
			filtedSolutionList.addAll(hasWangLianduiSolutionList);
		}
		if (noWangLiansanzhangSolutionList.isEmpty()) {
			filtedSolutionList.addAll(hasWangLiansanzhangSolutionList);
		}
		filtedSolutionList.addAll(zhadanSolutionList);
		return filtedSolutionList;
	}

	public static Map<String, DaPaiDianShuSolution> generateAllKedaPaiSolutions(int[] dianshuCountArray) {
		Map<String, DaPaiDianShuSolution> yaPaiSolutionCandidates = new HashMap<>();
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		// 大小王做单张牌打出必定是作为本身的牌的点数
		List<DanzhangDianShuZu> danzhangDianShuZuList = DianShuZuGenerator
				.generateAllDanzhangDianShuZu(dianshuCountArray);
		for (DanzhangDianShuZu danzhangDianShuZu : danzhangDianShuZuList) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(danzhangDianShuZu);
			DianShu[] dachuDianShuArray = { danzhangDianShuZu.getDianShu() };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		int xiaowangCount = dianshuCountArray[13];
		int dawangCount = dianshuCountArray[14];
		// 对子，当有大小王个一张,千变时可以当一对大王打出，百变时可以当一对小王打出
		if (xiaowangCount > 0 && dawangCount > 0) {
			DuiziDianShuZu duiziDianShuZu = null;
			if (BianXingWanFa.qianbian.equals(bx)) {
				duiziDianShuZu = new DuiziDianShuZu(DianShu.dawang);
			} else if (BianXingWanFa.baibian.equals(bx) || BianXingWanFa.banqianbian.equals(bx)) {
				duiziDianShuZu = new DuiziDianShuZu(DianShu.xiaowang);
			}
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(duiziDianShuZu);
			DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.dawang };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		if (dawangCount > 1) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(new DuiziDianShuZu(DianShu.dawang));
			DianShu[] dachuDianShuArray = { DianShu.dawang, DianShu.dawang };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		if (xiaowangCount > 1) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(new DuiziDianShuZu(DianShu.xiaowang));
			DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.xiaowang };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		// 王炸
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = DianShuZuCalculator
				.generateAllWangZhadanDianShuZu(dianshuCountArray);

		for (WangZhadanDianShuZu wangZhadanDianShuZu : wangZhadanDianShuZuList) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(wangZhadanDianShuZu);
			List<DianShu> dachuDianShuList = new ArrayList<>();
			DianShu[] lianXuDianShuArray = wangZhadanDianShuZu.getDianShuZu();
			for (int count = 0; count < wangZhadanDianShuZu.getXiaowangCount(); count++) {
				dachuDianShuList.add(lianXuDianShuArray[0]);
			}
			for (int count = 0; count < wangZhadanDianShuZu.getDawangCount(); count++) {
				dachuDianShuList.add(lianXuDianShuArray[1]);
			}
			DianShu[] dachuDianShuArray = dachuDianShuList.toArray(new DianShu[dachuDianShuList.size()]);
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		int wangCount = 0;
		if (BianXingWanFa.qianbian.equals(bx)) {// 千变
			wangCount = xiaowangCount + dawangCount;
			// 减去王牌的数量
			dianshuCountArray[13] = dianshuCountArray[13] - xiaowangCount;
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
			wangCount = dawangCount;
			// 减去王牌的数量
			if (xiaowangCount > 0 && xiaowangCount % 2 == 0) {
				wangCount++;
				dianshuCountArray[13] = dianshuCountArray[13] - 2;
			}
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
			wangCount = dawangCount;
			// 减去王牌的数量
			dianshuCountArray[14] = dianshuCountArray[14] - dawangCount;
		} else {

		}
		if (wangCount > 0) {
			// 有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithWangDang(wangCount, dianshuCountArray, xiaowangCount,
					dawangCount));
		} else {
			// 没有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithoutWangDang(dianshuCountArray));
		}
		solutionList.forEach((solution) -> {
			DaPaiDianShuSolution daPaiDianShuSolution = yaPaiSolutionCandidates.get(solution.getDianshuZuheIdx());
			if (daPaiDianShuSolution != null) {
				DianShuZu dianShuZu = daPaiDianShuSolution.getDianShuZu();
				if (dianShuZu instanceof LianXuDianShuZu) {
					try {
						if (lianXuDianShuZuComparator.compare((LianXuDianShuZu) solution.getDianShuZu(),
								(LianXuDianShuZu) dianShuZu) > 0) {
							yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
						}
					} catch (CanNotCompareException e) {
					}
				} else if (dianShuZu instanceof LianXuZhadanDianShuZu) {
					if (zhadanComparator.compare((ZhadanDianShuZu) solution.getDianShuZu(),
							(ZhadanDianShuZu) dianShuZu) > 0) {
						yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
					}
				}
			} else {
				yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
			}
		});
		return yaPaiSolutionCandidates;
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDang(int wangCount,
			int[] dianshuCountArray, int xiaowangCount, int dawangCount) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		// 循环王的各种当法
		int maxZuheCode = (int) Math.pow(13, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(13, wangCount - 1 - m);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			ShoupaiJiesuanPai[] wangDangPaiArray = new ShoupaiJiesuanPai[wangCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int n = 0; n < wangCount; n++) {
				int mod = modArray[n];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {// 计算王的各种当法，排除效果相同的当法
					int yu = temp % mod;
					if (BianXingWanFa.qianbian.equals(bx)) {// 千变
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
					} else {

					}
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					wangDangPaiArray = null;
					break;
				}
			}
			if (wangDangPaiArray != null) {
				// 加上当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]++;
				}
				PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
				solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithWangDang(paiXing,
						wangDangPaiArray, dianshuCountArray, bx));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return new ArrayList<>(solutionSet);
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
		return new ArrayList<>(solutionSet);
	}

	public static Map<String, DaPaiDianShuSolution> calculate(DianShuZu beiYaDianShuZu, int[] dianShuAmountArray) {
		int[] dianShuAmount = dianShuAmountArray.clone();
		Map<String, DaPaiDianShuSolution> yaPaiSolutionCandidates = new HashMap<>();
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		// 单张
		if (beiYaDianShuZu instanceof DanzhangDianShuZu) {
			DanzhangDianShuZu beiYaDanzhangDianShuZu = (DanzhangDianShuZu) beiYaDianShuZu;
			// 大小王做单张牌打出必定是作为本身的牌的点数
			List<DanzhangDianShuZu> danzhangDianShuZuList = DianShuZuGenerator
					.generateAllDanzhangDianShuZu(dianShuAmount);
			for (DanzhangDianShuZu danzhangDianShuZu : danzhangDianShuZuList) {
				try {
					if (danGeDianShuZuComparator.compare(danzhangDianShuZu, beiYaDanzhangDianShuZu) > 0) {

						DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
						solution.setDianShuZu(danzhangDianShuZu);
						DianShu[] dachuDianShuArray = { danzhangDianShuZu.getDianShu() };
						solution.setDachuDianShuArray(dachuDianShuArray);
						solution.calculateDianshuZuheIdx();
						solutionList.add(solution);
					}
				} catch (CanNotCompareException e) {

				}
			}
			solutionList.forEach((solution) -> yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution));
			return yaPaiSolutionCandidates;
		}
		int xiaowangCount = dianShuAmount[13];
		int dawangCount = dianShuAmount[14];
		if (beiYaDianShuZu instanceof DuiziDianShuZu) {
			DuiziDianShuZu beiYaDuiziDianShuZu = (DuiziDianShuZu) beiYaDianShuZu;
			try {
				// 对子，当有大小王个一张时可以当一对大王打出
				if (xiaowangCount > 0 && dawangCount > 0) {
					DuiziDianShuZu duiziDianShuZu = null;
					if (BianXingWanFa.qianbian.equals(bx)) {
						duiziDianShuZu = new DuiziDianShuZu(DianShu.dawang);
					} else {
						duiziDianShuZu = new DuiziDianShuZu(DianShu.xiaowang);
					}
					if (danGeDianShuZuComparator.compare(duiziDianShuZu, beiYaDuiziDianShuZu) > 0) {
						DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
						solution.setDianShuZu(duiziDianShuZu);
						DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.dawang };
						solution.setDachuDianShuArray(dachuDianShuArray);
						solution.calculateDianshuZuheIdx();
						solutionList.add(solution);
					}
				}
				if (dawangCount > 1) {
					DuiziDianShuZu duiziDianShuZu = new DuiziDianShuZu(DianShu.dawang);
					if (danGeDianShuZuComparator.compare(duiziDianShuZu, beiYaDuiziDianShuZu) > 0) {
						DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
						solution.setDianShuZu(duiziDianShuZu);
						DianShu[] dachuDianShuArray = { DianShu.dawang, DianShu.dawang };
						solution.setDachuDianShuArray(dachuDianShuArray);
						solution.calculateDianshuZuheIdx();
						solutionList.add(solution);
					}
				}
				if (xiaowangCount > 1) {
					DuiziDianShuZu duiziDianShuZu = new DuiziDianShuZu(DianShu.xiaowang);
					if (danGeDianShuZuComparator.compare(duiziDianShuZu, beiYaDuiziDianShuZu) > 0) {
						DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
						solution.setDianShuZu(duiziDianShuZu);
						DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.xiaowang };
						solution.setDachuDianShuArray(dachuDianShuArray);
						solution.calculateDianshuZuheIdx();
						solutionList.add(solution);
					}
				}
			} catch (CanNotCompareException e) {

			}
		}

		int wangCount = 0;
		if (BianXingWanFa.qianbian.equals(bx)) {// 千变
			wangCount = xiaowangCount + dawangCount;
			// 减去王牌的数量
			dianShuAmount[13] = dianShuAmount[13] - xiaowangCount;
			dianShuAmount[14] = dianShuAmount[14] - dawangCount;
		} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
			wangCount = dawangCount;
			// 减去王牌的数量
			if (xiaowangCount > 0 && xiaowangCount % 2 == 0) {
				wangCount++;
				dianShuAmount[13] = dianShuAmount[13] - 2;
			}
			dianShuAmount[14] = dianShuAmount[14] - dawangCount;
		} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
			wangCount = dawangCount;
			// 减去王牌的数量
			dianShuAmount[14] = dianShuAmount[14] - dawangCount;
		} else {

		}
		if (wangCount > 0) {
			// 有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithWangDang(wangCount, dianShuAmount, xiaowangCount,
					dawangCount, beiYaDianShuZu));
		} else {
			// 没有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithoutWangDang(dianShuAmount, beiYaDianShuZu));
		}

		solutionList.forEach((solution) -> {
			DaPaiDianShuSolution daPaiDianShuSolution = yaPaiSolutionCandidates.get(solution.getDianshuZuheIdx());
			if (daPaiDianShuSolution != null) {
				DianShuZu dianShuZu = daPaiDianShuSolution.getDianShuZu();
				if (dianShuZu instanceof LianXuDianShuZu) {
					try {
						if (lianXuDianShuZuComparator.compare((LianXuDianShuZu) solution.getDianShuZu(),
								(LianXuDianShuZu) dianShuZu) > 0) {
							yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
						}
					} catch (CanNotCompareException e) {
					}
				}
			} else {
				yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
			}
		});
		return yaPaiSolutionCandidates;
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDang(int wangCount,
			int[] dianshuCountArray, int xiaowangCount, int dawangCount, DianShuZu beiYaDianShuZu) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		// 循环王的各种当法
		int maxZuheCode = (int) Math.pow(13, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(13, wangCount - 1 - m);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			ShoupaiJiesuanPai[] wangDangPaiArray = new ShoupaiJiesuanPai[wangCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int n = 0; n < wangCount; n++) {
				int mod = modArray[n];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {// 计算王的各种当法，排除效果相同的当法
					int yu = temp % mod;
					if (BianXingWanFa.qianbian.equals(bx)) {// 千变
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
					} else {

					}
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					wangDangPaiArray = null;
					break;
				}
			}
			if (wangDangPaiArray != null) {
				// 加上当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]++;
				}
				PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
				paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
				solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithWangDang(paiXing,
						wangDangPaiArray, dianshuCountArray, bx));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return new ArrayList<>(solutionSet);
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray,
			DianShuZu beiYaDianShuZu) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
		return new ArrayList<>(solutionSet);
	}

	private static PaiXing paiXingFilter(PaiXing paiXing, DianShuZu beiYaDianShuZu) {
		PaiXing filtedPaiXing = new PaiXing();
		if (beiYaDianShuZu instanceof DuiziDianShuZu) {
			DuiziDianShuZu beiYaDuiziDianShuZu = (DuiziDianShuZu) beiYaDianShuZu;
			List<DuiziDianShuZu> filtedDuiziDianShuZuList = filtedPaiXing.getDuiziDianShuZuList();
			List<DuiziDianShuZu> duiziDianShuZuList = paiXing.getDuiziDianShuZuList();
			for (DuiziDianShuZu duiziDianShuZu : duiziDianShuZuList) {
				try {
					if (danGeDianShuZuComparator.compare(duiziDianShuZu, beiYaDuiziDianShuZu) > 0) {
						filtedDuiziDianShuZuList.add(duiziDianShuZu);
					}
				} catch (CanNotCompareException e) {

				}
			}
			return filtedPaiXing;
		}
		if (beiYaDianShuZu instanceof SanzhangDianShuZu) {
			SanzhangDianShuZu beiYaSanzhangDianShuZu = (SanzhangDianShuZu) beiYaDianShuZu;
			List<SanzhangDianShuZu> filtedSanzhangDianShuZu = filtedPaiXing.getSanzhangDianShuZuList();
			List<SanzhangDianShuZu> sanzhangDianShuZuList = paiXing.getSanzhangDianShuZuList();
			for (SanzhangDianShuZu sanzhangDianShuZu : sanzhangDianShuZuList) {
				try {
					if (danGeDianShuZuComparator.compare(sanzhangDianShuZu, beiYaSanzhangDianShuZu) > 0) {
						filtedSanzhangDianShuZu.add(sanzhangDianShuZu);
					}
				} catch (CanNotCompareException e) {

				}
			}
			return filtedPaiXing;
		}
		if (beiYaDianShuZu instanceof ShunziDianShuZu) {
			ShunziDianShuZu beiYaShunziDianShuZu = (ShunziDianShuZu) beiYaDianShuZu;
			List<ShunziDianShuZu> filtedShunziDianShuZu = filtedPaiXing.getShunziDianShuZuList();
			List<ShunziDianShuZu> shunziDianShuZuList = paiXing.getShunziDianShuZuList();
			for (ShunziDianShuZu shunziDianShuZu : shunziDianShuZuList) {
				try {
					if (lianXuDianShuZuComparator.compare(shunziDianShuZu, beiYaShunziDianShuZu) > 0) {
						filtedShunziDianShuZu.add(shunziDianShuZu);
					}
				} catch (CanNotCompareException e) {

				}
			}
			return filtedPaiXing;
		}
		if (beiYaDianShuZu instanceof LianduiDianShuZu) {
			LianduiDianShuZu beiYaLianduiDianShuZu = (LianduiDianShuZu) beiYaDianShuZu;
			List<LianduiDianShuZu> filtedLianduiDianShuZu = filtedPaiXing.getLianduiDianShuZuList();
			List<LianduiDianShuZu> lianduiDianShuZuList = paiXing.getLianduiDianShuZuList();
			for (LianduiDianShuZu lianduiDianShuZu : lianduiDianShuZuList) {
				try {
					if (lianXuDianShuZuComparator.compare(lianduiDianShuZu, beiYaLianduiDianShuZu) > 0) {
						filtedLianduiDianShuZu.add(lianduiDianShuZu);
					}
				} catch (CanNotCompareException e) {

				}
			}
			return filtedPaiXing;
		}
		if (beiYaDianShuZu instanceof LiansanzhangDianShuZu) {
			LiansanzhangDianShuZu beiYaLiansanzhangDianShuZu = (LiansanzhangDianShuZu) beiYaDianShuZu;
			List<LiansanzhangDianShuZu> filtedLiansanzhangDianShuZu = filtedPaiXing.getLiansanzhangDianShuZuList();
			List<LiansanzhangDianShuZu> liansanzhangDianShuZuList = paiXing.getLiansanzhangDianShuZuList();
			for (LiansanzhangDianShuZu liansanzhangDianShuZu : liansanzhangDianShuZuList) {
				try {
					if (lianXuDianShuZuComparator.compare(liansanzhangDianShuZu, beiYaLiansanzhangDianShuZu) > 0) {
						filtedLiansanzhangDianShuZu.add(liansanzhangDianShuZu);
					}
				} catch (CanNotCompareException e) {

				}
			}
			return filtedPaiXing;
		}
		return filtedPaiXing;
	}

	public static Map<String, DaPaiDianShuSolution> calculateForZhadan(DianShuZu beiYaDianShuZu,
			int[] dianShuAmountArray) {
		int[] dianShuAmount = dianShuAmountArray.clone();
		Map<String, DaPaiDianShuSolution> yaPaiSolutionCandidates = new HashMap<>();
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		int xiaowangCount = dianShuAmount[13];
		int dawangCount = dianShuAmount[14];
		// 王炸
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = DianShuZuCalculator
				.generateAllWangZhadanDianShuZu(dianShuAmountArray);

		for (WangZhadanDianShuZu wangZhadanDianShuZu : wangZhadanDianShuZuList) {
			if (beiYaDianShuZu instanceof ZhadanDianShuZu) {
				ZhadanDianShuZu beiYaZhadanDianShuZu = (ZhadanDianShuZu) beiYaDianShuZu;
				if (zhadanComparator.compare(wangZhadanDianShuZu, beiYaZhadanDianShuZu) > 0) {
					DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
					solution.setDianShuZu(wangZhadanDianShuZu);
					List<DianShu> dachuDianShuList = new ArrayList<>();
					DianShu[] lianXuDianShuArray = wangZhadanDianShuZu.getDianShuZu();
					for (int count = 0; count < wangZhadanDianShuZu.getXiaowangCount(); count++) {
						dachuDianShuList.add(lianXuDianShuArray[0]);
					}
					for (int count = 0; count < wangZhadanDianShuZu.getDawangCount(); count++) {
						dachuDianShuList.add(lianXuDianShuArray[1]);
					}
					DianShu[] dachuDianShuArray = dachuDianShuList.toArray(new DianShu[dachuDianShuList.size()]);
					solution.setDachuDianShuArray(dachuDianShuArray);
					solution.calculateDianshuZuheIdx();
					solutionList.add(solution);
				}
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(wangZhadanDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				DianShu[] lianXuDianShuArray = wangZhadanDianShuZu.getDianShuZu();
				for (int count = 0; count < wangZhadanDianShuZu.getXiaowangCount(); count++) {
					dachuDianShuList.add(lianXuDianShuArray[0]);
				}
				for (int count = 0; count < wangZhadanDianShuZu.getDawangCount(); count++) {
					dachuDianShuList.add(lianXuDianShuArray[1]);
				}
				DianShu[] dachuDianShuArray = dachuDianShuList.toArray(new DianShu[dachuDianShuList.size()]);
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		int wangCount = 0;
		if (BianXingWanFa.qianbian.equals(bx)) {// 千变
			wangCount = xiaowangCount + dawangCount;
			// 减去王牌的数量
			dianShuAmount[13] = dianShuAmount[13] - xiaowangCount;
			dianShuAmount[14] = dianShuAmount[14] - dawangCount;
		} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
			wangCount = dawangCount;
			// 减去王牌的数量
			if (xiaowangCount > 0 && xiaowangCount % 2 == 0) {
				wangCount++;
				dianShuAmount[13] = dianShuAmount[13] - 2;
			}
			dianShuAmount[14] = dianShuAmount[14] - dawangCount;
		} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
			wangCount = dawangCount;
			// 减去王牌的数量
			dianShuAmount[14] = dianShuAmount[14] - dawangCount;
		} else {

		}
		if (wangCount > 0) {
			// 有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithWangDangForZhadan(wangCount, dianShuAmount,
					xiaowangCount, dawangCount, beiYaDianShuZu));
		} else {
			// 没有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithoutWangDangForZhadan(dianShuAmount, beiYaDianShuZu));
		}

		solutionList.forEach((solution) -> {
			DaPaiDianShuSolution daPaiDianShuSolution = yaPaiSolutionCandidates.get(solution.getDianshuZuheIdx());
			if (daPaiDianShuSolution != null) {
				DianShuZu dianShuZu = daPaiDianShuSolution.getDianShuZu();
				if (dianShuZu instanceof LianXuZhadanDianShuZu) {
					if (zhadanComparator.compare((ZhadanDianShuZu) solution.getDianShuZu(),
							(ZhadanDianShuZu) dianShuZu) > 0) {
						yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
					}

				}
			} else {
				yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution);
			}
		});
		return yaPaiSolutionCandidates;
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDangForZhadan(int wangCount,
			int[] dianshuCountArray, int xiaowangCount, int dawangCount, DianShuZu beiYaDianShuZu) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		// 循环王的各种当法
		int maxZuheCode = (int) Math.pow(13, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(13, wangCount - 1 - m);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			ShoupaiJiesuanPai[] wangDangPaiArray = new ShoupaiJiesuanPai[wangCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int n = 0; n < wangCount; n++) {
				int mod = modArray[n];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {// 计算王的各种当法，排除效果相同的当法
					int yu = temp % mod;
					if (BianXingWanFa.qianbian.equals(bx)) {// 千变
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, DianShu.getDianShuByOrdinal(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(DianShu.getDianShuByOrdinal(shang));
					} else {

					}
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					wangDangPaiArray = null;
					break;
				}
			}
			if (wangDangPaiArray != null) {
				// 加上当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]++;
				}
				PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
				paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
				solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithWangDang(paiXing,
						wangDangPaiArray, dianshuCountArray, bx));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return new ArrayList<>(solutionSet);
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDangForZhadan(
			int[] dianshuCountArray, DianShuZu beiYaDianShuZu) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		paiXing = paiXingFilterForZhadan(paiXing, beiYaDianShuZu);
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
		return new ArrayList<>(solutionSet);
	}

	private static PaiXing paiXingFilterForZhadan(PaiXing paiXing, DianShuZu beiYaDianShuZu) {
		PaiXing filtedPaiXing = new PaiXing();
		if (beiYaDianShuZu instanceof ZhadanDianShuZu) {
			ZhadanDianShuZu beiYaZhadanDianShuZu = (ZhadanDianShuZu) beiYaDianShuZu;
			List<DanGeZhadanDianShuZu> filtedDanGeZhadanDianShuZuList = filtedPaiXing.getZhadanDianShuZuList();
			List<DanGeZhadanDianShuZu> zhadanDianShuZuList = paiXing.getZhadanDianShuZuList();
			for (DanGeZhadanDianShuZu danGeZhadanDianShuZu : zhadanDianShuZuList) {
				if (zhadanComparator.compare(danGeZhadanDianShuZu, beiYaZhadanDianShuZu) > 0) {
					filtedDanGeZhadanDianShuZuList.add(danGeZhadanDianShuZu);
				}
			}
			List<LianXuZhadanDianShuZu> filtedLianXuZhadanDianShuZuList = filtedPaiXing.getLianXuZhadanDianShuZuList();
			List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paiXing.getLianXuZhadanDianShuZuList();
			for (LianXuZhadanDianShuZu lianXuZhadanDianShuZu : lianXuZhadanDianShuZuList) {
				if (zhadanComparator.compare(lianXuZhadanDianShuZu, beiYaZhadanDianShuZu) > 0) {
					filtedLianXuZhadanDianShuZuList.add(lianXuZhadanDianShuZu);
				}
			}
		} else {
			filtedPaiXing.setZhadanDianShuZuList(paiXing.getZhadanDianShuZuList());
			filtedPaiXing.setLianXuZhadanDianShuZuList(paiXing.getLianXuZhadanDianShuZuList());
		}
		return filtedPaiXing;
	}
}
