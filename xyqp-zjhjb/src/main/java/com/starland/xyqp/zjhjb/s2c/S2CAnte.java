package com.starland.xyqp.zjhjb.s2c;

public class S2CAnte {

	/**
	 * 房间总注数
	 */
	private int totalChips;
	
	/**
	 * 金币变化量
	 */
	private int goldChange;
	
	/**
	 * 用户金币
	 */
	private int userGold;
	
	/**
	 * 玩家单局下的总注数
	 */
	private int totalAnte;
	
	/**
	 * 跟注
	 */
	private int position;
	
	/**
	 * 当前轮数
	 */
	private int roundNum;
	
	public int getTotalChips() {
		return totalChips;
	}

	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	public int getGoldChange() {
		return goldChange;
	}

	public void setGoldChange(int goldChange) {
		this.goldChange = goldChange;
	}

	public int getUserGold() {
		return userGold;
	}

	public void setUserGold(int userGold) {
		this.userGold = userGold;
	}

	public int getTotalAnte() {
		return totalAnte;
	}

	public void setTotalAnte(int totalAnte) {
		this.totalAnte = totalAnte;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(int roundNum) {
		this.roundNum = roundNum;
	}
}

