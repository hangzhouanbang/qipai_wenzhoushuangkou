package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.puke.pai.DianShu;
import com.dml.puke.pai.PukePai;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZuGenerator;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianXuDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.CanNotCompareException;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.LianXuDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.ZhadanComparator;
import com.dml.shuangkou.pai.dianshuzu.DianShuZuCalculator;
import com.dml.shuangkou.pai.dianshuzu.LianXuZhadanDianShuZu;
import com.dml.shuangkou.pai.dianshuzu.PaiXing;
import com.dml.shuangkou.pai.dianshuzu.WangZhadanDianShuZu;
import com.dml.shuangkou.pai.jiesuanpai.DawangDangPai;
import com.dml.shuangkou.pai.jiesuanpai.ShoupaiJiesuanPai;
import com.dml.shuangkou.pai.jiesuanpai.XiaowangDangPai;
import com.dml.shuangkou.player.action.da.AllKedaPaiSolutionsGenerator;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class WenzhouShuangkouAllKedaPaiSolutionsGenerator implements AllKedaPaiSolutionsGenerator {

	private BianXingWanFa bx;

	private ZhadanComparator zhadanComparator;

	private LianXuDianShuZuComparator lianXuDianShuZuComparator;

	@Override
	public Map<String, DaPaiDianShuSolution> generateAllKedaPaiSolutions(Map<Integer, PukePai> allShoupai) {
		Map<String, DaPaiDianShuSolution> yaPaiSolutionCandidates = new HashMap<>();
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
		// 大小王做单张牌打出必定是作为本身的牌的点数
		List<DanzhangDianShuZu> danzhangDianShuZuList = DianShuZuGenerator
				.generateAllDanzhangDianShuZu(dianshuCountArray);
		solutionSet.addAll(DianShuZuCalculator.generateAllDanzhangDaPaiDianShuSolution(danzhangDianShuZuList));

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
			solutionSet.add(solution);
		}
		if (dawangCount > 1) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(new DuiziDianShuZu(DianShu.dawang));
			DianShu[] dachuDianShuArray = { DianShu.dawang, DianShu.dawang };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionSet.add(solution);
		}
		if (xiaowangCount > 1) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(new DuiziDianShuZu(DianShu.xiaowang));
			DianShu[] dachuDianShuArray = { DianShu.xiaowang, DianShu.xiaowang };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionSet.add(solution);
		}
		// 王炸
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = DianShuZuCalculator
				.calculateWangZhadanDianShuZu(dianshuCountArray);
		solutionSet.addAll(DianShuZuCalculator.generateAllWangZhadanDianShuZu(wangZhadanDianShuZuList));

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
			calculateDaPaiDianShuSolutionWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount,
					solutionSet);
		} else {
			// 没有王牌
			calculateDaPaiDianShuSolutionWithoutWangDang(dianshuCountArray, solutionSet);
		}
		solutionSet.forEach((solution) -> {
			DaPaiDianShuSolution daPaiDianShuSolution = yaPaiSolutionCandidates.get(solution.getDianshuZuheIdx());
			if (daPaiDianShuSolution != null) {
				DianShuZu dianShuZu = daPaiDianShuSolution.getDianShuZu();
				// 有可能出现打出点数相同类型却不同的情况
				if (!solution.getDianShuZu().getClass().equals(dianShuZu.getClass())) {

				} else if (dianShuZu instanceof LianXuDianShuZu) {
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

	private List<DianShu> verifyDangFa(int wangCount, int[] dianshuCountArray) {
		Set<DianShu> kedangDianShuSet = new HashSet<>();
		for (int i = 0; i < 13; i++) {
			if (dianshuCountArray[i] > 0) {
				kedangDianShuSet.add(DianShu.getDianShuByOrdinal(i));
				if (i - 1 >= 0) {
					kedangDianShuSet.add(DianShu.getDianShuByOrdinal(i - 1));
				}
				if (i + 1 < 13) {
					kedangDianShuSet.add(DianShu.getDianShuByOrdinal(i + 1));
				}
			}
		}
		return new ArrayList<>(kedangDianShuSet);
	}

	private void calculateDaPaiDianShuSolutionWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount, Set<DaPaiDianShuSolution> solutionSet) {
		// 计算可以当的牌，提高性能
		List<DianShu> kedangDianShuList = verifyDangFa(dawangCount, dianshuCountArray);
		if (!kedangDianShuList.isEmpty()) {
			// 循环王的各种当法
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
					// 对子
					paiXing.setDuiziDianShuZuList(DianShuZuCalculator.calculateDuiziDianShuZu(dianshuCountArray));
					// 三张
					paiXing.setSanzhangDianShuZuList(DianShuZuCalculator.calculateSanzhangDianShuZu(dianshuCountArray));
					// 顺子
					paiXing.setShunziDianShuZuList(DianShuZuCalculator.calculateShunziDianShuZu(dianshuCountArray));
					// 连对
					paiXing.setLianduiDianShuZuList(DianShuZuCalculator.calculateLianduiDianShuZu(dianshuCountArray));
					// 连三张
					paiXing.setLiansanzhangDianShuZuList(
							DianShuZuCalculator.calculateLiansanzhangDianShuZu(dianshuCountArray));
					// 普通炸弹
					paiXing.setDanGeZhadanDianShuZuList(
							DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray));
					// 连续炸弹
					paiXing.setLianXuZhadanDianShuZuList(
							DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray));
					solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithWangDang(paiXing,
							wangDangPaiArray, dianshuCountArray, bx));
					// 减去当牌的数量
					for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
						dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
					}
				}
			}
		}
	}

	private void calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray,
			Set<DaPaiDianShuSolution> solutionSet) {
		PaiXing paiXing = new PaiXing();
		// 对子
		paiXing.setDuiziDianShuZuList(DianShuZuCalculator.calculateDuiziDianShuZu(dianshuCountArray));
		// 三张
		paiXing.setSanzhangDianShuZuList(DianShuZuCalculator.calculateSanzhangDianShuZu(dianshuCountArray));
		// 顺子
		paiXing.setShunziDianShuZuList(DianShuZuCalculator.calculateShunziDianShuZu(dianshuCountArray));
		// 连对
		paiXing.setLianduiDianShuZuList(DianShuZuCalculator.calculateLianduiDianShuZu(dianshuCountArray));
		// 连三张
		paiXing.setLiansanzhangDianShuZuList(DianShuZuCalculator.calculateLiansanzhangDianShuZu(dianshuCountArray));
		// 普通炸弹
		paiXing.setDanGeZhadanDianShuZuList(DianShuZuCalculator.calculateDanGeZhadanDianShuZu(dianshuCountArray));
		// 连续炸弹
		paiXing.setLianXuZhadanDianShuZuList(DianShuZuCalculator.calculateLianXuZhadanDianShuZu(dianshuCountArray));
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public ZhadanComparator getZhadanComparator() {
		return zhadanComparator;
	}

	public void setZhadanComparator(ZhadanComparator zhadanComparator) {
		this.zhadanComparator = zhadanComparator;
	}

	public LianXuDianShuZuComparator getLianXuDianShuZuComparator() {
		return lianXuDianShuZuComparator;
	}

	public void setLianXuDianShuZuComparator(LianXuDianShuZuComparator lianXuDianShuZuComparator) {
		this.lianXuDianShuZuComparator = lianXuDianShuZuComparator;
	}

}
