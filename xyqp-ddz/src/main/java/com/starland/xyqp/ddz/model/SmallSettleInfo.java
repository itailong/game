package com.starland.xyqp.ddz.model;

public class SmallSettleInfo {

	/**
	 * 分数
	 */
	private int score;
	
	/**
	 * 是否是地主
	 */
	private boolean landlord;
	
	/**
	 * 是否赢了
	 * 
	 */
	private boolean win;
	
	/**
	 * 倍数
	 */
	private int multiple;
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isLandlord() {
		return landlord;
	}

	public void setLandlord(boolean landlord) {
		this.landlord = landlord;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

}
