package com.starland.xyqp.pdk.s2c;

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
	
}
