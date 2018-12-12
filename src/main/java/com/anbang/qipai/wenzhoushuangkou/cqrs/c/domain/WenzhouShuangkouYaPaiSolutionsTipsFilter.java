package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import com.dml.shuangkou.player.action.da.YaPaiSolutionsTipsFilter;
import com.dml.shuangkou.player.action.da.solution.DaPaiDianShuSolution;

/**
 * 提示顺序： 没有王牌代替的打法、不拆炸弹、同种类型、
 * 
 * @author lsc
 *
 */
public class WenzhouShuangkouYaPaiSolutionsTipsFilter implements YaPaiSolutionsTipsFilter {

	private ZhadanComparator zhadanComparator;

	@Override
	public List<DaPaiDianShuSolution> filter(List<DaPaiDianShuSolution> YaPaiSolutions) {
		List<DaPaiDianShuSolution> filtedSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSolutionList = new ArrayList<>();
		List<DaPaiDianShuSolution> hasWangSolutionList = new ArrayList<>();
		f1: for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			for (DianShu dianshu : solution.getDachuDianShuArray()) {
				if (DianShu.xiaowang.equals(dianshu) || DianShu.dawang.equals(dianshu)) {
					hasWangSolutionList.add(solution);
					continue f1;
				}
			}
			noWangSolutionList.add(solution);
		}

