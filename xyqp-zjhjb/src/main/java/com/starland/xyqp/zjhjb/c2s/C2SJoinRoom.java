package com.starland.xyqp.zjhjb.c2s;

public class C2SJoinRoom {

	/**
	 * 牌的类型
	 */
	private String type;
	
	/**
	 * 底注
	 */
	private int antes;
	
	/**
	 * 最低金币
	 */
	private int lowGold;

	public int getAntes() {
		return antes;
	}

	public void setAntes(int antes) {
		this.antes = antes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLowGold() {
		return lowGold;
	}

	public void setLowGold(int lowGold) {
		this.lowGold = lowGold;
	}
}
