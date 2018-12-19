package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouGongxianFen;
import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.WenzhouShuangkouXianshuBeishu;
import com.anbang.qipai.wenzhoushuangkou.init.XianshuCalculatorHelper;
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

	public static void main(String[] args) {
		XianshuCalculatorHelper.calculateXianshu();
		long s1 = System.currentTimeMillis();
		List<int[]> xianshuList = calculateShouPaiTotalGongxianfenForPlayer();
		long s2 = System.currentTimeMillis();
		System.out.println("计算手牌线数组合耗时：");
		System.out.println(s2 - s1 + "ms");
		WenzhouShuangkouGongxianFen fen = new WenzhouShuangkouGongxianFen();
		fen.calculateShouPaiXianshu(xianshuList);
		fen.calculate(renshu);
		long s3 = System.currentTimeMillis();
		System.out.println("计算最佳贡献分耗时：");
		System.out.println(s3 - s2 + "ms");
		int[] xianshuCountArray = new int[9];
		xianshuCountArray[0] = fen.getSixian();
		xianshuCountArray[1] = fen.getWuxian();
		xianshuCountArray[2] = fen.getLiuxian();
		xianshuCountArray[3] = fen.getQixian();
		xianshuCountArray[4] = fen.getBaxian();
		xianshuCountArray[5] = fen.getJiuxian();
		xianshuCountArray[6] = fen.getShixian();
		xianshuCountArray[7] = fen.getShiyixian();
		xianshuCountArray[8] = fen.getShierxian();
		WenzhouShuangkouXianshuBeishu beishu = new WenzhouShuangkouXianshuBeishu(xianshuCountArray);
		beishu.calculate();
		long s4 = System.currentTimeMillis();
		System.out.println("最佳线数组合：");
		for (int i = 0; i < xianshuCountArray.length; i++) {
			System.out.print(xianshuCountArray[i]);
		}
		System.out.println("");
		System.out.println("最佳倍数：");
		System.out.println(beishu.getValue());
		System.out.println("最佳线数组合得分：");
		System.out.println(fen.getValue());
		System.out.println("耗时：");
		System.out.println(s4 - s1 + "ms");
	}

	// public static void main(String[] args) {
	// long s1 = System.currentTimeMillis();
	// XianshuCalculatorHelper.calculateXianshu();
	// int[] xianshuAmountArray = new int[9];
	// xianshuAmountArray[0] = 0;
	// xianshuAmountArray[1] = 1;
	// xianshuAmountArray[2] = 0;
	// xianshuAmountArray[3] = 0;
	// xianshuAmountArray[4] = 1;
	// xianshuAmountArray[5] = 0;
	// xianshuAmountArray[6] = 1;
	// xianshuAmountArray[7] = 0;
	// xianshuAmountArray[8] = 0;
	// WenzhouShuangkouXianshuBeishu beishu = new
	// WenzhouShuangkouXianshuBeishu(xianshuAmountArray);
	// beishu.calculate();
	// WenzhouShuangkouGongxianFen fen = new
	// WenzhouShuangkouGongxianFen(xianshuAmountArray);
	// fen.calculateXianshu();
	// fen.calculate(renshu);
	// long s2 = System.currentTimeMillis();
	// System.out.println("最佳倍数：");
	// System.out.println(beishu.getValue());
	// System.out.println("最佳线数组合得分：");
	// System.out.println(fen.getValue());
	// System.out.println("耗时：");
	// System.out.println(s2 - s1 + "ms");
	// }

	private static List<int[]> calculateShouPaiTotalGongxianfenForPlayer() {
		int[] dianshuCountArray = { 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, 5, 4, 4, 2, 2 };
		List<int[]> xianshuList = new ArrayList<>();
		int xiaowangCount = dianshuCountArray[13];
		int dawangCount = dianshuCountArray[14];
		if (xiaowangCount + dawangCount == 4) {// 有天王炸
			List<int[]> xianshuList1 = calculatePaiXingWithWangDang(1, dianshuCountArray, xiaowangCount, dawangCount);
			for (int[] xianshuCount1 : xianshuList1) {
				xianshuCount1[2] += 1;
			}
			xianshuList.addAll(xianshuList1);
			List<int[]> xianshuList2 = calculatePaiXingWithoutWangDang(dianshuCountArray);
			for (int[] xianshuCount2 : xianshuList2) {
				xianshuCount2[3] += 1;
			}
			xianshuList.addAll(xianshuList2);
		}
		if (xiaowangCount + dawangCount == 3) {// 有三王炸
			List<int[]> xianshuList3 = calculatePaiXingWithoutWangDang(dianshuCountArray);
			for (int[] xianshuCount3 : xianshuList3) {
				xianshuCount3[2] += 1;
			}
			xianshuList.addAll(xianshuList3);
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
			xianshuList.addAll(calculatePaiXingWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount));
		} else {
			// 没有王牌
			xianshuList.addAll(calculatePaiXingWithoutWangDang(dianshuCountArray));
		}
		return xianshuList;
	}

	private static List<int[]> calculatePaiXingWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = new PaiXing();
		// 普通炸弹
		paiXing.setDanGeZhadanDianShuZuList(DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray));
		// 连续炸弹
		paiXing.setLianXuZhadanDianShuZuList(DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray));
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

	private static List<int[]> calculatePaiXingWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount) {
		List<int[]> xianshuList = new ArrayList<>();
		// 计算王可以当哪些牌，提高性能
		List<DianShu> kedangDianShuList = verifyDangFa(dawangCount, dianshuCountArray);
		// 循环王的各种当法
		if (kedangDianShuList.isEmpty()) {
			return xianshuList;
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
				PaiXing paiXing = new PaiXing();
				// 普通炸弹
				paiXing.setDanGeZhadanDianShuZuList(
						DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray));
				// 连续炸弹
				paiXing.setLianXuZhadanDianShuZuList(
						DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray));
				xianshuList.addAll(calculateBestGongxianfen(dianshuCountArray.clone(), paiXing));
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return xianshuList;
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
			List<ZhadanDianShuZu> zhadanDianShuZuList, List<int[]> xianshuList) {
		if (zhadanDianShuZuList.isEmpty()) {
			xianshuList.add(xianshuArray);
		} else {
			for (int i = 0; i < zhadanDianShuZuList.size(); i++) {
				int[] xianshuArray1 = xianshuArray.clone();
				ZhadanDianShuZu zhadanDianShuZu = zhadanDianShuZuList.get(i);
				int xianshu = calculateXianShu(zhadanDianShuZu);
				xianshuArray1[xianshu - 4]++;
				int[] dianshuCountArray1 = removeZhadan(dianshuCountArray.clone(), zhadanDianShuZu);
				List<ZhadanDianShuZu> zhadanDianShuZuList1 = calculateTotalGongxianfenForDianshuCountArray(
						dianshuCountArray1);
				calculateGongxianfen(dianshuCountArray1, xianshuArray1, zhadanDianShuZuList1, xianshuList);
			}
		}
	}

	/**
	 * 根据牌型和牌数计算最佳贡献分
	 */
	private static List<int[]> calculateBestGongxianfen(int[] dianshuCountArray, PaiXing paixing) {
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getDanGeZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);

		List<int[]> xianshuList = new ArrayList<>();
		int[] xianshuArray = new int[9];
		calculateGongxianfen(dianshuCountArray, xianshuArray, zhadanDianShuZuList, xianshuList);

		return xianshuList;
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
		PaiXing paiXing = new PaiXing();
		// 普通炸弹
		paiXing.setDanGeZhadanDianShuZuList(DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray));
		// 连续炸弹
		paiXing.setLianXuZhadanDianShuZuList(DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray));
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paiXing.getDanGeZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paiXing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
		return zhadanDianShuZuList;
	}

	private static List<ZhadanDianShuZu> calculateZhadanDianShuZuWithWangDang(int wangCount, int[] dianshuCountArray,
			int xiaowangCount, int dawangCount) {
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		// 计算王可以当哪些牌，提高性能
		List<DianShu> kedangDianShuList = verifyDangFa(dawangCount, dianshuCountArray);
		// 循环王的各种当法
		if (kedangDianShuList.isEmpty()) {
			return zhadanDianShuZuList;
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
				PaiXing paiXing = new PaiXing();
				// 普通炸弹
				paiXing.setDanGeZhadanDianShuZuList(
						DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray));
				// 连续炸弹
				paiXing.setLianXuZhadanDianShuZuList(
						DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray));
				List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paiXing.getDanGeZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
				List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paiXing.getLianXuZhadanDianShuZuList();
				zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
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
