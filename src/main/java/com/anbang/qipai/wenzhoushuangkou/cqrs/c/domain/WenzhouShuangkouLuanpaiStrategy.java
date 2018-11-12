package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dml.puke.pai.PukePai;
import com.dml.puke.wanfa.dianshu.paizu.DianShuZuPaiZu;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pan.Pan;
import com.dml.shuangkou.preparedapai.luanpai.ChaPai;
import com.dml.shuangkou.preparedapai.luanpai.LuanpaiStrategy;

public class WenzhouShuangkouLuanpaiStrategy implements LuanpaiStrategy {
	private BianXingWanFa bx;
	private ChaPai chapai;
	private long seed;

	public WenzhouShuangkouLuanpaiStrategy() {

	}

	public WenzhouShuangkouLuanpaiStrategy(ChaPai chapai, long seed) {
		this.chapai = chapai;
		this.seed = seed;
	}

	@Override
	public void luanpai(Ju ju) throws Exception {
		Random r = new Random(seed);
		Integer[] indexs = new Integer[26];
		// 随机出26份牌的叠加顺序
		for (int i = 0; i < 26; i++) {
			while (true) {
				int index = r.nextInt(26);
				if (indexs[i] == null) {
					indexs[i] = index;
					break;
				}
			}
		}
		Pan currentPan = ju.getCurrentPan();
		List<PukePai> allPaiList = currentPan.getAvaliablePaiList();
		// 最终的牌堆
		List<PukePai> finalPaiList = new ArrayList<>();
		// 临时存放的牌堆
		List<PukePai> tmpPaiList = new LinkedList<>();
		Map<Integer, List<PukePai>> temMap = new HashMap<>();
		if (ChaPai.wuxu.equals(chapai)) {// 无序
			if (currentPan.getNo() == 1) {// 第一盘时，按照2-6混插
				// 将每种牌8张分成2份
				List<PukePai> firstPaiList = new ArrayList<>();
				List<PukePai> secondPaiList = new ArrayList<>();
				for (int i = 0; i < 13; i++) {
					// 随机出前一份的数量2,3,4
					int firstSize = (int) (r.nextDouble() * (4 - 2 + 1)) + 2;
					switch (firstSize) {
					case 2:
						System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, firstSize);
						System.arraycopy(allPaiList, (i - 1) * 8 + firstSize - 1, secondPaiList, 0, 8 - firstSize);
						break;
					case 3:
						System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, firstSize);
						System.arraycopy(allPaiList, (i - 1) * 8 + firstSize - 1, secondPaiList, 0, 8 - firstSize);
						break;
					case 4:
						System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, firstSize);
						System.arraycopy(allPaiList, (i - 1) * 8 + firstSize - 1, secondPaiList, 0, 8 - firstSize);
						break;
					}
					// 保存分组和叠加顺序
					int firstIndex = indexs[i];
					int secondIndex = indexs[26 - i - 1];
					temMap.put(firstIndex, firstPaiList);
					temMap.put(secondIndex, secondPaiList);
				}
				// 将所有牌放入牌堆
				for (int i = 0; i < 26; i++) {
					tmpPaiList.addAll(temMap.get(i));
				}
			} else {// 此后每一盘牌的顺序按照上一盘的出牌的顺序将牌叠在一起
				List<DianShuZuPaiZu> dachuPaiZuList = ju.findLatestFinishedPanResult().getPan().getDachuPaiZuList();
				for (DianShuZuPaiZu zu : dachuPaiZuList) {
					/*
					 * 调用Arrays.asList()时，其返回值类型是ArrayList，但此ArrayList是Array的内部类，调用add()时，会报错：java.
					 * lang.UnsupportedOperationException，并且结果会因为array的某个值的改变而改变，
					 * 故需要再次构造一个新的ArrayList。
					 * 
					 */
					tmpPaiList.addAll(Arrays.asList(zu.getPaiArray()));
				}
			}
		} else if (ChaPai.erliuhun.equals(chapai)) {// 2-6混
			// 将每种牌8张分成2份
			List<PukePai> firstPaiList = new ArrayList<>();
			List<PukePai> secondPaiList = new ArrayList<>();
			for (int i = 0; i < 13; i++) {
				// 随机出前一份的数量2,3,4
				int firstSize = (int) (r.nextDouble() * (4 - 2 + 1)) + 2;
				switch (firstSize) {
				case 2:
					System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, firstSize);
					System.arraycopy(allPaiList, (i - 1) * 8 + firstSize - 1, secondPaiList, 0, 8 - firstSize);
					break;
				case 3:
					System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, firstSize);
					System.arraycopy(allPaiList, (i - 1) * 8 + firstSize - 1, secondPaiList, 0, 8 - firstSize);
					break;
				case 4:
					System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, firstSize);
					System.arraycopy(allPaiList, (i - 1) * 8 + firstSize - 1, secondPaiList, 0, 8 - firstSize);
					break;
				}
				// 保存分组和叠加顺序
				int firstIndex = indexs[i];
				int secondIndex = indexs[26 - i - 1];
				temMap.put(firstIndex, firstPaiList);
				temMap.put(secondIndex, secondPaiList);
			}
			// 将所有牌放入牌堆
			for (int i = 0; i < 26; i++) {
				tmpPaiList.addAll(temMap.get(i));
			}
		} else if (ChaPai.sanwu.equals(chapai)) {// 3/5
			// 将每种牌8张分成2份
			List<PukePai> firstPaiList = new ArrayList<>();
			List<PukePai> secondPaiList = new ArrayList<>();
			for (int i = 0; i < 13; i++) {
				System.arraycopy(allPaiList, (i - 1) * 8, firstPaiList, 0, 3);
				System.arraycopy(allPaiList, (i - 1) * 8 + 3 - 1, secondPaiList, 0, 5);

				// 保存分组和叠加顺序
				int firstIndex = indexs[i];
				int secondIndex = indexs[26 - i - 1];
				temMap.put(firstIndex, firstPaiList);
				temMap.put(secondIndex, secondPaiList);
			}
			// 将所有牌放入牌堆
			for (int i = 0; i < 26; i++) {
				tmpPaiList.addAll(temMap.get(i));
			}
		} else if (ChaPai.ba.equals(chapai)) {// 8张
			// 将每种牌8张作为1份
			List<PukePai> paiList = new ArrayList<>();
			for (int i = 0; i < 13; i++) {
				System.arraycopy(allPaiList, (i - 1) * 8, paiList, 0, 8);

				// 保存分组和叠加顺序
				int index = indexs[i];
				temMap.put(index, paiList);
			}
			// 将所有牌放入牌堆
			for (int i = 0; i < 26; i++) {
				List<PukePai> pais = temMap.get(i);
				if (pais != null) {
					tmpPaiList.addAll(pais);
				}
			}
		}
		// 插入大、小王
		if (BianXingWanFa.qianbian.equals(bx)) {// 千变
			tmpPaiList.add(r.nextInt(tmpPaiList.size()), allPaiList.get(107));
			tmpPaiList.add(r.nextInt(tmpPaiList.size()), allPaiList.get(106));
			tmpPaiList.add(r.nextInt(tmpPaiList.size()), allPaiList.get(105));
			tmpPaiList.add(r.nextInt(tmpPaiList.size()), allPaiList.get(104));
		} else {// 百变，半千变：两张小王永远叠在一起与其他两张大王随机插入已经叠好的牌堆
			tmpPaiList.add(r.nextInt(tmpPaiList.size()), allPaiList.get(107));
			tmpPaiList.add(r.nextInt(tmpPaiList.size()), allPaiList.get(106));
			int index = r.nextInt(tmpPaiList.size());
			tmpPaiList.add(index, allPaiList.get(105));
			tmpPaiList.add(index + 1, allPaiList.get(104));
		}
		// 切牌 TODO通过链表的指针直接排序
		List<PukePai> paiList1 = new ArrayList<>();
		List<PukePai> paiList2 = new ArrayList<>();
		List<PukePai> paiList3 = new ArrayList<>();
		Map<Integer, List<PukePai>> ordinalListMap = new HashMap<>();
		ordinalListMap.put(0, paiList1);
		ordinalListMap.put(1, paiList2);
		ordinalListMap.put(2, paiList3);
		int firstIndex;
		int secondIndex;
		do {
			firstIndex = r.nextInt(tmpPaiList.size());// 第一刀位置
			secondIndex = r.nextInt(tmpPaiList.size());// 第二刀位置
		} while (firstIndex == secondIndex);// 两刀位置不相同
		if (firstIndex > secondIndex) {// 如果第二刀位置在第一刀前面，则交换位置
			firstIndex = firstIndex ^ secondIndex;
			secondIndex = firstIndex ^ secondIndex;
			firstIndex = firstIndex ^ secondIndex;
		}
		System.arraycopy(tmpPaiList, 0, paiList1, 0, firstIndex);
		System.arraycopy(tmpPaiList, firstIndex, paiList2, 0, secondIndex - firstIndex);
		System.arraycopy(tmpPaiList, secondIndex, paiList3, 0, tmpPaiList.size() - secondIndex);
		List<Integer> ordinalList = new ArrayList<>();
		ordinalList.add(0);
		ordinalList.add(1);
		ordinalList.add(2);
		for (int i = 0; i < 3; i++) {
			int ordinal = r.nextInt(ordinalList.size());
			finalPaiList.addAll(ordinalListMap.get(ordinal));
			ordinalList.remove(ordinal);
		}
		currentPan.setAvaliablePaiList(finalPaiList);
		seed++;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public ChaPai getChapai() {
		return chapai;
	}

	public void setChapai(ChaPai chapai) {
		this.chapai = chapai;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
