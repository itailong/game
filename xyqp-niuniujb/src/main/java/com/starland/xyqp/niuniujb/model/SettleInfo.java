package com.starland.xyqp.niuniujb.model;

public class SettleInfo {

	/**
	 * 分数
	 */
	private int score;
	
	/**
	 * 是否是庄家
	 */
	private boolean banker;
	
	/**
	 * 是否赢了
	 */
	private boolean win = true;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isBanker() {
		return banker;
	}

	public void setBanker(boolean banker) {
		this.banker = banker;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}
}
