package com.starland.xyqp.pdk.model;

public class BigSettleInfo {

	/**
	 * 分数
	 */
	private int score;
	
	/**
	 * 总炸弹数
	 */
	private int bombNums;
	
	/**
	 * 单局最高得分
	 */
	private int highPoint;
	
	/**
	 * 胜的局数
	 */
	private int winRound;
	
	
	public int getWinRound() {
		return winRound;
	}

	public void setWinRound(int winRound) {
		this.winRound = winRound;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getBombNums() {
		return bombNums;
	}

	public void setBombNums(int bombNums) {
		this.bombNums = bombNums;
	}

	public int getHighPoint() {
		return highPoint;
	}

	public void setHighPoint(int highPoint) {
		this.highPoint = highPoint;
	}
}
