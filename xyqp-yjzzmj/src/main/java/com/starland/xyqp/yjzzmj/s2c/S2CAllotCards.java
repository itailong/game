package com.starland.xyqp.yjzzmj.s2c;

import java.util.List;

public class S2CAllotCards {

	/**
	 * 庄家的位置
	 */
	private int bankerPosition;
	
	/**
	 * 牌的集合
	 */
	private List<Integer> cards;
	
	private int roundCount;

	private int surplusCardNum;
	
	public int getBankerPosition() {
		return bankerPosition;
	}

	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}

	public List<Integer> getCards() {
		return cards;
	}

	public void setCards(List<Integer> cards) {
		this.cards = cards;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public int getSurplusCardNum() {
		return surplusCardNum;
	}

	public void setSurplusCardNum(int surplusCardNum) {
		this.surplusCardNum = surplusCardNum;
	}

}
