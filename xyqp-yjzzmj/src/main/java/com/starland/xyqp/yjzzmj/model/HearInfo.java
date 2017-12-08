package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.List;

public class HearInfo implements Serializable {

	private static final long serialVersionUID = -1131539118137260259L;

	/**
	 * 听牌时要打出去的牌
	 */
	private Integer card;
	
	/**
	 * 听牌的列表
	 */
	private List<Integer> cardList;
	
	public Integer getCard() {
		return card;
	}

	public void setCard(Integer card) {
		this.card = card;
	}

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

}
