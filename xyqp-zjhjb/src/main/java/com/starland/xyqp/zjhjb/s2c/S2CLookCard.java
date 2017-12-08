package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

public class S2CLookCard {

	/**
	 * 牌的集合
	 */
	private List<Integer> cardList;
	
	/**
	 * 牌型
	 */
	private int cardType;

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
}
