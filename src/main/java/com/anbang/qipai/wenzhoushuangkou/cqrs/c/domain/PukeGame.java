package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain.listener.XianshuCountDaActionStatisticsListener;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;
import com.dml.puke.pai.DianShu;
import com.dml.puke.pai.PukePai;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.NoZhadanDanGeDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.TongDengLianXuDianShuZuComparator;
import com.dml.shuangkou.BianXingWanFa;
import com.dml.shuangkou.ShoupaiJiesuanPai;
import com.dml.shuangkou.gameprocess.FixedPanNumbersJuFinishiDeterminer;
import com.dml.shuangkou.gameprocess.OnePlayerHasPaiPanFinishiDeterminer;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pan.Pan;
import com.dml.shuangkou.pan.PanActionFrame;
import com.dml.shuangkou.pan.PanResult;
import com.dml.shuangkou.player.ShuangkouPlayer;
import com.dml.shuangkou.preparedapai.avaliablepai.DoubleAvaliablePaiFiller;
import com.dml.shuangkou.preparedapai.fapai.YiciJiuzhangFapaiStrategy;
import com.dml.shuangkou.preparedapai.fapai.YiciSanzhangFapaiStrategy;
import com.dml.shuangkou.preparedapai.fapai.YiliuSanqiFapaiStrategy;
import com.dml.shuangkou.preparedapai.fapai.YisanSiliuFapaiStrategy;
import com.dml.shuangkou.preparedapai.lipai.DianshuOrPaishuShoupaiSortStrategy;
import com.dml.shuangkou.preparedapai.luanpai.BazhangHasSiwangLuanpaiStrategy;
import com.dml.shuangkou.preparedapai.luanpai.ErliuhunHasSiwangLuanpaiStrategy;
import com.dml.shuangkou.preparedapai.luanpai.LastPanChuPaiOrdinalLuanpaiStrategy;
import com.dml.shuangkou.preparedapai.luanpai.SanwuHasSiwangLuanpaiStrategy;
import com.dml.shuangkou.preparedapai.xianda.DongPositionXiandaPlayerDeterminer;
import com.dml.shuangkou.preparedapai.zudui.HongxinbaHongxinjiuZuduiStrategy;

public class PukeGame extends FixedPlayersMultipanAndVotetofinishGame {
	private int panshu;
	private int renshu;
	private BianXingWanFa bx;
	private boolean chaodi;
	private boolean shuangming;
	private boolean fengding;
	private ChaPai chapai;
	private FaPai fapai;
	private Ju ju;
	private Map<String, Integer> playerTotalScoreMap = new HashMap<>();
	private Map<String, Integer> playerGongxianfenMap = new HashMap<>();
	private Map<String, Integer> playerTotalGongxianfenMap = new HashMap<>();
	private Map<String, Integer> playerMaxXianshuMap = new HashMap<>();
	private Map<String, Integer> playerOtherMaxXianshuMap = new HashMap<>();
	private List<String> chaodiPlayerIdList = new ArrayList<>();
	private Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap = new HashMap<>();