		List<DaPaiDianShuSolution> danGeZhadanSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DanGeZhadanDianShuZu) {
				if (danGeZhadanSolutionList.isEmpty()) {
					danGeZhadanSolutionList.add(solution);
				} else {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) dianshuZu;
					int length = danGeZhadanSolutionList.size();
					boolean flag = false;
					for (int i = 0; i < length; i++) {
						DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) danGeZhadanSolutionList
								.get(i).getDianShuZu();
						if (danGeZhadanDianShuZu1.getDianShu().equals(danGeZhadanDianShuZu2.getDianShu())) {
							if (danGeZhadanDianShuZu1.getSize() > danGeZhadanDianShuZu2.getSize()) {
								danGeZhadanSolutionList.set(i, solution);
							}
							flag = true;
						}
					}
					if (!flag) {
						for (int i = 0; i < length; i++) {
							DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) danGeZhadanSolutionList
									.get(i).getDianShuZu();
							int compare = zhadanComparator.compare(danGeZhadanDianShuZu2, danGeZhadanDianShuZu1);
							if (compare > 0) {
								danGeZhadanSolutionList.add(i, solution);
								break;
							}
							if (i == length - 1) {
								danGeZhadanSolutionList.add(solution);
							}
						}
					}
				}
			}
		}
		LinkedList<DaPaiDianShuSolution> zhadanSolutionList = new LinkedList<>();
		for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
			if (zhadanSolutionList.isEmpty()) {
				zhadanSolutionList.add(zhadanSolution);
			} else {
				DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
				int length = zhadanSolutionList.size();
				for (int i = 0; i < length; i++) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu2 = (DanGeZhadanDianShuZu) zhadanSolutionList.get(i)
							.getDianShuZu();
					if (danGeZhadanDianShuZu1.getSize() < danGeZhadanDianShuZu2.getSize()) {
						zhadanSolutionList.add(i, zhadanSolution);
						break;
					}
					if (i == length - 1) {
						zhadanSolutionList.add(zhadanSolution);
					}
				}
			}
		}
		DaPaiDianShuSolution maxDanGeZhadanSolution = null;
		for (DaPaiDianShuSolution solution : YaPaiSolutions) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DanGeZhadanDianShuZu) {
				DanGeZhadanDianShuZu danGeZhadanDianShuZu1 = (DanGeZhadanDianShuZu) dianshuZu;
				if (maxDanGeZhadanSolution == null || zhadanComparator.compare(danGeZhadanDianShuZu1,
						(ZhadanDianShuZu) maxDanGeZhadanSolution.getDianShuZu()) > 0) {
					maxDanGeZhadanSolution = solution;
				}
			}
		}
		if (maxDanGeZhadanSolution != null) {
			if (!zhadanSolutionList.isEmpty()) {
				DaPaiDianShuSolution solution = zhadanSolutionList.getLast();
				if (!solution.getDianshuZuheIdx().equals(maxDanGeZhadanSolution.getDianshuZuheIdx())) {
					zhadanSolutionList.add(maxDanGeZhadanSolution);
				}
			} else {
				zhadanSolutionList.add(maxDanGeZhadanSolution);
			}
		}
		List<DaPaiDianShuSolution> noWangDanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangDuiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangSanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangShunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangLianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> noWangLiansanzhangSolutionList = new LinkedList<>();
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (noWangSanzhangSolutionList.isEmpty()) {
					noWangSanzhangSolutionList.add(solution);
				} else {
					int length = noWangSanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((SanzhangDianShuZu) noWangSanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(sanzhangDianShuZu.getDianShu()) > 0) {
							noWangSanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							noWangSanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (noWangDuiziSolutionList.isEmpty()) {
					noWangDuiziSolutionList.add(solution);
				} else {
					int length = noWangDuiziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DuiziDianShuZu) noWangDuiziSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(duiziDianShuZu.getDianShu()) > 0) {
							noWangDuiziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							noWangDuiziSolutionList.add(solution);
						}
						i++;
					}
				}
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
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : noWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (noWangDanzhangSolutionList.isEmpty()) {
					noWangDanzhangSolutionList.add(solution);
				} else {
					int length = noWangDanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DanzhangDianShuZu) noWangDanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(danzhangDianShuZu.getDianShu()) > 0) {
							noWangDanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							noWangDanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (noWangLiansanzhangSolutionList.isEmpty()) {
					noWangLiansanzhangSolutionList.add(solution);
				} else {
					int length = noWangLiansanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LiansanzhangDianShuZu) noWangLiansanzhangSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0].compareTo(
										((LiansanzhangDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							noWangLiansanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							noWangLiansanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (noWangLianduiSolutionList.isEmpty()) {
					noWangLianduiSolutionList.add(solution);
				} else {
					int length = noWangLianduiSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LianduiDianShuZu) noWangLianduiSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0]
										.compareTo(((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							noWangLianduiSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							noWangLianduiSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (noWangShunziSolutionList.isEmpty()) {
					noWangShunziSolutionList.add(solution);
				} else {
					int length = noWangShunziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((ShunziDianShuZu) noWangShunziSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0]
										.compareTo(((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							noWangShunziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							noWangShunziSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		List<DaPaiDianShuSolution> hasWangDanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangDuiziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangSanzhangSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangShunziSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangLianduiSolutionList = new LinkedList<>();
		List<DaPaiDianShuSolution> hasWangLiansanzhangSolutionList = new LinkedList<>();
		f2: for (DaPaiDianShuSolution solution : hasWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof SanzhangDianShuZu) {
				SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu1 = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu1.getDianShu().compareTo(sanzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (hasWangSanzhangSolutionList.isEmpty()) {
					hasWangSanzhangSolutionList.add(solution);
				} else {
					int length = hasWangSanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((SanzhangDianShuZu) hasWangSanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(sanzhangDianShuZu.getDianShu()) > 0) {
							hasWangSanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangSanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof DuiziDianShuZu) {
				DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					if (danGeZhadanDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : hasWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : noWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu1 = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu1.getDianShu().compareTo(duiziDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (hasWangDuiziSolutionList.isEmpty()) {
					hasWangDuiziSolutionList.add(solution);
				} else {
					int length = hasWangDuiziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DuiziDianShuZu) hasWangDuiziSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(duiziDianShuZu.getDianShu()) > 0) {
							hasWangDuiziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangDuiziSolutionList.add(solution);
						}
						i++;
					}
				}
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
				for (DaPaiDianShuSolution sanzhangSolution : hasWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : hasWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution sanzhangSolution : noWangSanzhangSolutionList) {
					SanzhangDianShuZu sanzhangDianShuZu = (SanzhangDianShuZu) sanzhangSolution.getDianShuZu();
					if (sanzhangDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				for (DaPaiDianShuSolution duiziSolution : noWangDuiziSolutionList) {
					DuiziDianShuZu duiziDianShuZu = (DuiziDianShuZu) duiziSolution.getDianShuZu();
					if (duiziDianShuZu.getDianShu().compareTo(danzhangDianShuZu.getDianShu()) == 0) {
						continue f2;
					}
				}
				if (hasWangDanzhangSolutionList.isEmpty()) {
					hasWangDanzhangSolutionList.add(solution);
				} else {
					int length = hasWangDanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((DanzhangDianShuZu) hasWangDanzhangSolutionList.get(i).getDianShuZu()).getDianShu()
								.compareTo(danzhangDianShuZu.getDianShu()) > 0) {
							hasWangDanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangDanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LiansanzhangDianShuZu) {
				LiansanzhangDianShuZu liansanzhangDianShuZu = (LiansanzhangDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : liansanzhangDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (hasWangLiansanzhangSolutionList.isEmpty()) {
					hasWangLiansanzhangSolutionList.add(solution);
				} else {
					int length = hasWangLiansanzhangSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LiansanzhangDianShuZu) hasWangLiansanzhangSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0].compareTo(
										((LiansanzhangDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							hasWangLiansanzhangSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangLiansanzhangSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof LianduiDianShuZu) {
				LianduiDianShuZu lianduiDianShuZu = (LianduiDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : lianduiDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (hasWangLianduiSolutionList.isEmpty()) {
					hasWangLianduiSolutionList.add(solution);
				} else {
					int length = hasWangLianduiSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((LianduiDianShuZu) hasWangLianduiSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0]
										.compareTo(((LianduiDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							hasWangLianduiSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangLianduiSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		f2: for (DaPaiDianShuSolution solution : noWangSolutionList) {
			DianShuZu dianshuZu = solution.getDianShuZu();
			if (dianshuZu instanceof ShunziDianShuZu) {
				ShunziDianShuZu shunziDianShuZu = (ShunziDianShuZu) dianshuZu;
				for (DaPaiDianShuSolution zhadanSolution : danGeZhadanSolutionList) {
					DanGeZhadanDianShuZu danGeZhadanDianShuZu = (DanGeZhadanDianShuZu) zhadanSolution.getDianShuZu();
					for (DianShu dianshu : shunziDianShuZu.getLianXuDianShuArray())
						if (danGeZhadanDianShuZu.getDianShu().compareTo(dianshu) == 0) {
							continue f2;
						}
				}
				if (hasWangShunziSolutionList.isEmpty()) {
					hasWangShunziSolutionList.add(solution);
				} else {
					int length = hasWangShunziSolutionList.size();
					int i = 0;
					while (i < length) {
						if (((ShunziDianShuZu) hasWangShunziSolutionList.get(i).getDianShuZu())
								.getLianXuDianShuArray()[0]
										.compareTo(((ShunziDianShuZu) dianshuZu).getLianXuDianShuArray()[0]) > 0) {
							hasWangShunziSolutionList.add(i, solution);
							break;
						}
						if (i == length - 1) {
							hasWangShunziSolutionList.add(solution);
						}
						i++;
					}
				}
			}
		}
		filtedSolutionList.addAll(noWangDanzhangSolutionList);
		filtedSolutionList.addAll(noWangDuiziSolutionList);
		filtedSolutionList.addAll(noWangSanzhangSolutionList);
		filtedSolutionList.addAll(noWangShunziSolutionList);
		filtedSolutionList.addAll(noWangLianduiSolutionList);
		filtedSolutionList.addAll(noWangLiansanzhangSolutionList);
		filtedSolutionList.addAll(hasWangDanzhangSolutionList);
		filtedSolutionList.addAll(hasWangDuiziSolutionList);
		filtedSolutionList.addAll(hasWangSanzhangSolutionList);
		filtedSolutionList.addAll(hasWangShunziSolutionList);
		filtedSolutionList.addAll(hasWangLianduiSolutionList);
		filtedSolutionList.addAll(hasWangLiansanzhangSolutionList);
		filtedSolutionList.addAll(zhadanSolutionList);
		return filtedSolutionList;
	}

	public ZhadanComparator getZhadanComparator() {
		return zhadanComparator;
	}

	public void setZhadanComparator(ZhadanComparator zhadanComparator) {
		this.zhadanComparator = zhadanComparator;
	}

}
