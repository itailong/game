package com.starland.xyqp.niuniujb.s2c;

import java.util.List;

public class S2COpenCard {

	/**
	 * 位置
	 */
	private int position;
	
	/**
	 * 分后的牌
	 */
	private List<Integer> cardList;
	
	/**
	 * 牌的类型
	 */
	private int type;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
