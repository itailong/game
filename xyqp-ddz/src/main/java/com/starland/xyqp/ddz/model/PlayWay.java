package com.starland.xyqp.ddz.model;

import java.io.Serializable;

public class PlayWay implements Serializable {

	private static final long serialVersionUID = 1342201302456648876L;

	/**
	 * 局数，6,12,20
	 */
	private int roundCount;
	
	/**
	 * 底牌数量，3,4
	 */
	private int floorCount;
	
	/**
	 * 最大炸弹数
	 */
	private int maxBomb;

	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public int getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(int floorCount) {
		this.floorCount = floorCount;
	}

	public int getMaxBomb() {
		return maxBomb;
	}

	public void setMaxBomb(int maxBomb) {
		this.maxBomb = maxBomb;
	}
	
}
