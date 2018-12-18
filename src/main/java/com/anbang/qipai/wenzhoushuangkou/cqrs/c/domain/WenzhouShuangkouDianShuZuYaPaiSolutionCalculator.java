package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianXuDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.CanNotCompareException;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.DanGeDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.LianXuDianShuZuComparator;
import com.dml.shuangkou.pai.dianshuzu.DianShuZuCalculator;
import com.dml.shuangkou.pai.dianshuzu.PaiXing;
import com.dml.shuangkou.pai.jiesuanpai.DawangDangPai;
import com.dml.shuangkou.pai.jiesuanpai.ShoupaiJiesuanPai;
import com.dml.shuangkou.pai.jiesuanpai.XiaowangDangPai;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;
import com.dml.shuangkou.player.action.da.solution.DianShuZuYaPaiSolutionCalculator;
import com.dml.shuangkou.wanfa.BianXingWanFa;

public class WenzhouShuangkouDianShuZuYaPaiSolutionCalculator implements DianShuZuYaPaiSolutionCalculator {
	private BianXingWanFa bx;
	private DanGeDianShuZuComparator danGeDianShuZuComparator;
	private LianXuDianShuZuComparator lianXuDianShuZuComparator;

	@Override
	public Map<String, DaPaiDianShuSolution> calculate(DianShuZu beiYaDianShuZu, int[] dianShuAmountArray) {
		int[] dianShuAmount = dianShuAmountArray.clone();
		Map<String, DaPaiDianShuSolution> yaPaiSolutionCandidates = new HashMap<>();
		Set<DaPaiDianShuSolution> solutionSet = new HashSet<>();
		// 单张
		if (beiYaDianShuZu instanceof DanzhangDianShuZu) {
			DanzhangDianShuZu beiYaDanzhangDianShuZu = (DanzhangDianShuZu) beiYaDianShuZu;
			// 大小王做单张牌打出必定是作为本身的牌的点数
			List<DanzhangDianShuZu> danzhangDianShuZuList = DianShuZuCalculator
					.calculateDanzhangDianShuZu(dianShuAmount);
			for (DanzhangDianShuZu danzhangDianShuZu : danzhangDianShuZuList) {
				try {
					if (danGeDianShuZuComparator.compare(danzhangDianShuZu, beiYaDanzhangDianShuZu) > 0) {

						DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
						solution.setDianShuZu(danzhangDianShuZu);
						DianShu[] dachuDianShuArray = { danzhangDianShuZu.getDianShu() };
						solution.setDachuDianShuArray(dachuDianShuArray);
						solution.calculateDianshuZuheIdx();
						solutionSet.add(solution);
					}
				} catch (CanNotCompareException e) {

				}
			}
			solutionSet.forEach((solution) -> yaPaiSolutionCandidates.put(solution.getDianshuZuheIdx(), solution));
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
						solutionSet.add(solution);
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
						solutionSet.add(solution);
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
						solutionSet.add(solution);
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
			calculateDaPaiDianShuSolutionWithWangDang(wangCount, dianShuAmount, xiaowangCount, dawangCount,
					beiYaDianShuZu, solutionSet);
		} else {
			// 没有王牌
			calculateDaPaiDianShuSolutionWithoutWangDang(dianShuAmount, beiYaDianShuZu, solutionSet);
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
			int dawangCount, DianShuZu beiYaDianShuZu, Set<DaPaiDianShuSolution> solutionSet) {
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
					if (beiYaDianShuZu instanceof DuiziDianShuZu) {
						// 对子
						paiXing.setDuiziDianShuZuList(DianShuZuCalculator.calculateDuiziDianShuZu(dianshuCountArray));
					}
					if (beiYaDianShuZu instanceof SanzhangDianShuZu) {
						// 三张
						paiXing.setSanzhangDianShuZuList(
								DianShuZuCalculator.calculateSanzhangDianShuZu(dianshuCountArray));
					}
					if (beiYaDianShuZu instanceof ShunziDianShuZu) {
						// 顺子
						paiXing.setShunziDianShuZuList(DianShuZuCalculator.calculateShunziDianShuZu(dianshuCountArray));
					}
					if (beiYaDianShuZu instanceof LianduiDianShuZu) {
						// 连对
						paiXing.setLianduiDianShuZuList(
								DianShuZuCalculator.calculateLianduiDianShuZu(dianshuCountArray));
					}
					if (beiYaDianShuZu instanceof LiansanzhangDianShuZu) {
						// 连三张
						paiXing.setLiansanzhangDianShuZuList(
								DianShuZuCalculator.calculateLiansanzhangDianShuZu(dianshuCountArray));
					}
					paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
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

	private void calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray, DianShuZu beiYaDianShuZu,
			Set<DaPaiDianShuSolution> solutionSet) {
		PaiXing paiXing = new PaiXing();
		if (beiYaDianShuZu instanceof DuiziDianShuZu) {
			// 对子
			paiXing.setDuiziDianShuZuList(DianShuZuCalculator.calculateDuiziDianShuZu(dianshuCountArray));
		}
		if (beiYaDianShuZu instanceof SanzhangDianShuZu) {
			// 三张
			paiXing.setSanzhangDianShuZuList(DianShuZuCalculator.calculateSanzhangDianShuZu(dianshuCountArray));
		}
		if (beiYaDianShuZu instanceof ShunziDianShuZu) {
			// 顺子
			paiXing.setShunziDianShuZuList(DianShuZuCalculator.calculateShunziDianShuZu(dianshuCountArray));
		}
		if (beiYaDianShuZu instanceof LianduiDianShuZu) {
			// 连对
			paiXing.setLianduiDianShuZuList(DianShuZuCalculator.calculateLianduiDianShuZu(dianshuCountArray));
		}
		if (beiYaDianShuZu instanceof LiansanzhangDianShuZu) {
			// 连三张
			paiXing.setLiansanzhangDianShuZuList(DianShuZuCalculator.calculateLiansanzhangDianShuZu(dianshuCountArray));
		}
		paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
		solutionSet.addAll(DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing));
	}

	private PaiXing paiXingFilter(PaiXing paiXing, DianShuZu beiYaDianShuZu) {
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

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public DanGeDianShuZuComparator getDanGeDianShuZuComparator() {
		return danGeDianShuZuComparator;
	}

	public void setDanGeDianShuZuComparator(DanGeDianShuZuComparator danGeDianShuZuComparator) {
		this.danGeDianShuZuComparator = danGeDianShuZuComparator;
	}

	public LianXuDianShuZuComparator getLianXuDianShuZuComparator() {
		return lianXuDianShuZuComparator;
	}

	public void setLianXuDianShuZuComparator(LianXuDianShuZuComparator lianXuDianShuZuComparator) {
		this.lianXuDianShuZuComparator = lianXuDianShuZuComparator;
	}

}
