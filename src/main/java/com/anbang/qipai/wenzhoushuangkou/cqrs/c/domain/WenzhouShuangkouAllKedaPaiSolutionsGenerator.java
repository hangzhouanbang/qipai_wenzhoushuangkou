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
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
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

	private List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDang(int wangCount, int[] dianshuCountArray,
			int xiaowangCount, int dawangCount) {
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

	private List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray) {
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
		return new ArrayList<>(solutionSet);
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
