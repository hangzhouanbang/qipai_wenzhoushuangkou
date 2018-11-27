package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.ju.JuResult;
import com.dml.shuangkou.ju.JuResultBuilder;
import com.dml.shuangkou.pan.PanResult;

public class WenzhouShuangkouJuResultBuilder implements JuResultBuilder {

	@Override
	public JuResult buildJuResult(Ju ju) {
		WenzhouShuangkouJuResult wenzhouShuangkouJuResult = new WenzhouShuangkouJuResult();
		wenzhouShuangkouJuResult.setFinishedPanCount(ju.countFinishedPan());
		if (ju.countFinishedPan() > 0) {
			Map<String, WenzhouShuangkouJuPlayerResult> juPlayerResultMap = new HashMap<>();
			for (PanResult panResult : ju.getFinishedPanResultList()) {
				WenzhouShuangkouPanResult wenzhouShuangkouPanResult = (WenzhouShuangkouPanResult) panResult;
				for (WenzhouShuangkouPanPlayerResult panPlayerResult : wenzhouShuangkouPanResult
						.getPanPlayerResultList()) {
					WenzhouShuangkouJuPlayerResult juPlayerResult = juPlayerResultMap
							.get(panPlayerResult.getPlayerId());
					if (juPlayerResult == null) {
						juPlayerResult = new WenzhouShuangkouJuPlayerResult();
						juPlayerResult.setPlayerId(panPlayerResult.getPlayerId());
						juPlayerResultMap.put(panPlayerResult.getPlayerId(), juPlayerResult);
					}
					WenzhouShuangkouMingcifen mingcifen = panPlayerResult.getMingcifen();
					if (mingcifen.isShuangkou()) {
						juPlayerResult.increaseShuangkouCount();
					}
					if (mingcifen.isDankou()) {
						juPlayerResult.increaseDankouCount();
					}
					if (mingcifen.isPingkou()) {
						juPlayerResult.increasePingkouCount();
					}
					juPlayerResult.tryAndUpdateMaxXianshu(panPlayerResult.getXianshubeishu().getValue());
					juPlayerResult.increaseTotalScore(panPlayerResult.getScore());
				}
			}

			WenzhouShuangkouJuPlayerResult dayingjia = null;
			WenzhouShuangkouJuPlayerResult datuhao = null;
			for (WenzhouShuangkouJuPlayerResult juPlayerResult : juPlayerResultMap.values()) {
				if (dayingjia == null) {
					dayingjia = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() > dayingjia.getTotalScore()) {
						dayingjia = juPlayerResult;
					}
				}

				if (datuhao == null) {
					datuhao = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() < datuhao.getTotalScore()) {
						datuhao = juPlayerResult;
					}
				}
			}
			wenzhouShuangkouJuResult.setDatuhaoId(datuhao.getPlayerId());
			wenzhouShuangkouJuResult.setDayingjiaId(dayingjia.getPlayerId());
			wenzhouShuangkouJuResult.setPlayerResultList(new ArrayList<>(juPlayerResultMap.values()));
		}
		return wenzhouShuangkouJuResult;
	}

}
