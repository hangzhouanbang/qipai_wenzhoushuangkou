package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import com.dml.shuangkou.BianXingWanFa;
import com.dml.shuangkou.ShoupaiJiesuanPai;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;

public class CaseTest {
	private static BianXingWanFa bx = BianXingWanFa.qianbian;
	private static ZhadanComparator zhadanComparator = new WenzhouShuangkouZhadanComparator();

	public static void main(String[] args) {
		DianShuZu beiYaDianShuZu = new DanzhangDianShuZu(DianShu.liu);
		int[] dianShuAmountArray = { 0, 0, 5, 7, 3, 0, 3, 0, 0, 4, 0, 1, 0, 2, 1 };
		List<DaPaiDianShuSolution> solutionList = calculate(beiYaDianShuZu, dianShuAmountArray);
		// for (DaPaiDianShuSolution solution : solutionList) {
		// DianShuZu dianShuZu = solution.getDianShuZu();
		// if (dianShuZu instanceof ZhadanDianShuZu) {
		// int xianshu = calculateXianShu((ZhadanDianShuZu) dianShuZu);
		// // System.out.println(xianshu);
		// }
		// }
		solutionList = filter(solutionList);
		System.out.println(solutionList);
	}

	public static List<DaPaiDianShuSolution> calculate(DianShuZu beiYaDianShuZu, int[] dianShuAmountArray) {
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
			solutionList.addAll(calculateDaPaiDianShuSolutionWithWangDang(wangCount, dianShuAmount, xiaowangCount,
					dawangCount, beiYaDianShuZu));
		} else {
			// 没有王牌
			solutionList.addAll(calculateDaPaiDianShuSolutionWithoutWangDang(dianShuAmount, beiYaDianShuZu));
		}
		return solutionList;
	}

	public static List<DaPaiDianShuSolution> filter(List<DaPaiDianShuSolution> YaPaiSolutions) {
		List<DaPaiDianShuSolution> filtedSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSolutionList = new ArrayList<>();
		Set<String> dianshuZuSet = new HashSet<>();
		f1: for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (!(dianshuZu instanceof ZhadanDianShuZu)) {
				dianshuZuSet.add(dianshuZu.getClass().getName());
			}
			for (DianShu dianshu : solution.getDachuDianShuArray()) {
				if (DianShu.xiaowang.equals(dianshu) || DianShu.dawang.equals(dianshu)) {
					continue f1;
				}
			}
			noWangSolutionList.add(solution);
		}
		if (dianshuZuSet.size() > 1) {
			return YaPaiSolutions;
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

	private static int calculateXianShu(ZhadanDianShuZu zhadan) {
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
						wangDangPaiArray, dianshuCountArray));
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
}