	public ChaodiResult chaodi(String playerId, boolean chaodi, long actionTime) throws Exception {
		ChaodiResult chaodiResult = new ChaodiResult();
		if (tryPlayerHasZhadan(playerId)) {
			playerChaodiStateMap.put(playerId, PukeGamePlayerChaodiState.cannotchaodi);
		} else {
			if (chaodi) {
				playerChaodiStateMap.put(playerId, PukeGamePlayerChaodiState.chaodi);
			} else {
				playerChaodiStateMap.put(playerId, PukeGamePlayerChaodiState.buchao);
			}
		}
		if (state.name().equals(VoteNotPassWhenChaodi.name)) {
			state = new StartChaodi();
		}
		updatePlayerState(playerId, new PlayerAfterChaodi());
		boolean start = true;
		boolean finish = false;
		for (PukeGamePlayerChaodiState chaodiState : playerChaodiStateMap.values()) {
			if (chaodiState.equals(PukeGamePlayerChaodiState.startChaodi)) {
				start = false;
			}
			if (chaodiState.equals(PukeGamePlayerChaodiState.chaodi)) {
				finish = true;
			}
		}
		if (finish) {// 抄底成功
			WenzhouShuangkouCurrentPanResultBuilder wenzhouShuangkouCurrentPanResultBuilder = (WenzhouShuangkouCurrentPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			PanResult panResult = wenzhouShuangkouCurrentPanResultBuilder.buildCurrentPanResultByChaodi(ju, actionTime);
			ju.getFinishedPanResultList().add(panResult);
			ju.setCurrentPan(null);
			if (ju.getJuFinishiDeterminer().determineToFinishJu(ju)) {// 是否局结束
				ju.setJuResult(ju.getJuResultBuilder().buildJuResult(ju));
			}
			checkAndFinishPan();

			if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
				WenzhouShuangkouPanResult wenzhouShuangkouPanResult = (WenzhouShuangkouPanResult) ju
						.findLatestFinishedPanResult();
				for (WenzhouShuangkouPanPlayerResult wenzhouShuangkouPanPlayerResult : wenzhouShuangkouPanResult
						.getPanPlayerResultList()) {
					playerTotalScoreMap.put(wenzhouShuangkouPanPlayerResult.getPlayerId(),
							wenzhouShuangkouPanPlayerResult.getTotalScore());
				}
				chaodiResult.setPanResult(wenzhouShuangkouPanResult);
				if (state.name().equals(Finished.name)) {// 局结束了
					chaodiResult.setJuResult((WenzhouShuangkouJuResult) ju.getJuResult());
				}
			}
			chaodiResult.setPukeGame(new PukeGameValueObject(this));
			return chaodiResult;
		}
		if (start) {// 未成功抄底，正常游戏
			state = new Playing();
			updateAllPlayersState(new PlayerPlaying());
			PanActionFrame firstPanActionFrame = ju.getCurrentPan().findLatestActionFrame();
			chaodiResult.setPanActionFrame(firstPanActionFrame);
			chaodiResult.setPukeGame(new PukeGameValueObject(this));
		}
		return chaodiResult;
	}

