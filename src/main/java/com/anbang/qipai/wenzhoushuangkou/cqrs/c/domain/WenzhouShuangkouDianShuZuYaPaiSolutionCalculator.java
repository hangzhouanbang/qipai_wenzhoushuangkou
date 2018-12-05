package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZuGenerator;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.CanNotCompareException;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.DanGeDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.LianXuDianShuZuComparator;
import com.dml.shuangkou.BianXingWanFa;
import com.dml.shuangkou.ShoupaiJiesuanPai;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;
import com.dml.shuangkou.player.action.da.solution.DianShuZuYaPaiSolutionCalculator;

public class WenzhouShuangkouDianShuZuYaPaiSolutionCalculator implements DianShuZuYaPaiSolutionCalculator {
	private BianXingWanFa bx;
	private DanGeDianShuZuComparator danGeDianShuZuComparator;
	private LianXuDianShuZuComparator lianXuDianShuZuComparator;

	@Override
	public List<DaPaiDianShuSolution> calculate(DianShuZu beiYaDianShuZu, int[] dianShuAmountArray) {
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

	private List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithWangDang(int wangCount, int[] dianshuCountArray,
			int xiaowangCount, int dawangCount, DianShuZu beiYaDianShuZu) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		// 循环王的各种当法
		int maxZuheCode = (int) Math.pow(15, wangCount);
		int[] modArray = new int[wangCount];
		for (int m = 0; m < wangCount; m++) {
			modArray[m] = (int) Math.pow(15, wangCount - 1 - m);
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

	private List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray,
			DianShuZu beiYaDianShuZu) {
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		paiXing = paiXingFilter(paiXing, beiYaDianShuZu);
		return DianShuZuCalculator.calculateAllDaPaiDianShuSolutionWithoutWangDang(paiXing);
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
