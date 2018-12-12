package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouGongxianFen;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouXianshuBeishu;
import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.shuangkou.pai.dianshuzu.DianShuZuCalculator;
import com.dml.shuangkou.pai.dianshuzu.LianXuZhadanDianShuZu;
import com.dml.shuangkou.pai.dianshuzu.PaiXing;
import com.dml.shuangkou.pai.dianshuzu.WangZhadanDianShuZu;
import com.dml.shuangkou.pai.jiesuanpai.DawangDangPai;
import com.dml.shuangkou.pai.jiesuanpai.ShoupaiJiesuanPai;
import com.dml.shuangkou.pai.jiesuanpai.XiaowangDangPai;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class CaseTest1 {
	private static int renshu = 4;
	private static BianXingWanFa bx = BianXingWanFa.qianbian;

	// public static void main(String[] args) {
	// long s1 = System.currentTimeMillis();
	// int[] xianshuCountArray = calculateShouPaiTotalGongxianfenForPlayer();
	// System.out.println("最佳线数组合：");
	// for (int i = 0; i < xianshuCountArray.length; i++) {
	// System.out.print(xianshuCountArray[i]);
	// }
	// WenzhouShuangkouGongxianFen fen = new
	// WenzhouShuangkouGongxianFen(xianshuCountArray);
	// fen.calculateXianshu();
	// fen.calculate(renshu);
	// WenzhouShuangkouXianshuBeishu beishu = new
	// WenzhouShuangkouXianshuBeishu(xianshuCountArray);
	// beishu.calculateXianshu();
	// beishu.calculate();
	// long s2 = System.currentTimeMillis();
	// System.out.println("");
	// System.out.println("最佳倍数：");
	// System.out.println(beishu.getValue());
	// System.out.println("最佳线数组合得分：");
	// System.out.println(fen.getValue());
	// System.out.println("耗时：");
	// System.out.println(s2 - s1 + "ms");
	// }

	public static void main(String[] args) {
		long s1 = System.currentTimeMillis();
		int[] xianshuAmountArray = new int[9];
		xianshuAmountArray[0] = 0;
		xianshuAmountArray[1] = 3;
		xianshuAmountArray[2] = 1;
		xianshuAmountArray[3] = 1;
		xianshuAmountArray[4] = 0;
		xianshuAmountArray[5] = 0;
		xianshuAmountArray[6] = 0;
		xianshuAmountArray[7] = 0;
		xianshuAmountArray[8] = 0;
		WenzhouShuangkouXianshuBeishu beishu = new WenzhouShuangkouXianshuBeishu(xianshuAmountArray);
		beishu.calculateXianshu();
		beishu.calculate();
		WenzhouShuangkouGongxianFen fen = new WenzhouShuangkouGongxianFen(xianshuAmountArray);
		fen.calculateXianshu();
		fen.calculate(renshu);
		long s2 = System.currentTimeMillis();
		System.out.println("最佳倍数：");
		System.out.println(beishu.getValue());
		System.out.println("最佳线数组合得分：");
		System.out.println(fen.getValue());
		System.out.println("耗时：");
		System.out.println(s2 - s1 + "ms");
	}

	private static int[] calculateShouPaiTotalGongxianfenForPlayer() {

		int[] dianshuCountArray = { 2, 0, 0, 0, 4, 0, 8, 0, 5, 0, 0, 6, 0, 1, 1 };

		int xiaowangCount = dianshuCountArray[13];
		int dawangCount = dianshuCountArray[14];
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
			return calculatePaiXingWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount);
		} else {
			// 没有王牌
			return calculatePaiXingWithoutWangDang(dianshuCountArray);
		}
	}

	private static int[] calculatePaiXingWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		return calculateBestGongxianfen(dianshuCountArray.clone(), paiXing);
	}

	private static List<DianShu> verifyDangFa(int wangCount, int[] dianshuCountArray) {
		List<DianShu> kedangDianShuList = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			if (dianshuCountArray[i] + wangCount >= 4) {
				kedangDianShuList.add(DianShu.getDianShuByOrdinal(i));
			}
		}
		return kedangDianShuList;
	}

	private static int[] calculatePaiXingWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount) {
		int bestScore = 0;
		int[] bestXianshuCount = new int[9];
		// 计算王可以当哪些牌，提高性能
		List<DianShu> kedangDianShuList = verifyDangFa(dawangCount, dianshuCountArray);
		// 循环王的各种当法
		if (kedangDianShuList.isEmpty()) {
			return bestXianshuCount;
		}
		int size = kedangDianShuList.size();
		int maxZuheCode = (int) Math.pow(size, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(size, wangCount - 1 - m);
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
							wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(1, kedangDianShuList.get(shang));
						}
					} else if (BianXingWanFa.banqianbian.equals(bx)) {// 半千变;
						if (n < dawangCount) {
							wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
						} else {
							wangDangPaiArray[n] = new XiaowangDangPai(2, kedangDianShuList.get(shang));
						}
					} else if (BianXingWanFa.baibian.equals(bx)) {// 百变
						wangDangPaiArray[n] = new DawangDangPai(kedangDianShuList.get(shang));
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

				PaiXing paixing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
				int[] xianshuCount = calculateBestGongxianfen(dianshuCountArray.clone(), paixing);
				WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
				gongxianfen.calculateXianshu();
				gongxianfen.calculate(renshu);
				int score = gongxianfen.getValue();
				if (score > bestScore) {
					bestScore = score;
					bestXianshuCount = xianshuCount;
				}
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return bestXianshuCount;
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

	private static void calculateGongxianfen(int[] dianshuCountArray, int[] xianshuArray,
			List<ZhadanDianShuZu> zhadanDianShuZuList, Map<Integer, int[]> scoreXianshuMap) {
		if (zhadanDianShuZuList.isEmpty()) {
			WenzhouShuangkouGongxianFen gongxianfen = new WenzhouShuangkouGongxianFen(xianshuArray);
			gongxianfen.calculateXianshu();
			gongxianfen.calculate(renshu);
			int score = gongxianfen.getValue();
			scoreXianshuMap.put(score, xianshuArray);
		} else {
			for (int i = 0; i < zhadanDianShuZuList.size(); i++) {
				int[] xianshuArray1 = xianshuArray.clone();
				ZhadanDianShuZu zhadanDianShuZu = zhadanDianShuZuList.get(i);
				int xianshu = calculateXianShu(zhadanDianShuZu);
				xianshuArray1[xianshu - 4]++;
				int[] dianshuCountArray1 = removeZhadan(dianshuCountArray.clone(), zhadanDianShuZu);
				List<ZhadanDianShuZu> zhadanDianShuZuList1 = calculateTotalGongxianfenForDianshuCountArray(
						dianshuCountArray1);
				calculateGongxianfen(dianshuCountArray1, xianshuArray1, zhadanDianShuZuList1, scoreXianshuMap);
			}
		}
	}

	/**
	 * 根据牌型和牌数计算最佳贡献分
	 */
	private static int[] calculateBestGongxianfen(int[] dianshuCountArray, PaiXing paixing) {
		int bestScore = 0;
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = paixing.getWangZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(wangZhadanDianShuZuList);

		Map<Integer, int[]> scoreXianshuMap = new HashMap<>();
		int[] xianshuArray = new int[9];
		calculateGongxianfen(dianshuCountArray, xianshuArray, zhadanDianShuZuList, scoreXianshuMap);

		for (int score : scoreXianshuMap.keySet()) {
			if (score > bestScore) {
				bestScore = score;
			}
		}
		return scoreXianshuMap.get(bestScore);
	}

	private static List<ZhadanDianShuZu> calculateTotalGongxianfenForDianshuCountArray(int[] dianshuCountArray) {
		int xiaowangCount = dianshuCountArray[13];
		int dawangCount = dianshuCountArray[14];
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
			return calculateZhadanDianShuZuWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount);
		} else {
			// 没有王牌
			return calculateListZhadanDianShuZuWithoutWangDang(dianshuCountArray);
		}
	}

	private static List<ZhadanDianShuZu> calculateListZhadanDianShuZuWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paixing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = paixing.getWangZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(wangZhadanDianShuZuList);
		return zhadanDianShuZuList;
	}

	private static List<ZhadanDianShuZu> calculateZhadanDianShuZuWithWangDang(int wangCount, int[] dianshuCountArray,
			int xiaowangCount, int dawangCount) {
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
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
				PaiXing paixing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
				List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
				List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
				List<WangZhadanDianShuZu> wangZhadanDianShuZuList = paixing.getWangZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(wangZhadanDianShuZuList);
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return zhadanDianShuZuList;
	}

	/**
	 * 从手牌中移除打出的炸弹
	 */
	private static int[] removeZhadan(int[] dianshuCountArray, ZhadanDianShuZu zhadanDianShuZu) {
		if (zhadanDianShuZu instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanDianShuZu;
			dianshuCountArray[danGeZhadanDianShuZu.getDianShu().ordinal()] -= danGeZhadanDianShuZu.getSize();
		}
		if (zhadanDianShuZu instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) zhadanDianShuZu;
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
			for (int i = 0; i < lianXuDianShuArray.length; i++) {
				dianshuCountArray[lianXuDianShuArray[i].ordinal()] -= lianXuDianShuSizeArray[i];
			}
		}
		if (zhadanDianShuZu instanceof WangZhadanDianShuZu) {
			WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) zhadanDianShuZu;
			dianshuCountArray[13] -= wangZhadanDianShuZu.getXiaowangCount();
			dianshuCountArray[14] -= wangZhadanDianShuZu.getDawangCount();
		}
		return dianshuCountArray;
	}
}
