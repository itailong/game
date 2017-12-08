package com.starland.xyqp.pdk.model;

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
	 * 三带一张
	 */
	public static final int TYPE_THREE_ONE = 4;
	
	/**
	 * 三带两张
	 */
	public static final int TYPE_THREE_TWO = 5;
	
	/**
	 * 炸弹
	 */
	public static final int TYPE_BOMB = 8;
	
	/**
	 * 连对
	 */
	public static final int TYPE_CONTINUE_PAIR = 11;
	
	/**
	 * 飞机
	 */
	public static final int TYPE_PLANE = 12;
	
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
	
	/**
	 * 牌是否完整
	 */
	private boolean complete;
	
	public int getWeightCode() {
		return weightCode;
	}

	public void setWeightCode(int weightCode) {
		this.weightCode = weightCode;
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

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	@Override
	public String toString() {
		return "CardShape [weightCode=" + weightCode + ", type=" + type + ", continueSize=" + continueSize + "]";
	}

}
