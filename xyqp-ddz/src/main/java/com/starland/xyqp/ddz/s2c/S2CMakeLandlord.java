package com.starland.xyqp.ddz.s2c;

import java.util.List;

public class S2CMakeLandlord {

	/**
	 * 地主的位置
	 */
	private int position;
	
	/**
	 * 底牌
	 */
	private List<Integer> floorCards;
	
	/**
	 * 叫地主的分数
	 */
	private int callScore;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Integer> getFloorCards() {
		return floorCards;
	}

	public void setFloorCards(List<Integer> floorCards) {
		this.floorCards = floorCards;
	}

	public int getCallScore() {
		return callScore;
	}

	public void setCallScore(int callScore) {
		this.callScore = callScore;
	}
	
}
