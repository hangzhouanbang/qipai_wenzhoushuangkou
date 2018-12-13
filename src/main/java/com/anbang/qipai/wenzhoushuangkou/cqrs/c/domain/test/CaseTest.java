package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouZhadanComparator;
import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZuGenerator;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
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
	private static BianXingWanFa bx = BianXingWanFa.qianbian;
	private static ZhadanComparator zhadanComparator = new WenzhouShuangkouZhadanComparator();
	private static DanGeDianShuZuComparator danGeDianShuZuComparator = new NoZhadanDanGeDianShuZuComparator();
	private static LianXuDianShuZuComparator lianXuDianShuZuComparator = new TongDengLianXuDianShuZuComparator();

	// 炸弹压牌
	// public static void main(String[] args) {
	// DianShuZu beiYaDianShuZu = new DanzhangDianShuZu(DianShu.A);
	// int[] dianShuAmountArray = { 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0 };
	// List<DaPaiDianShuSolution> solutionList = calculateZhadan(beiYaDianShuZu,
	// dianShuAmountArray);
	// solutionList = filter(solutionList);
	// System.out.println(solutionList);
	// }

	// 普通压牌
	// public static void main(String[] args) {
	// DianShuZu beiYaDianShuZu = new DuiziDianShuZu(DianShu.shi);
	// int[] dianShuAmountArray = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 };
	// List<DaPaiDianShuSolution> solutionList = calculate(beiYaDianShuZu,
	// dianShuAmountArray);
	// // solutionList.addAll(calculateZhadan(beiYaDianShuZu, dianShuAmountArray));
	// solutionList = filter(solutionList);
	// System.out.println(solutionList);
	// }

	// 所有可打的牌
	public static void main(String[] args) {
		int[] dianShuAmountArray = { 0, 0, 0, 0, 0, 2, 0, 1, 2, 0, 0, 0, 0, 1, 2 };
		long s1 = System.currentTimeMillis();
		List<DaPaiDianShuSolution> solutionList = generateAllKedaPaiSolutions(dianShuAmountArray);
		solutionList = filter(solutionList);
		long s2 = System.currentTimeMillis();
		System.out.println(s2 - s1);
	}

	public static List<DaPaiDianShuSolution> calculateZhadan(DianShuZu beiYaDianShuZu, int[] dianShuAmountArray) {
		int[] dianShuAmount = dianShuAmountArray.clone();
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		int xiaowangCount = dianShuAmount[13];
		int dawangCount = dianShuAmount[14];
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
			solutionList.addAll(calculateDaPaiDianShuSolutionWithWangDangZhadan(wangCount, dianShuAmount, xiaowangCount,
					dawangCount, beiYaDianShuZu));
		} else {
			// 没有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithoutWangDangZhadan(dianShuAmount, beiYaDianShuZu));
		}
		return solutionList;
	}

	public static List<DaPaiDianShuSolution> filter(List<DaPaiDianShuSolution> YaPaiSolutions) {
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
				if (maxDanGeZhadanSolution == null || zhadanComparator.compare(danGeZhadanDianShuZu1,
						(ZhadanDianShuZu) maxDanGeZhadanSolution.getDianShuZu()) > 0) {
					maxDanGeZhadanSolution = solution;
				}
			}
		}
		if (maxDanGeZhadanSolution != null) {
			if (!zhadanSolutionList.isEmpty()) {
				DaPaiDianShuSolution solution = zhadanSolutionList.getLast();
				if (!solution.getDianshuZuheIdx().equals(maxDanGeZhadanSolution.getDianshuZuheIdx())) {
					zhadanSolutionList.add(maxDanGeZhadanSolution);
				}
			} else {
				zhadanSolutionList.add(maxDanGeZhadanSolution);
			}
		}
		List<DaPaiDianShuSolution> noWangDanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangDuiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangShunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangLianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangLiansanzhangSolutionList = new LinkedList<>();
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
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
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
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
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : noWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
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
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
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
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
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
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
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
		List<DaPaiDianShuSolution> hasWangDanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangDuiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangSanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangShunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangLianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangLiansanzhangSolutionList = new LinkedList<>();
		f2: for (DaPaiDianShuSolution solution : hasWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu1 = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu1.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
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
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : hasWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : noWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu1 = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu1.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
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
				for (DaPaiDianShuSolution sanzhangSolution : hasWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : hasWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : noWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
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
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
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
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
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
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
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

	private static int calculateXianShuZhandan(ZhadanDianShuZu zhadan) {
		if (zhadan instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadan = (DanGeZhadanDianShuZu) zhadan;
			return danGeZhadan.getSize();
		} else if (zhadan instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadan = (LianXuZhadanDianShuZu) zhadan;
			return lianXuZhadan.getXianShu();
		} else {
			WangZhadanDianShuZu wangZhadan = (WangZhadanDianShuZu) zhadan;
			int xiaowangCount = wangZhadan.getXiaowangCount();
			int dawangCount = wangZhadan.getDawangCount();
			if (xiaowangCount + dawangCount == 4) {
				return 7;
			} else {
				return 6;
			}
		}
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDangZhadan(int wangCount,
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
				paiXing = paiXingFilterZhadan(paiXing, beiYaDianShuZu);
				solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithWangDang(paiXing,
						wangDangPaiArray, dianshuCountArray));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return new ArrayList<>(solutionSet);
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDangZhadan(
			int[] dianshuCountArray, DianShuZu beiYaDianShuZu) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		paiXing = paiXingFilterZhadan(paiXing, beiYaDianShuZu);
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
		return new ArrayList<>(solutionSet);
	}

	private static PaiXing paiXingFilterZhadan(PaiXing paiXing, DianShuZu beiYaDianShuZu) {
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
			List<WangZhadanDianShuZu> filtedWangZhadanDianShuZuList = filtedPaiXing.getWangZhadanDianShuZuList();
			List<WangZhadanDianShuZu> wangZhadanDianShuZuList = paiXing.getWangZhadanDianShuZuList();
			for (WangZhadanDianShuZu wangZhadanDianShuZu : wangZhadanDianShuZuList) {
				if (zhadanComparator.compare(wangZhadanDianShuZu, beiYaZhadanDianShuZu) > 0) {
					filtedWangZhadanDianShuZuList.add(wangZhadanDianShuZu);
				}
			}
		} else {
			filtedPaiXing.setZhadanDianShuZuList(paiXing.getZhadanDianShuZuList());
			filtedPaiXing.setLianXuZhadanDianShuZuList(paiXing.getLianXuZhadanDianShuZuList());
			filtedPaiXing.setWangZhadanDianShuZuList(paiXing.getWangZhadanDianShuZuList());
		}
		return filtedPaiXing;
	}

	public static List<DaPaiDianShuSolution> calculate(DianShuZu beiYaDianShuZu, int[] dianShuAmountArray) {
		int[] dianShuAmount = dianShuAmountArray.clone();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return solutionList;
		}
		int xiaowangCount = dianShuAmount[13];
		int dawangCount = dianShuAmount[14];
		// 对子，当有大小王个一张时可以当一对大王打出
		if (beiYaDianShuZu instanceof DuiziDianShuZu && xiaowangCount > 0 && dawangCount > 0) {
			DuiziDianShuZu beiYaDuiziDianShuZu = (DuiziDianShuZu) beiYaDianShuZu;
			DuiziDianShuZu duiziDianShuZu = new DuiziDianShuZu(DianShu.dawang);
			try {
				if (danGeDianShuZuComparator.compare(duiziDianShuZu, beiYaDuiziDianShuZu) > 0) {
					DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
					solution.setDianShuZu(new DuiziDianShuZu(DianShu.dawang));
					DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.dawang };
					solution.setDachuDianShuArray(dachuDianShuArray);
					solution.calculateDianshuZuheIdx();
					solutionList.add(solution);
				}
			} catch (CanNotCompareException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDang(int wangCount,
			int[] dianshuCountArray, int xiaowangCount, int dawangCount, DianShuZu beiYaDianShuZu) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
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
				solutionList.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithWangDang(paiXing,
						wangDangPaiArray, dianshuCountArray));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray,
			DianShuZu beiYaDianShuZu) {
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
		return DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return filtedPaiXing;
		}
		return filtedPaiXing;
	}

	public static List<DaPaiDianShuSolution> generateAllKedaPaiSolutions(int[] dianshuCountArray) {
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
		// 当有大小王个一张时可以当一对大王打出
		if (xiaowangCount > 0 && dawangCount > 0) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(new DuiziDianShuZu(DianShu.dawang));
			DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.dawang };
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
		return solutionList;
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
						wangDangPaiArray, dianshuCountArray));
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
}
