package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import com.dml.puke.pai.DianShu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanGeZhadanDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.DuiziDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LianduiDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.LiansanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.SanzhangDianShuZu;
import com.dml.puke.wanfa.dianshu.dianshuzu.ShunziDianShuZu;
import com.dml.puke.wanfa.dianshu.paizu.DianShuZuPaiZu;
import com.dml.shuangkou.pai.waihao.WaihaoGenerator;

public class ShuangkouWaihaoGenerator implements WaihaoGenerator {

	@Override
	public void generateWaihao(DianShuZuPaiZu dianShuZuPaiZu) {
		DianShuZu dianShuZu = dianShuZuPaiZu.getDianShuZu();
		// 单张
		if (dianShuZu instanceof DanzhangDianShuZu) {
			DianShu dianshu = ((DanzhangDianShuZu) dianShuZu).getDianShu();
			dianShuZuPaiZu.setWaihao(dianshu.name());
		}
		// 对子
		if (dianShuZu instanceof DuiziDianShuZu) {
			DianShu dianshu = ((DuiziDianShuZu) dianShuZu).getDianShu();
			dianShuZuPaiZu.setWaihao(2 + dianshu.name());
		}
		// 三张
		if (dianShuZu instanceof SanzhangDianShuZu) {
			DianShu dianshu = ((SanzhangDianShuZu) dianShuZu).getDianShu();
			dianShuZuPaiZu.setWaihao(3 + dianshu.name());
		}
		// 顺子
		if (dianShuZu instanceof ShunziDianShuZu) {
			dianShuZuPaiZu.setWaihao("shunzi");
		}
		// 连对
		if (dianShuZu instanceof LianduiDianShuZu) {
			dianShuZuPaiZu.setWaihao("jiemeidui");
		}
		// 连三张
		if (dianShuZu instanceof LiansanzhangDianShuZu) {
			dianShuZuPaiZu.setWaihao("santuobei");
		}
		// 单个炸弹
		if (dianShuZu instanceof DanGeZhadanDianShuZu) {
			DianShu dianshu = ((DanGeZhadanDianShuZu) dianShuZu).getDianShu();
			dianShuZuPaiZu.setWaihao(((DanGeZhadanDianShuZu) dianShuZu).getSize() + dianshu.name());
		}
		// 连续炸弹
		if (dianShuZu instanceof LianXuZhadanDianShuZu) {
			LianXuZhadanDianShuZu lianXuZhadanDianShuZu = (LianXuZhadanDianShuZu) dianShuZu;
			DianShu[] lianXuDianShuArray = lianXuZhadanDianShuZu.getLianXuDianShuArray();
			dianShuZuPaiZu
					.setWaihao(lianXuZhadanDianShuZu.getXianShu() + "xian" + lianXuDianShuArray.length + "lianzha");
		}
		// 王炸
		if (dianShuZu instanceof WangZhadanDianShuZu) {
			WangZhadanDianShuZu wangZhadanDianShuZu = (WangZhadanDianShuZu) dianShuZu;
			int xiaowangCount = wangZhadanDianShuZu.getXiaowangCount();
			int dawangCount = wangZhadanDianShuZu.getDawangCount();
			if (xiaowangCount + dawangCount == 4) {
				dianShuZuPaiZu.setWaihao("sanwangzha");
			} else if (xiaowangCount + dawangCount == 3) {
				dianShuZuPaiZu.setWaihao("sisiling");
			}
		}
	}
}
