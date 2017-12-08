package com.starland.xyqp.niuniujb.model;

import java.util.List;

public class CardShape {

	/**
	 * 无牛
	 */
	public static final int TYPE_WUNIU = 0;
	
	/**
	 * 牛一
	 */
	public static final int TYPE_NIUYI = 1;
	
	/**
	 * 牛二
	 */
	public static final int TYPE_NIUER = 2;
	
	/**
	 * 牛三
	 */
	public static final int TYPE_NIUSAN = 3;
	
	/**
	 * 牛四
	 */
	public static final int TYPE_NIUSI = 4;
	
	/**
	 * 牛五
	 */
	public static final int TYPE_NIUWU = 5;
	
	/**
	 * 牛六
	 */
	public static final int TYPE_NIULIU = 6;
	
	/**
	 * 牛七
	 */
	public static final int TYPE_NIUQI = 7;
	
	/**
	 * 牛八
	 */
	public static final int TYPE_NIUBA = 8;
	
	/**
	 * 牛九
	 */
	public static final int TYPE_NIUJIU = 9;
	
	/**
	 * 牛牛
	 */
	public static final int TYPE_NIUNIU = 10;
	
	/**
	 * 牌的类型
	 */
	private int type;
	
	/**
	 * 牌的集合
	 */
	private List<Integer> cardList;
	
	/**
	 * 倍数
	 */
	private int multiple;
	
	/**
	 * 最大牌
	 */
	private int maxCard;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getMaxCard() {
		return maxCard;
	}

	public void setMaxCard(int maxCard) {
		this.maxCard = maxCard;
	}
}
