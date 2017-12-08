package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;

public class SettleDetail implements Serializable {

	private static final long serialVersionUID = 6801489559588806921L;

	/**
	 * 分数的变化量
	 */
	private int offsetScore;
	
	/**
	 * 当前分数
	 */
	private int currentScore;
	
	/**
	 * 是否赢了
	 * 
	 */
	private boolean win;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 杠，报喜的分数
	 */
	private int bridgeScore;
	
	/**
	 * 胡牌的分数
	 */
	private int winScroe;

	public int getOffsetScore() {
		return offsetScore;
	}

	public void setOffsetScore(int offsetScore) {
		this.offsetScore = offsetScore;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public int getBridgeScore() {
		return bridgeScore;
	}

	public void setBridgeScore(int bridgeScore) {
		this.bridgeScore = bridgeScore;
	}

	public int getWinScroe() {
		return winScroe;
	}

	public void setWinScroe(int winScroe) {
		this.winScroe = winScroe;
	}
	
}