	private boolean tryPlayerHasZhadan(String playerId) {
		Pan currentPan = ju.getCurrentPan();
		ShuangkouPlayer player = currentPan.findPlayer(playerId);
		Map<Integer, PukePai> allShoupai = player.getAllShoupai();
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
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
			if (tryHasZhadanWithWangDang(wangCount, dianshuCountArray, xiaowangCount, dawangCount)) {
				return true;
			}
		} else {
			// 没有王牌
			if (tryHasZhadanWithoutWangDang(dianshuCountArray)) {
				return true;
			}
		}
		return false;
	}

	private boolean tryHasZhadanWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount) {
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
				if (paiXing.hasZhadan()) {
					return true;
				}
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return false;
	}

	private boolean tryHasZhadanWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		return paiXing.hasZhadan();
	}

	private int calculateTotalGongxianfenForPlayer(String playerId) {
		Pan currentPan = ju.getCurrentPan();
		ShuangkouPlayer player = currentPan.findPlayer(playerId);
		Map<Integer, PukePai> allShoupai = player.getAllShoupai();
		int[] dianshuCountArray = new int[15];
		for (PukePai pukePai : allShoupai.values()) {
			DianShu dianShu = pukePai.getPaiMian().dianShu();
			dianshuCountArray[dianShu.ordinal()]++;
		}
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

	private int calculateXianShu(ZhadanDianShuZu zhadan) {
		if (zhadan instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadan = (DanGeZhadanDianShuZu) zhadan;
			return danGeZhadan.getSize();
		} else if (zhadan instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadan = (LianXuZhadanDianShuZu) zhadan;
			int[] lianXuDianShuSizeArray = lianXuZhadan.getLianXuDianShuSizeArray();
			int xianshu = lianXuDianShuSizeArray[0];
			for (int i = 1; i < lianXuDianShuSizeArray.length; i++) {
				if (xianshu > lianXuDianShuSizeArray[i]) {
					xianshu = lianXuDianShuSizeArray[i];
				}
			}
			return xianshu + lianXuZhadan.getLianXuDianShuArray().length;
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

	/**
	 * 根据牌型和牌数计算最佳贡献分
	 */
	private int calculateBestGongxianfen(int[] dianshuCountArray, PaiXing paixing) {
		int bestScore = 0;
		int[] xianshuCount = new int[9];
		List<ZhadanDianShuZu> zhadanDianShuZuList = new ArrayList<>();
		List<DanGeZhadanDianShuZu> danGeZhadanDianShuZuList = paixing.getZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(danGeZhadanDianShuZuList);
		List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList = paixing.getLianXuZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(lianXuZhadanDianShuZuList);
		List<WangZhadanDianShuZu> wangZhadanDianShuZuList = paixing.getWangZhadanDianShuZuList();
		zhadanDianShuZuList.addAll(wangZhadanDianShuZuList);

		while (true) {//
			ZhadanDianShuZu bestZhadanDianShuZu = null;
			int bestXianShu = 0;
			for (ZhadanDianShuZu zhadanDianShuZu : zhadanDianShuZuList) {
				if (verifyZhadanConflict(dianshuCountArray, zhadanDianShuZu)) {
					int xianshu = calculateXianShu(zhadanDianShuZu);
					if (bestZhadanDianShuZu == null || xianshu >= bestXianShu) {// 相同线数优先取非连续牌组
						if (xianshu > bestXianShu) {
							bestZhadanDianShuZu = zhadanDianShuZu;
							bestXianShu = xianshu;
						} else {
							if (!(zhadanDianShuZu instanceof LianXuZhadanDianShuZu)) {
								bestZhadanDianShuZu = zhadanDianShuZu;
								bestXianShu = xianshu;
							}
						}
					}
				}
			}
			if (bestZhadanDianShuZu != null) {
				zhadanDianShuZuList.remove(bestZhadanDianShuZu);
				xianshuCount[bestXianShu - 4]++;
				if (bestZhadanDianShuZu instanceof DanGeZhadanDianShuZu) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) bestZhadanDianShuZu;
					dianshuCountArray[danGeZhadanDianShuZu.getDianShu().ordinal()] -= danGeZhadanDianShuZu.getSize();
				}
				if (bestZhadanDianShuZu instanceof LianXuZhadanDianShuZu) {
					LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) bestZhadanDianShuZu;
					DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
					int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
					for (int i = 0; i < lianXuDianShuArray.length; i++) {
						dianshuCountArray[lianXuDianShuArray[i].ordinal()] -= lianXuDianShuSizeArray[i];
					}
				}
				if (bestZhadanDianShuZu instanceof WangZhadanDianShuZu) {
					WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) bestZhadanDianShuZu;
					dianshuCountArray[13] -= wangZhadanDianShuZu.getXiaowangCount();
					dianshuCountArray[14] -= wangZhadanDianShuZu.getDawangCount();
				}
			} else {
				bestScore = 4 * xianshuCount[2] + 8 * xianshuCount[3] + 16 * xianshuCount[4] + 32 * xianshuCount[5]
						+ 64 * xianshuCount[6] + 128 * xianshuCount[7] + 256 * xianshuCount[8];
				return bestScore;
			}
		}
	}

	/**
	 * 验算炸弹是否合理
	 */
	private boolean verifyZhadanConflict(int[] dianshuCountArray, ZhadanDianShuZu zhadanDianShuZu) {
		if (zhadanDianShuZu instanceof DanGeZhadanDianShuZu) {
			DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanDianShuZu;
			if (dianshuCountArray[danGeZhadanDianShuZu.getDianShu().ordinal()] - danGeZhadanDianShuZu.getSize() < 0) {
				return false;
			}
		}
		if (zhadanDianShuZu instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) zhadanDianShuZu;
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
			for (int i = 0; i < lianXuDianShuArray.length; i++) {
				if (dianshuCountArray[lianXuDianShuArray[i].ordinal()] - lianXuDianShuSizeArray[i] < 0) {
					return false;
				}
			}
		}
		if (zhadanDianShuZu instanceof WangZhadanDianShuZu) {
			WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) zhadanDianShuZu;
			if (dianshuCountArray[13] - wangZhadanDianShuZu.getXiaowangCount() < 0
					|| dianshuCountArray[14] - wangZhadanDianShuZu.getDawangCount() < 0) {
				return false;
			}
		}
		return true;
	}

	private int calculatePaiXingWithoutWangDang(int[] dianshuCountArray) {
		PaiXing paiXing = DianShuZuCalculator.calculateAllDianShuZu(dianshuCountArray);
		return calculateBestGongxianfen(dianshuCountArray.clone(), paiXing);
	}

	private int calculatePaiXingWithWangDang(int wangCount, int[] dianshuCountArray, int xiaowangCount,
			int dawangCount) {
		int bestScore = 0;
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
				int score = calculateBestGongxianfen(dianshuCountArray.clone(), paixing);
				if (score > bestScore) {
					bestScore = score;
				}
				// 减去当牌的数量
				for (ShoupaiJiesuanPai jiesuanPai : wangDangPaiArray) {
					dianshuCountArray[jiesuanPai.getDangPaiType().ordinal()]--;
				}
			}
		}
		return bestScore;
	}

	private void createJuAndStartFirstPan(long currentTime) throws Exception {
		ju = new Ju();
		ju.setCurrentPanFinishiDeterminer(new OnePlayerHasPaiPanFinishiDeterminer());
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer(panshu));
		// 生成盘结果
		WenzhouShuangkouCurrentPanResultBuilder panResultBuilder = new WenzhouShuangkouCurrentPanResultBuilder();
		Map<String, Integer> totalGongxianfenMap = new HashMap<>();
		panResultBuilder.setPlayeTotalGongxianfenMap(totalGongxianfenMap);
		panResultBuilder.setRenshu(renshu);
		ju.setCurrentPanResultBuilder(panResultBuilder);
		// 生成局结果
		ju.setJuResultBuilder(new WenzhouShuangkouJuResultBuilder());
		ju.setAvaliablePaiFiller(new DoubleAvaliablePaiFiller());
		if (ChaPai.wuxu.equals(chapai)) {
			ju.setLuanpaiStrategyForFirstPan(new ErliuhunHasSiwangLuanpaiStrategy(bx, currentTime));
			ju.setLuanpaiStrategyForNextPan(new LastPanChuPaiOrdinalLuanpaiStrategy());
		} else if (ChaPai.erliuhun.equals(chapai)) {
			ju.setLuanpaiStrategyForFirstPan(new ErliuhunHasSiwangLuanpaiStrategy(bx, currentTime));
			ju.setLuanpaiStrategyForNextPan(new ErliuhunHasSiwangLuanpaiStrategy(bx, currentTime + 1));
		} else if (ChaPai.sanwu.equals(chapai)) {
			ju.setLuanpaiStrategyForFirstPan(new SanwuHasSiwangLuanpaiStrategy(bx, currentTime));
			ju.setLuanpaiStrategyForNextPan(new SanwuHasSiwangLuanpaiStrategy(bx, currentTime + 2));
		} else if (ChaPai.ba.equals(chapai)) {
			ju.setLuanpaiStrategyForFirstPan(new BazhangHasSiwangLuanpaiStrategy(bx, currentTime));
			ju.setLuanpaiStrategyForNextPan(new BazhangHasSiwangLuanpaiStrategy(bx, currentTime + 3));
		} else {

		}
		if (FaPai.san.equals(fapai)) {
			ju.setFapaiStrategyForFirstPan(new YiciSanzhangFapaiStrategy());
			ju.setFapaiStrategyForNextPan(new YiciSanzhangFapaiStrategy());
		} else if (FaPai.sanliu.equals(fapai)) {
			ju.setFapaiStrategyForFirstPan(new YisanSiliuFapaiStrategy());
			ju.setFapaiStrategyForNextPan(new YisanSiliuFapaiStrategy());
		} else if (FaPai.liuqi.equals(fapai)) {
			ju.setFapaiStrategyForFirstPan(new YiliuSanqiFapaiStrategy());
			ju.setFapaiStrategyForNextPan(new YiliuSanqiFapaiStrategy());
		} else if (FaPai.jiu.equals(fapai)) {
			ju.setFapaiStrategyForFirstPan(new YiciJiuzhangFapaiStrategy());
			ju.setFapaiStrategyForNextPan(new YiciJiuzhangFapaiStrategy());
		} else {

		}
		ju.setShoupaiSortStrategy(new DianshuOrPaishuShoupaiSortStrategy());
		ju.setZuduiStrategyForFirstPan(new HongxinbaHongxinjiuZuduiStrategy());
		ju.setZuduiStrategyForNextPan(new HongxinbaHongxinjiuZuduiStrategy());
		ju.setXiandaPlayerDeterminer(new DongPositionXiandaPlayerDeterminer());
		// ju.setKeyaDaPaiDianShuSolutionsGenerator(keyaDaPaiDianShuSolutionsGenerator);
		WenzhouShuangkouYaPaiSolutionsTipsFilter wenzhouShuangkouYaPaiSolutionsTipsFilter = new WenzhouShuangkouYaPaiSolutionsTipsFilter();
		wenzhouShuangkouYaPaiSolutionsTipsFilter.setZhadanComparator(new WenzhouShuangkouZhadanComparator());
		ju.setYaPaiSolutionsTipsFilter(wenzhouShuangkouYaPaiSolutionsTipsFilter);
		ju.setAllKedaPaiSolutionsGenerator(new WenzhouShuangkouAllKedaPaiSolutionsGenerator());
		ju.setWaihaoGenerator(new ShuangkouWaihaoGenerator());
		WenzhouShuangkouDianShuZuYaPaiSolutionCalculator dianShuZuYaPaiSolutionCalculator = new WenzhouShuangkouDianShuZuYaPaiSolutionCalculator();
		dianShuZuYaPaiSolutionCalculator.setBx(bx);
		dianShuZuYaPaiSolutionCalculator.setDanGeDianShuZuComparator(new NoZhadanDanGeDianShuZuComparator());
		dianShuZuYaPaiSolutionCalculator.setLianXuDianShuZuComparator(new TongDengLianXuDianShuZuComparator());
		ju.setDianShuZuYaPaiSolutionCalculator(dianShuZuYaPaiSolutionCalculator);
		WenzhouShuangkouZaDanYaPaiSolutionCalculator zaDanYaPaiSolutionCalculator = new WenzhouShuangkouZaDanYaPaiSolutionCalculator();
		zaDanYaPaiSolutionCalculator.setBx(bx);
		zaDanYaPaiSolutionCalculator.setZhadanComparator(new WenzhouShuangkouZhadanComparator());
		ju.setZaDanYaPaiSolutionCalculator(zaDanYaPaiSolutionCalculator);

		ju.addDaListener(new XianshuCountDaActionStatisticsListener());
		// 开始第一盘
		ju.startFirstPan(allPlayerIds(), currentTime);
		allPlayerIds().forEach((pid) -> {
			playerTotalGongxianfenMap.put(pid, calculateTotalGongxianfenForPlayer(pid));
		});
		totalGongxianfenMap.putAll(playerTotalGongxianfenMap);
	}

	public PanActionFrame findFirstPanActionFrame() {
		return ju.getCurrentPan().findLatestActionFrame();
	}

	public PukeActionResult da(String playerId, List<Integer> paiIds, String dianshuZuheIdx, long actionTime)
			throws Exception {
		PanActionFrame panActionFrame = null;

		panActionFrame = ju.da(playerId, paiIds, dianshuZuheIdx, actionTime);
		// 记录贡献分
		XianshuCountDaActionStatisticsListener wenzhouShuangkouListener = ju.getActionStatisticsListenerManager()
				.findDaListener(XianshuCountDaActionStatisticsListener.class);
		Map<String, int[]> playerXianshuMap = wenzhouShuangkouListener.getPlayerXianshuMap();
		Map<String, WenzhouShuangkouXianshuBeishu> maxXianshuMap = new HashMap<>();
		Map<String, WenzhouShuangkouGongxianFen> gongxianfenMap = new HashMap<>();
		List<WenzhouShuangkouGongxianFen> panPlayerGongxianfenList = new ArrayList<>();
		allPlayerIds().forEach((pid) -> {
			int[] xianshuCount = playerXianshuMap.get(pid);
			WenzhouShuangkouXianshuBeishu xianshubeishu = null;
			if (xianshuCount != null) {
				xianshubeishu = new WenzhouShuangkouXianshuBeishu(xianshuCount);
			} else {
				xianshubeishu = new WenzhouShuangkouXianshuBeishu();
			}
			xianshubeishu.calculate();
			maxXianshuMap.put(pid, xianshubeishu);
			WenzhouShuangkouGongxianFen gongxianfen = null;
			if (xianshuCount != null) {
				gongxianfen = new WenzhouShuangkouGongxianFen(xianshuCount);
			} else {
				gongxianfen = new WenzhouShuangkouGongxianFen();
			}
			gongxianfen.calculate(renshu);
			panPlayerGongxianfenList.add(gongxianfen);
			gongxianfenMap.put(pid, gongxianfen);
		});

		// 两两结算贡献分
		for (int i = 0; i < renshu; i++) {
			WenzhouShuangkouGongxianFen gongxian1 = panPlayerGongxianfenList.get(i);
			for (int j = (i + 1); j < renshu; j++) {
				WenzhouShuangkouGongxianFen gongxian2 = panPlayerGongxianfenList.get(j);
				// 结算贡献分
				int fen1 = gongxian1.getValue();
				int fen2 = gongxian2.getValue();
				gongxian1.jiesuan(-fen2);
				gongxian2.jiesuan(-fen1);
			}
		}
		// 计算贡献分
		allPlayerIds().forEach((pid) -> {
			WenzhouShuangkouGongxianFen gongxianfen = gongxianfenMap.get(pid);
			playerGongxianfenMap.put(pid, gongxianfen.getTotalscore());
		});
		// 计算胜负分
		List<String> playerIds = allPlayerIds();
		Pan currentPan = ju.getCurrentPan();
		allPlayerIds().forEach((pid) -> {
			if (renshu > 2) {
				ShuangkouPlayer duijiaPlayer = currentPan.findDuijiaPlayer(pid);
				String duijiaPlayerId = duijiaPlayer.getId();
				int maxXianshu = 1;
				int otherMaxXianshu = 1;
				for (String id : playerIds) {
					if (id.equals(pid) || id.equals(duijiaPlayerId)) {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						if (xianshubeishu.getValue() > maxXianshu) {
							maxXianshu = xianshubeishu.getValue();
						}
					} else {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						if (xianshubeishu.getValue() > otherMaxXianshu) {
							otherMaxXianshu = xianshubeishu.getValue();
						}
					}
				}
				playerMaxXianshuMap.put(pid, maxXianshu);
				playerOtherMaxXianshuMap.put(pid, otherMaxXianshu);
			} else {
				for (String id : playerIds) {
					if (id.equals(pid)) {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						playerMaxXianshuMap.put(id, xianshubeishu.getValue());
					} else {
						WenzhouShuangkouXianshuBeishu xianshubeishu = maxXianshuMap.get(id);
						playerOtherMaxXianshuMap.put(id, xianshubeishu.getValue());
					}
				}
			}
		});
		PukeActionResult result = new PukeActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		checkAndFinishPan();

		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			WenzhouShuangkouPanResult panResult = (WenzhouShuangkouPanResult) ju.findLatestFinishedPanResult();
			for (WenzhouShuangkouPanPlayerResult wenzhouShuangkouPanPlayerResult : panResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(wenzhouShuangkouPanPlayerResult.getPlayerId(),
						wenzhouShuangkouPanPlayerResult.getTotalScore());
			}
			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((WenzhouShuangkouJuResult) ju.getJuResult());
			}
		}
		result.setPukeGame(new PukeGameValueObject(this));
		return result;
	}

	public PukeActionResult guo(String playerId, long actionTime) throws Exception {
		PanActionFrame panActionFrame = null;
		panActionFrame = ju.guo(playerId, actionTime);

		PukeActionResult result = new PukeActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		result.setPukeGame(new PukeGameValueObject(this));
		return result;
	}

	@Override
	protected boolean checkToFinishGame() throws Exception {
		return ju.getJuResult() != null;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		return ju.getCurrentPan() == null;
	}

	@Override
	protected void startNextPan() throws Exception {
		ju.startNextPan();
		boolean hasChaodi = false;
		Set<String> cannotChaodiSet = new HashSet<>();
		if (chaodi) {
			for (String playerId : allPlayerIds()) {
				if (!tryPlayerHasZhadan(playerId)) {
					chaodiPlayerIdList.add(playerId);
					hasChaodi = true;
				} else {
					cannotChaodiSet.add(playerId);
				}
			}
		}
		if (hasChaodi) {// 能够抄底
			WenzhouShuangkouCurrentPanResultBuilder wenzhouShuangkouCurrentPanResultBuilder = (WenzhouShuangkouCurrentPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			List<String> chaodiPlayerIds = new ArrayList<>(chaodiPlayerIdList);
			wenzhouShuangkouCurrentPanResultBuilder.setChaodiPlayerIdList(chaodiPlayerIds);
			allPlayerIds().forEach((pid) -> {
				if (cannotChaodiSet.contains(pid)) {
					playerChaodiStateMap.put(pid, PukeGamePlayerChaodiState.cannotchaodi);
				} else {
					playerChaodiStateMap.put(pid, PukeGamePlayerChaodiState.startChaodi);
				}
			});
			state = new StartChaodi();
			updateAllPlayersState(new PlayerChaodi());
		} else {
			state = new Playing();
			updateAllPlayersState(new PlayerPlaying());
		}
	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
		if (player.getState().name().equals(PlayerChaodi.name)) {
			player.setState(new PlayerVotingWhenChaodi());
		} else if (player.getState().name().equals(PlayerAfterChaodi.name)) {
			player.setState(new PlayerVotingWhenAfterChaodi());
		}
	}

	@Override
	protected void updateToExtendedVotingState() {
		if (state.name().equals(StartChaodi.name) || state.name().equals(VoteNotPassWhenChaodi.name)) {
			state = new VotingWhenChaodi();
		}
	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
		String stateName = player.getState().name();
		if (stateName.equals(PlayerVotingWhenChaodi.name)) {
			player.setState(new PlayerVotedWhenChaodi());
		} else if (player.getState().name().equals(PlayerVotingWhenAfterChaodi.name)) {
			player.setState(new PlayerVotedWhenAfterChaodi());
		}
	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
		if (state.name().equals(VoteNotPassWhenChaodi.name)) {
			for (GamePlayer player : idPlayerMap.values()) {
				if (player.getState().name().equals(PlayerVotingWhenChaodi.name)
						|| player.getState().name().equals(PlayerVotedWhenChaodi.name)) {
					updatePlayerState(player.getId(), new PlayerChaodi());
				} else if (player.getState().name().equals(PlayerVotingWhenAfterChaodi.name)
						|| player.getState().name().equals(PlayerVotedWhenAfterChaodi.name)) {
					updatePlayerState(player.getId(), new PlayerAfterChaodi());
				}
			}
		}
	}

	@Override
	protected void updateToVoteNotPassStateFromExtendedVoting() throws Exception {
		if (state.name().equals(VotingWhenChaodi.name)) {
			state = new VoteNotPassWhenChaodi();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PukeGameValueObject toValueObject() {
		return new PukeGameValueObject(this);
	}

	@Override
	public void start(long currentTime) throws Exception {
		createJuAndStartFirstPan(currentTime);
		boolean hasChaodi = false;
		Set<String> cannotChaodiSet = new HashSet<>();
		if (chaodi) {
			for (String playerId : allPlayerIds()) {
				if (!tryPlayerHasZhadan(playerId)) {
					chaodiPlayerIdList.add(playerId);
					hasChaodi = true;
				} else {
					cannotChaodiSet.add(playerId);
				}
			}
		}
		if (hasChaodi) {// 能够抄底
			WenzhouShuangkouCurrentPanResultBuilder wenzhouShuangkouCurrentPanResultBuilder = (WenzhouShuangkouCurrentPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			List<String> chaodiPlayerIds = new ArrayList<>(chaodiPlayerIdList);
			wenzhouShuangkouCurrentPanResultBuilder.setChaodiPlayerIdList(chaodiPlayerIds);
			allPlayerIds().forEach((pid) -> {
				if (cannotChaodiSet.contains(pid)) {
					playerChaodiStateMap.put(pid, PukeGamePlayerChaodiState.cannotchaodi);
				} else {
					playerChaodiStateMap.put(pid, PukeGamePlayerChaodiState.startChaodi);
				}
			});
			state = new StartChaodi();
			updateAllPlayersState(new PlayerChaodi());
		} else {
			state = new Playing();
			updateAllPlayersState(new PlayerPlaying());
		}
	}

	@Override
	public void finish() throws Exception {
		if (ju != null) {
			ju.finish();
		}
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public boolean isChaodi() {
		return chaodi;
	}

	public void setChaodi(boolean chaodi) {
		this.chaodi = chaodi;
	}

	public boolean isShuangming() {
		return shuangming;
	}

	public void setShuangming(boolean shuangming) {
		this.shuangming = shuangming;
	}

	public boolean isFengding() {
		return fengding;
	}

	public void setFengding(boolean fengding) {
		this.fengding = fengding;
	}

	public ChaPai getChapai() {
		return chapai;
	}

	public void setChapai(ChaPai chapai) {
		this.chapai = chapai;
	}

	public FaPai getFapai() {
		return fapai;
	}

	public void setFapai(FaPai fapai) {
		this.fapai = fapai;
	}

	public Ju getJu() {
		return ju;
	}

	public void setJu(Ju ju) {
		this.ju = ju;
	}

	public Map<String, Integer> getPlayerTotalScoreMap() {
		return playerTotalScoreMap;
	}

	public void setPlayerTotalScoreMap(Map<String, Integer> playerTotalScoreMap) {
		this.playerTotalScoreMap = playerTotalScoreMap;
	}

	public Map<String, Integer> getPlayerGongxianfenMap() {
		return playerGongxianfenMap;
	}

	public void setPlayerGongxianfenMap(Map<String, Integer> playerGongxianfenMap) {
		this.playerGongxianfenMap = playerGongxianfenMap;
	}

	public Map<String, Integer> getPlayerTotalGongxianfenMap() {
		return playerTotalGongxianfenMap;
	}

	public void setPlayerTotalGongxianfenMap(Map<String, Integer> playerTotalGongxianfenMap) {
		this.playerTotalGongxianfenMap = playerTotalGongxianfenMap;
	}

	public List<String> getChaodiPlayerIdList() {
		return chaodiPlayerIdList;
	}

	public void setChaodiPlayerIdList(List<String> chaodiPlayerIdList) {
		this.chaodiPlayerIdList = chaodiPlayerIdList;
	}

	public Map<String, PukeGamePlayerChaodiState> getPlayerChaodiStateMap() {
		return playerChaodiStateMap;
	}

	public void setPlayerChaodiStateMap(Map<String, PukeGamePlayerChaodiState> playerChaodiStateMap) {
		this.playerChaodiStateMap = playerChaodiStateMap;
	}

	public Map<String, Integer> getPlayerMaxXianshuMap() {
		return playerMaxXianshuMap;
	}

	public void setPlayerMaxXianshuMap(Map<String, Integer> playerMaxXianshuMap) {
		this.playerMaxXianshuMap = playerMaxXianshuMap;
	}

	public Map<String, Integer> getPlayerOtherMaxXianshuMap() {
		return playerOtherMaxXianshuMap;
	}

	public void setPlayerOtherMaxXianshuMap(Map<String, Integer> playerOtherMaxXianshuMap) {
		this.playerOtherMaxXianshuMap = playerOtherMaxXianshuMap;
	}

}
