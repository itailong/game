package com.starland.xyqp.ddz.model;

import java.util.List;

public class HintParam {

	/**
	 * 上家出的牌型
	 */
	private CardShape cardShape;
	
	/**
	 * 手上的牌
	 */
	private List<Integer> cardList;

	public CardShape getCardShape() {
		return cardShape;
	}

	public void setCardShape(CardShape cardShape) {
		this.cardShape = cardShape;
	}

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}
	
}
