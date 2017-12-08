package com.starland.xyqp.ddz.model;

public class CardShape {

	/**
	 * 单牌
	 */
	public static final int TYPE_SINGLE = 1;
	
	/**
	 * 对子
	 */
	public static final int TYPE_PAIR = 2;
	
	/**
	 * 三张牌
	 */
	public static final int TYPE_THREE = 3;
	
	/**
	 * 三带一
	 */
	public static final int TYPE_THREE_SINGLE = 4;
	
	/**
	 * 三带一对
	 */
	public static final int TYPE_THREE_PAIR = 5;
	
	/**
	 * 四带两个单牌
	 */
	public static final int TYPE_FOUR_SINGLE = 6;
	
	/**
	 * 四带两对
	 */
	public static final int TYPE_FOUR_PAIR = 7;
	
	/**
	 * 炸弹
	 */
	public static final int TYPE_BOMB = 8;
	
	/**
	 * 王炸
	 */
	public static final int TYPE_KING_BOMB = 9;
	
	/**
	 * 鬼牌
	 */
	public static final int TYPE_GHOST = 10;
	
	/**
	 * 连对
	 */
	public static final int TYPE_CONTINUE_PAIR = 11;
	
	/**
	 * 飞机
	 */
	public static final int TYPE_PLANE = 12;
	
	/**
	 * 飞机带单牌
	 */
	public static final int TYPE_PLANE_SINGLE = 13;
	
	/**
	 * 飞机带对子
	 */
	public static final int TYPE_PLANE_PAIR = 14;
	
	/**
	 * 顺子
	 */
	public static final int TYPE_STRAIGHT = 15;
	
	/**
	 * 决定大小的牌
	 */
	private int weightCode;
	
	/**
	 * 牌的类型
	 */
	private int type;
	
	/**
	 * 连续的个数，连对，顺子，飞机有效
	 */
	private int continueSize;
	
	public int getWeightCode() {
		return weightCode;
	}

	public void setWeightCode(int weightCard) {
		this.weightCode = weightCard;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getContinueSize() {
		return continueSize;
	}

	public void setContinueSize(int continueSize) {
		this.continueSize = continueSize;
	}

	@Override
	public String toString() {
		return "CardShape [weightCard=" + weightCode + ", type=" + type + ", continueSize=" + continueSize + "]";
	}

}
