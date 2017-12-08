package com.starland.xyqp.pdk.model;

import java.io.Serializable;

public class PlayWay implements Serializable {

	private static final long serialVersionUID = 1342201302456648876L;

	/**
	 * 牌数，15,16
	 */
	private int cardCount;
	
	/**
	 * 人数，2,3
	 */
	private int personCount;
	
	/**
	 * 局数，10,20
	 */
	private int roundCount;
	
	/**
	 * 是否扎鸟
	 */
	private boolean zhaniao;
	
	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	public int getPersonCount() {
		return personCount;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public boolean isZhaniao() {
		return zhaniao;
	}

	public void setZhaniao(boolean zhaniao) {
		this.zhaniao = zhaniao;
	}

}
