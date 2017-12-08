package com.starland.xyqp.zjhjb.model;

import java.util.List;

public class CardShape {

	/**
	 * 散牌
	 */
	public static final int TYPE_SCATTER = 1;
	
	/**
	 * 对子
	 */
	public static final int TYPE_PAIR = 2;
	
	/**
	 * 顺子
	 */
	public static final int TYPE_SHUNZI = 3;
	
	/**
	 * 同花
	 */
	public static final int TYPE_TONGHUA = 4;
	
	/**
	 * 顺金
	 */
	public static final int TYPE_SHUNJIN = 5;
	
	/**
	 * 豹子
	 */
	public static final int TYPE_BAOZI = 6;
	
	/**
	 * 特殊牌型
	 */
	public static final int TYPE_TESHU = 7;
	
	/**
	 * 牌的集合
	 */
	private List<Integer> cardList;
	
	/**
	 * 牌的类型
	 */
	private int type;
	
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
