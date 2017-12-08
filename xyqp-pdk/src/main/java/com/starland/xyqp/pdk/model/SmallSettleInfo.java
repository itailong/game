package com.starland.xyqp.pdk.model;

public class SmallSettleInfo {

	/**
	 * 炸弹分数变化量
	 */
	private int bombScore;
	
	/**
	 * 分数
	 */
	private int score;
	
	/**
	 * 是否赢了
	 */
	private boolean win;
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public int getBombScore() {
		return bombScore;
	}

	public void setBombScore(int bombScore) {
		this.bombScore = bombScore;
	}
}
