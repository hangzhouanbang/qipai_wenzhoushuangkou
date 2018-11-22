package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.NoZhadanDanGeDianShuZuComparator;
import com.dml.puke.wanfa.dianshu.dianshuzu.comparator.TongDengLianXuDianShuZuComparator;
import com.dml.shuangkou.BianXingWanFa;
import com.dml.shuangkou.gameprocess.FixedPanNumbersJuFinishiDeterminer;
import com.dml.shuangkou.gameprocess.OnePlayerHasPaiPanFinishiDeterminer;
import com.dml.shuangkou.ju.Ju;
import com.dml.shuangkou.pan.PanActionFrame;
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
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();

	public PanActionFrame createJuAndStartFirstPan(Long currentTime) throws Exception {
		Ju ju = new Ju();
		ju.setCurrentPanFinishiDeterminer(new OnePlayerHasPaiPanFinishiDeterminer());
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer());
		// TODO 生成盘结果
		// ju.setCurrentPanResultBuilder(currentPanResultBuilder);
		// TODO 生成局结果
		// ju.setJuResultBuilder(juResultBuilder);
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
		// ju.setYaPaiSolutionsTipsFilter(yaPaiSolutionsTipsFilter);
		ju.setAllKedaPaiSolutionsGenerator(new WenzhouShuangkouAllKedaPaiSolutionsGenerator());
		// TODO 生成外号
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
		// 开始第一盘
		ju.startFirstPan(allPlayerIds(), currentTime);
		return ju.getCurrentPan().findLatestActionFrame();
	}

	public PukeActionResult action(String playerId, List<Integer> paiIds, String dianshuZuheIdx, long actionTime)
			throws Exception {
		PanActionFrame panActionFrame = null;
		if (paiIds.isEmpty()) {
			panActionFrame = ju.guo(playerId, actionTime);
		} else {
			panActionFrame = ju.da(playerId, paiIds, dianshuZuheIdx, actionTime);
		}
		PukeActionResult result = new PukeActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		checkAndFinishPan();

		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			WenzhouShuangkouPanResult panResult = (WenzhouShuangkouPanResult) ju.findLatestFinishedPanResult();

			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((WenzhouShuangkouJuResult) ju.getJuResult());
			}
		}
		result.setPukeGame(new PukeGameValueObject(this));
		return result;
	}

	@Override
	protected boolean checkToFinishGame() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void startNextPan() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToExtendedVotingState() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToVoteNotPassStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends GameValueObject> T toValueObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(long currentTime) throws Exception {
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	public void finish() throws Exception {
		// TODO Auto-generated method stub

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

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

}
