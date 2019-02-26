package com.anbang.qipai.wenzhoushuangkou.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.wenzhoushuangkou.cqrs.q.dbo.WenzhouShuangkouPanPlayerResultDbo;
import com.dml.shuangkou.player.ShuangkouPlayerValueObject;

public class PanResultVO {

	private List<WenzhouShuangkouPanPlayerResultVO> playerResultList;

	private boolean chaodi;

	private int panNo;

	private long finishTime;

	private PanActionFrameVO lastPanActionFrame;

	private GameInfoVO gameInfoVO;

	public PanResultVO() {

	}

	public PanResultVO(PanResultDbo panResultDbo, PukeGameDbo pukeGameDbo) {
		List<WenzhouShuangkouPanPlayerResultDbo> list = panResultDbo.getPlayerResultList();
		List<ShuangkouPlayerValueObject> players = panResultDbo.getPanActionFrame().getPanAfterAction()
				.getShuangkouPlayerList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> {
				ShuangkouPlayerValueObject shuangkouPlayer = null;
				for (ShuangkouPlayerValueObject player : players) {
					if (player.getId().equals(panPlayerResult.getPlayerId())) {
						shuangkouPlayer = player;
						break;
					}
				}
				playerResultList.add(new WenzhouShuangkouPanPlayerResultVO(
						pukeGameDbo.findPlayer(panPlayerResult.getPlayerId()), panPlayerResult, shuangkouPlayer));
			});
		}
		chaodi = panResultDbo.isChaodi();
		panNo = panResultDbo.getPanNo();
		finishTime = panResultDbo.getFinishTime();
		lastPanActionFrame = new PanActionFrameVO(panResultDbo.getPanActionFrame());
		gameInfoVO = new GameInfoVO(panResultDbo.getPukeGameInfoDbo());
	}

	public List<WenzhouShuangkouPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<WenzhouShuangkouPanPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public boolean isChaodi() {
		return chaodi;
	}

	public void setChaodi(boolean chaodi) {
		this.chaodi = chaodi;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public PanActionFrameVO getLastPanActionFrame() {
		return lastPanActionFrame;
	}

	public void setLastPanActionFrame(PanActionFrameVO lastPanActionFrame) {
		this.lastPanActionFrame = lastPanActionFrame;
	}

	public GameInfoVO getGameInfoVO() {
		return gameInfoVO;
	}

	public void setGameInfoVO(GameInfoVO gameInfoVO) {
		this.gameInfoVO = gameInfoVO;
	}

}
