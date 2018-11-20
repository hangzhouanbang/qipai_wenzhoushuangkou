package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZuGenerator;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ZhadanDianShuZu;
import com.dml.shuangkou.BianXingWanFa;
import com.dml.shuangkou.ShoupaiJiesuanPai;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;

/**
 * 通过王牌变化得到不同点数组，再通过点数组和王牌的代法产生打牌方案
 * 
 * @author lsc
 *
 */
public class DianShuZuCalculator {
	private static List<DuiziDianShuZu> duiziDianShuZuList;
	private static List<LianduiDianShuZu> lianduiDianShuZuList;
	private static List<LiansanzhangDianShuZu> liansanzhangDianShuZuList;
	private static List<SanzhangDianShuZu> sanzhangDianShuZuList;
	private static List<ShunziDianShuZu> shunziDianShuZuList;
	private static List<ZhadanDianShuZu> zhadanDianShuZuList;
	private static List<LianXuZhadanDianShuZu> lianXuZhadanDianShuZuList;
	private static List<WangZhadanDianShuZu> wangZhadanDianShuZu;

	/**
	 * 在千变玩法下 根据王的当法还原打牌点数
	 */
	private static List<DaPaiDianShuSolution> calculateDaPaiDianShuSolutionForDianShuZu(
			ShoupaiJiesuanPai[] wangDangPaiArray, int[] dianshuCountArray) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		int[] dianshuCount = dianshuCountArray.clone();
		// 大小王做单张牌打出必定是作为本身的牌的点数
		// 对子
		for (DuiziDianShuZu duiziDianShuZu : duiziDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu duiziDianShuType = duiziDianShuZu.getDianShu();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
				if (shoupaiJiesuanPai.getDangPaiType().equals(duiziDianShuType)) {
					keYongList.add(shoupaiJiesuanPai);
					dianshuCount[duiziDianShuType.ordinal()]--;
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForDuiziDianShuZu(zuheList, duiziDianShuZu,
						dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(duiziDianShuZu);
				DianShu[] dachuDianShuArray = new DianShu[2];
				for (int i = 0; i < 2; i++) {
					dachuDianShuArray[i] = duiziDianShuZu.getDianShu();
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 连对
		for (LianduiDianShuZu lianduiDianShuZu : lianduiDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu[] lianXuDianShuArray = lianduiDianShuZu.getLianXuDianShuArray();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (DianShu dianshu : lianXuDianShuArray) {
				for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
					if (shoupaiJiesuanPai.getDangPaiType().equals(dianshu)) {
						keYongList.add(shoupaiJiesuanPai);
						dianshuCount[dianshu.ordinal()]--;
					}
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForLianduiDianShuZu(zuheList, lianduiDianShuZu,
						dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(lianduiDianShuZu);
				DianShu[] dachuDianShuArray = new DianShu[lianduiDianShuZu.length() * 2];
				for (int i = 0; i < lianduiDianShuZu.length(); i++) {
					dachuDianShuArray[i] = lianduiDianShuZu.getLianXuDianShuArray()[i];
					dachuDianShuArray[i + 1] = lianduiDianShuZu.getLianXuDianShuArray()[i];
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 连三张
		for (LiansanzhangDianShuZu liansanzhangDianShuZu : liansanzhangDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu[] lianXuDianShuArray = liansanzhangDianShuZu.getLianXuDianShuArray();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (DianShu dianshu : lianXuDianShuArray) {
				for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
					if (shoupaiJiesuanPai.getDangPaiType().equals(dianshu)) {
						keYongList.add(shoupaiJiesuanPai);
						dianshuCount[dianshu.ordinal()]--;
					}
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForLiansanzhangDianShuZu(zuheList,
						liansanzhangDianShuZu, dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(liansanzhangDianShuZu);
				DianShu[] dachuDianShuArray = new DianShu[liansanzhangDianShuZu.length() * 3];
				for (int i = 0; i < liansanzhangDianShuZu.length(); i++) {
					dachuDianShuArray[i] = liansanzhangDianShuZu.getLianXuDianShuArray()[i];
					dachuDianShuArray[i + 1] = liansanzhangDianShuZu.getLianXuDianShuArray()[i];
					dachuDianShuArray[i + 2] = liansanzhangDianShuZu.getLianXuDianShuArray()[i];
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 三张牌
		for (SanzhangDianShuZu sanzhangDianShuZu : sanzhangDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu duiziDianShuType = sanzhangDianShuZu.getDianShu();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
				if (shoupaiJiesuanPai.getDangPaiType().equals(duiziDianShuType)) {
					keYongList.add(shoupaiJiesuanPai);
					dianshuCount[duiziDianShuType.ordinal()]--;
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForSanzhangDianShuZu(zuheList, sanzhangDianShuZu,
						dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(sanzhangDianShuZu);
				DianShu[] dachuDianShuArray = new DianShu[3];
				for (int i = 0; i < 3; i++) {
					dachuDianShuArray[i] = sanzhangDianShuZu.getDianShu();
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 顺子
		for (ShunziDianShuZu shunziDianShuZu : shunziDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu[] lianXuDianShuArray = shunziDianShuZu.getLianXuDianShuArray();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (DianShu dianshu : lianXuDianShuArray) {
				for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
					if (shoupaiJiesuanPai.getDangPaiType().equals(dianshu)) {
						keYongList.add(shoupaiJiesuanPai);
						dianshuCount[dianshu.ordinal()]--;
					}
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForShunziDianShuZu(zuheList, shunziDianShuZu,
						dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(shunziDianShuZu);
				DianShu[] dachuDianShuArray = new DianShu[shunziDianShuZu.length()];
				for (int i = 0; i < shunziDianShuZu.length(); i++) {
					dachuDianShuArray[i] = shunziDianShuZu.getLianXuDianShuArray()[i];
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 炸弹
		for (ZhadanDianShuZu zhadanDianShuZu : zhadanDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu duiziDianShuType = zhadanDianShuZu.getDianShu();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
				if (shoupaiJiesuanPai.getDangPaiType().equals(duiziDianShuType)) {
					keYongList.add(shoupaiJiesuanPai);
					dianshuCount[duiziDianShuType.ordinal()]--;
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForZhadanDianShuZu(zuheList, zhadanDianShuZu,
						dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(zhadanDianShuZu);
				DianShu[] dachuDianShuArray = new DianShu[zhadanDianShuZu.getSize()];
				for (int i = 0; i < zhadanDianShuZu.getSize(); i++) {
					dachuDianShuArray[i] = zhadanDianShuZu.getDianShu();
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 连续炸弹
		for (LianXuZhadanDianShuZu lianXuZhadanDianShuZu : lianXuZhadanDianShuZuList) {
			dianshuCount = dianshuCountArray.clone();
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			List<ShoupaiJiesuanPai> keYongList = new ArrayList<>();
			for (DianShu dianshu : lianXuDianShuArray) {
				for (ShoupaiJiesuanPai shoupaiJiesuanPai : wangDangPaiArray) {
					if (shoupaiJiesuanPai.getDangPaiType().equals(dianshu)) {
						keYongList.add(shoupaiJiesuanPai);
						dianshuCount[dianshu.ordinal()]--;
					}
				}
			}
			if (keYongList.size() > 0) {
				List<int[]> zuheList = calculateZuHeWithJiSuanPaiDang(keYongList.size());
				solutionList.addAll(verifyZuHeAndGenerateSolutionForLianXuZhadanDianShuZu(zuheList,
						lianXuZhadanDianShuZu, dianshuCount, keYongList));
			} else {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(lianXuZhadanDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				int[] dianshuZhangshuArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
				for (int i = 0; i < lianXuDianShuArray.length; i++) {
					for (int j = 0; j < dianshuZhangshuArray[i]; j++) {
						dachuDianShuList.add(lianXuDianShuArray[i]);
					}
				}
				DianShu[] dachuDianShuArray = (DianShu[]) dachuDianShuList.toArray();
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 王炸
		for (WangZhadanDianShuZu wangZhadanDianShuZu : wangZhadanDianShuZu) {
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
			DianShu[] dachuDianShuArray = (DianShu[]) dachuDianShuList.toArray();
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		return solutionList;
	}

	/**
	 * 计算是否使用当牌的组合
	 */
	private static List<int[]> calculateZuHeWithJiSuanPaiDang(int dangCount) {
		List<int[]> zuheList = new ArrayList<>();
		int maxZuheCode = (int) Math.pow(2, dangCount);// 二进制0代表不用，1代表用
		int[] modArray = new int[dangCount];
		for (int m = 0; m < dangCount; m++) {
			modArray[m] = (int) Math.pow(2, dangCount - 1 - m);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			int[] zuheArray = new int[dangCount];
			int temp = zuheCode;
			for (int n = 0; n < dangCount; n++) {
				int mod = modArray[n];
				int shang = temp / mod;
				int yu = temp % mod;
				zuheArray[n] = shang;
				temp = yu;
			}
			zuheList.add(zuheArray);
		}
		return zuheList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForDuiziDianShuZu(List<int[]> zuheList,
			DuiziDianShuZu duiziDianShuZu, int[] dianshuCount, List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			// 验证组合是否合法
			int num = 0;
			for (int i = 0; i < zuhe.length; i++) {
				num += zuhe[i];
			}
			num += dianshuCount[duiziDianShuZu.getDianShu().ordinal()];
			if (num >= 2) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(duiziDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				int k = 0;
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
					}
					k++;
				}
				while (k < 2) {
					dachuDianShuList.add(duiziDianShuZu.getDianShu());
					k++;
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForSanzhangDianShuZu(List<int[]> zuheList,
			SanzhangDianShuZu sanzhangDianShuZu, int[] dianshuCount, List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			// 验证组合是否合法
			int num = 0;
			for (int i = 0; i < zuhe.length; i++) {
				num += zuhe[i];
			}
			num += dianshuCount[sanzhangDianShuZu.getDianShu().ordinal()];
			if (num >= 3) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(sanzhangDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				int k = 0;
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
					}
					k++;
				}
				while (k < 3) {
					dachuDianShuList.add(sanzhangDianShuZu.getDianShu());
					k++;
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForZhadanDianShuZu(List<int[]> zuheList,
			ZhadanDianShuZu zhadanDianShuZu, int[] dianshuCount, List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			// 验证组合是否合法
			int num = 0;
			for (int i = 0; i < zuhe.length; i++) {
				num += zuhe[i];
			}
			num += dianshuCount[zhadanDianShuZu.getDianShu().ordinal()];
			if (num >= zhadanDianShuZu.getSize()) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(zhadanDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				int k = 0;
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
					}
					k++;
				}
				while (k < zhadanDianShuZu.getSize()) {
					dachuDianShuList.add(zhadanDianShuZu.getDianShu());
					k++;
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForShunziDianShuZu(List<int[]> zuheList,
			ShunziDianShuZu shunziDianShuZu, int[] dianshuCount, List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			boolean allSuccess = true;
			for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray()) {
				// 验证组合是否合法
				int num = 0;
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1 && dianshu.equals(keYongList.get(i).getDangPaiType())) {
						num++;
					}
				}
				num += dianshuCount[dianshu.ordinal()];
				if (num < 1) {
					allSuccess = false;
				}
			}
			if (allSuccess) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(shunziDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				Map<DianShu, Integer> dianshuCountMap = new HashMap<>();
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
						Integer count = dianshuCountMap.get(pai.getDangPaiType());
						if (count == null) {
							dianshuCountMap.put(pai.getDangPaiType(), 1);
						}
					}
				}
				for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray()) {
					Integer count = dianshuCountMap.get(dianshu);
					if (count == null) {
						dachuDianShuList.add(dianshu);
					}
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForLianduiDianShuZu(List<int[]> zuheList,
			LianduiDianShuZu lianduiDianShuZu, int[] dianshuCount, List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			boolean allSuccess = true;
			for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray()) {
				// 验证组合是否合法
				int num = 0;
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1 && dianshu.equals(keYongList.get(i).getDangPaiType())) {
						num++;
					}
				}
				num += dianshuCount[dianshu.ordinal()];
				if (num < 2) {
					allSuccess = false;
				}
			}
			if (allSuccess) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(lianduiDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				Map<DianShu, Integer> dianshuCountMap = new HashMap<>();
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
						Integer count = dianshuCountMap.get(pai.getDangPaiType());
						if (count == null) {
							dianshuCountMap.put(pai.getDangPaiType(), 1);
						} else {
							dianshuCountMap.put(pai.getDangPaiType(), count + 1);
						}
					}
				}
				for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray()) {
					Integer count = dianshuCountMap.get(dianshu);
					if (count == null) {
						dachuDianShuList.add(dianshu);
						dachuDianShuList.add(dianshu);
					} else if (count == 1) {
						dachuDianShuList.add(dianshu);
					}
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForLiansanzhangDianShuZu(
			List<int[]> zuheList, LiansanzhangDianShuZu liansanzhangDianShuZu, int[] dianshuCount,
			List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			boolean allSuccess = true;
			for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray()) {
				// 验证组合是否合法
				int num = 0;
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1 && dianshu.equals(keYongList.get(i).getDangPaiType())) {
						num++;
					}
				}
				num += dianshuCount[dianshu.ordinal()];
				if (num < 3) {
					allSuccess = false;
				}
			}
			if (allSuccess) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(liansanzhangDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				Map<DianShu, Integer> dianshuCountMap = new HashMap<>();
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
						Integer count = dianshuCountMap.get(pai.getDangPaiType());
						if (count == null) {
							dianshuCountMap.put(pai.getDangPaiType(), 1);
						} else {
							dianshuCountMap.put(pai.getDangPaiType(), count + 1);
						}
					}
				}
				for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray()) {
					Integer count = dianshuCountMap.get(dianshu);
					if (count == null) {
						dachuDianShuList.add(dianshu);
						dachuDianShuList.add(dianshu);
						dachuDianShuList.add(dianshu);
					} else if (count == 1) {
						dachuDianShuList.add(dianshu);
						dachuDianShuList.add(dianshu);
					} else if (count == 2) {
						dachuDianShuList.add(dianshu);
					}
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	private static List<DaPaiDianShuSolution> verifyZuHeAndGenerateSolutionForLianXuZhadanDianShuZu(
			List<int[]> zuheList, LianXuZhadanDianShuZu lianXuZhadanDianShuZu, int[] dianshuCount,
			List<ShoupaiJiesuanPai> keYongList) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		for (int[] zuhe : zuheList) {
			boolean allSuccess = true;
			for (DianShu dianshu : lianXuZhadanDianShuZu.getLianXuDianShuArray()) {
				// 验证组合是否合法
				if (dianshu.ordinal() < 13) {
					int num = 0;
					for (int i = 0; i < zuhe.length; i++) {
						if (zuhe[i] == 1 && dianshu.equals(keYongList.get(i).getDangPaiType())) {
							num++;
						}
					}
					num += dianshuCount[dianshu.ordinal()];
					if (num < 4) {
						allSuccess = false;
					}
				} else {
					int num = 0;
					for (int i = 0; i < zuhe.length; i++) {
						if (zuhe[i] == 1 && (DianShu.xiaowang.equals(keYongList.get(i).getDangPaiType())
								|| DianShu.dawang.equals(keYongList.get(i).getDangPaiType()))) {
							num++;
						}
					}
					num += dianshuCount[13] + dianshuCount[14];
					if (num < 3) {
						allSuccess = false;
					}
				}
			}
			if (allSuccess) {// 合法
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(lianXuZhadanDianShuZu);
				List<DianShu> dachuDianShuList = new ArrayList<>();
				Map<DianShu, Integer> dianshuCountMap = new HashMap<>();
				for (int i = 0; i < zuhe.length; i++) {
					if (zuhe[i] == 1) {
						ShoupaiJiesuanPai pai = keYongList.get(i);
						dachuDianShuList.addAll(Arrays.asList(pai.getAllYuanPai()));
						Integer count = dianshuCountMap.get(pai.getDangPaiType());
						if (count == null) {
							dianshuCountMap.put(pai.getDangPaiType(), 1);
						} else {
							dianshuCountMap.put(pai.getDangPaiType(), count + 1);
						}
					}
				}
				DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
				int[] lianXuDianShuSizeArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
				for (int i = 0; i < lianXuDianShuSizeArray.length; i++) {
					int size = lianXuDianShuSizeArray[i];
					DianShu dianshu = lianXuDianShuArray[i];
					Integer count = dianshuCountMap.get(dianshu);
					for (int k = 0; k < size - count; k++) {
						dachuDianShuList.add(dianshu);
					}
				}
				solution.setDachuDianShuArray((DianShu[]) dachuDianShuList.toArray());
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		return solutionList;
	}

	/**
	 * 在某种王的当法下计算出可打的点数组
	 */
	public static List<DaPaiDianShuSolution> calculateAllDaPaiDianShuSolutionWithWangDang(BianXingWanFa bx,
			int[] dianshuCountArray, ShoupaiJiesuanPai[] wangDangPaiArray) {
		// 对子
		duiziDianShuZuList = DianShuZuGenerator.generateAllDuiziDianShuZu(dianshuCountArray);
		// 连对
		lianduiDianShuZuList = new ArrayList<>();
		for (int k = 3; k < 14; k++) {// 最小3连，最大13连
			lianduiDianShuZuList.addAll(DianShuZuGenerator.generateAllLianduiDianShuZu(dianshuCountArray, k));
		}
		// 连三张
		liansanzhangDianShuZuList = new ArrayList<>();
		for (int k = 3; k < 10; k++) {// 最小3连，最大9连
			liansanzhangDianShuZuList.addAll(DianShuZuGenerator.generateAllLiansanzhangDianShuZu(dianshuCountArray, k));
		}
		// 三张牌
		sanzhangDianShuZuList = DianShuZuGenerator.generateAllSanzhangDianShuZu(dianshuCountArray);
		// 顺子
		shunziDianShuZuList = new ArrayList<>();
		for (int k = 5; k < 14; k++) {// 最小5连，最大13连
			shunziDianShuZuList.addAll(DianShuZuGenerator.generateAllShunziDianShuZu(dianshuCountArray, k));
		}
		// 炸弹
		zhadanDianShuZuList = DianShuZuGenerator.generateAllZhadanDianShuZu(dianshuCountArray);
		// 连续炸弹
		lianXuZhadanDianShuZuList = generateAllLianXuZhadanDianShuZu(dianshuCountArray);
		// 王炸
		wangZhadanDianShuZu = generateAllWangZhadanDianShuZu(dianshuCountArray);
		return calculateDaPaiDianShuSolutionForDianShuZu(wangDangPaiArray, dianshuCountArray);
	}

	/**
	 * 没有王的情况下生成打牌方案
	 */
	public static List<DaPaiDianShuSolution> calculateAllDaPaiDianShuSolutionWithoutWangDang(int[] dianshuCountArray) {
		List<DaPaiDianShuSolution> solutionList = new ArrayList<>();
		// 对子
		duiziDianShuZuList = DianShuZuGenerator.generateAllDuiziDianShuZu(dianshuCountArray);
		for (DuiziDianShuZu duiziDianShuZu : duiziDianShuZuList) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(duiziDianShuZu);
			DianShu[] dachuDianShuArray = { duiziDianShuZu.getDianShu(), duiziDianShuZu.getDianShu() };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		// 连对
		for (int k = 3; k < 14; k++) {// 最小3连，最大13连
			lianduiDianShuZuList = DianShuZuGenerator.generateAllLianduiDianShuZu(dianshuCountArray, k);
			for (LianduiDianShuZu lianduiDianShuZu : lianduiDianShuZuList) {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(lianduiDianShuZu);
				DianShu[] lianXuDianShuArray = lianduiDianShuZu.getLianXuDianShuArray();
				DianShu[] dachuDianShuArray = new DianShu[2 * lianXuDianShuArray.length];
				for (int i = 0; i < lianXuDianShuArray.length; i++) {
					dachuDianShuArray[i * 2] = lianXuDianShuArray[i];
					dachuDianShuArray[i * 2 + 1] = lianXuDianShuArray[i];
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 连三张
		for (int k = 3; k < 10; k++) {// 最小3连，最大9连
			liansanzhangDianShuZuList = DianShuZuGenerator.generateAllLiansanzhangDianShuZu(dianshuCountArray, k);
			for (LiansanzhangDianShuZu liansanzhangDianShuZu : liansanzhangDianShuZuList) {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(liansanzhangDianShuZu);
				DianShu[] lianXuDianShuArray = liansanzhangDianShuZu.getLianXuDianShuArray();
				DianShu[] dachuDianShuArray = new DianShu[3 * lianXuDianShuArray.length];
				for (int i = 0; i < lianXuDianShuArray.length; i++) {
					dachuDianShuArray[i * 3] = lianXuDianShuArray[i];
					dachuDianShuArray[i * 3 + 1] = lianXuDianShuArray[i];
					dachuDianShuArray[i * 3 + 2] = lianXuDianShuArray[i];
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 三张牌
		sanzhangDianShuZuList = DianShuZuGenerator.generateAllSanzhangDianShuZu(dianshuCountArray);
		for (SanzhangDianShuZu sanzhangDianShuZu : sanzhangDianShuZuList) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(sanzhangDianShuZu);
			DianShu[] dachuDianShuArray = { sanzhangDianShuZu.getDianShu(), sanzhangDianShuZu.getDianShu(),
					sanzhangDianShuZu.getDianShu() };
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		// 顺子
		for (int k = 5; k < 14; k++) {// 最小5连，最大13连
			shunziDianShuZuList = DianShuZuGenerator.generateAllShunziDianShuZu(dianshuCountArray, k);
			for (ShunziDianShuZu shunziDianShuZu : shunziDianShuZuList) {
				DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
				solution.setDianShuZu(shunziDianShuZu);
				DianShu[] lianXuDianShuArray = shunziDianShuZu.getLianXuDianShuArray();
				DianShu[] dachuDianShuArray = new DianShu[lianXuDianShuArray.length];
				for (int i = 0; i < lianXuDianShuArray.length; i++) {
					dachuDianShuArray[i] = lianXuDianShuArray[i];
				}
				solution.setDachuDianShuArray(dachuDianShuArray);
				solution.calculateDianshuZuheIdx();
				solutionList.add(solution);
			}
		}
		// 炸弹
		zhadanDianShuZuList = DianShuZuGenerator.generateAllZhadanDianShuZu(dianshuCountArray);
		for (ZhadanDianShuZu zhadanDianShuZu : zhadanDianShuZuList) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(zhadanDianShuZu);
			DianShu[] dachuDianShuArray = new DianShu[zhadanDianShuZu.getSize()];
			for (int i = 0; i < zhadanDianShuZu.getSize(); i++) {
				dachuDianShuArray[i] = zhadanDianShuZu.getDianShu();
			}
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		// 连续炸弹
		lianXuZhadanDianShuZuList = generateAllLianXuZhadanDianShuZu(dianshuCountArray);
		for (LianXuZhadanDianShuZu lianXuZhadanDianShuZu : lianXuZhadanDianShuZuList) {
			DaPaiDianShuSolution solution = new DaPaiDianShuSolution();
			solution.setDianShuZu(lianXuZhadanDianShuZu);
			List<DianShu> dachuDianShuList = new ArrayList<>();
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			int[] dianshuZhangshuArray = lianXuZhadanDianShuZu.getLianXuDianShuSizeArray();
			for (int i = 0; i < lianXuDianShuArray.length; i++) {
				for (int j = 0; j < dianshuZhangshuArray[i]; j++) {
					dachuDianShuList.add(lianXuDianShuArray[i]);
				}
			}
			DianShu[] dachuDianShuArray = (DianShu[]) dachuDianShuList.toArray();
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		// 王炸
		wangZhadanDianShuZu = generateAllWangZhadanDianShuZu(dianshuCountArray);
		for (WangZhadanDianShuZu wangZhadanDianShuZu : wangZhadanDianShuZu) {
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
			DianShu[] dachuDianShuArray = (DianShu[]) dachuDianShuList.toArray();
			solution.setDachuDianShuArray(dachuDianShuArray);
			solution.calculateDianshuZuheIdx();
			solutionList.add(solution);
		}
		return solutionList;
	}

	private static List<LianXuZhadanDianShuZu> generateAllLianXuZhadanDianShuZu(int[] dianShuAmountArray) {
		List<LianXuZhadanDianShuZu> lianXuZhadanList = new ArrayList<>();
		for (int i = 0; i < dianShuAmountArray.length; i++) {
			int[] dianshuZhangshuArray = new int[9];// 每种点数可能有多少张牌，最多9连
			int lianXuZhadanLianXuCount = 0;
			int length = 0;
			int j = i;
			while (j < 13 && dianShuAmountArray[j] >= 4) {// 任意3张或者3张以上点数相连的牌，3起最小，到2
				lianXuZhadanLianXuCount++;
				length++;
				j++;
				dianshuZhangshuArray[j - i] = dianShuAmountArray[j];
			}
			if (j + 1 == 13 && dianShuAmountArray[13] + dianShuAmountArray[14] >= 3) {// 连三王炸或者天王炸
				lianXuZhadanLianXuCount++;
				length += 2;
				j++;
				dianshuZhangshuArray[j - i] = dianShuAmountArray[j];
				dianshuZhangshuArray[j - i + 1] = dianShuAmountArray[j + 1];
			}
			if (lianXuZhadanLianXuCount >= 3) {
				DianShu[] lianXuDianShuArray = new DianShu[length];
				for (int k = 0; k < lianXuZhadanLianXuCount; k++) {
					lianXuDianShuArray[k] = DianShu.getDianShuByOrdinal(i + k);
				}
				LianXuZhadanDianShuZu lianXuZhadan = new LianXuZhadanDianShuZu(lianXuDianShuArray,
						dianshuZhangshuArray);
				lianXuZhadanList.add(lianXuZhadan);
			}
		}
		return lianXuZhadanList;
	}

	private static List<WangZhadanDianShuZu> generateAllWangZhadanDianShuZu(int[] dianShuAmountArray) {
		List<WangZhadanDianShuZu> wangZhadanList = new ArrayList<>();
		int xiaowangCount = dianShuAmountArray[13];
		int dawangCount = dianShuAmountArray[14];
		if (xiaowangCount + dawangCount >= 3) {
			WangZhadanDianShuZu angZhadan = new WangZhadanDianShuZu(xiaowangCount, dawangCount);
			wangZhadanList.add(angZhadan);
		}
		return wangZhadanList;
	}
}
