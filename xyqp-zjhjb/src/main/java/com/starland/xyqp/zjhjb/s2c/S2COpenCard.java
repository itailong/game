package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

public class S2COpenCard {

	/**
	 * 赢的位置
	 */
	private List<Integer> winPosition;
	
	/**
	 * 输的位置
	 */
	private List<Integer> losePositon;
	
	/**
	 * 桌上的总筹码
	 */
	private int totalChips;
	
	/**
	 * 金币
	 */
	private int surplusGold;
	
	/**
	 * 金币变化的位置 
	 */
	private int goldPosition;
	
	/**
	 * 金币变化量
	 */
	private int changeGold;
	
	/**
	 * 玩家下的总注数
	 */
	private int totalAnte;

	public List<Integer> getWinPosition() {
		return winPosition;
	}

	public void setWinPosition(List<Integer> winPosition) {
		this.winPosition = winPosition;
	}

	public List<Integer> getLosePositon() {
		return losePositon;
	}

	public void setLosePositon(List<Integer> losePositon) {
		this.losePositon = losePositon;
	}

	public int getTotalChips() {
		return totalChips;
	}

	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	public int getSurplusGold() {
		return surplusGold;
	}

	public void setSurplusGold(int surplusGold) {
		this.surplusGold = surplusGold;
	}

	public int getGoldPosition() {
		return goldPosition;
	}

	public void setGoldPosition(int goldPosition) {
		this.goldPosition = goldPosition;
	}

	public int getChangeGold() {
		return changeGold;
	}

	public void setChangeGold(int changeGold) {
		this.changeGold = changeGold;
	}

	public int getTotalAnte() {
		return totalAnte;
	}

	public void setTotalAnte(int totalAnte) {
		this.totalAnte = totalAnte;
	}
}
