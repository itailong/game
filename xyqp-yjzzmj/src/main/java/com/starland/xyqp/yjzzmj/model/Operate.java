package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;

public class Operate implements Serializable {

	private static final long serialVersionUID = 4828415751537813819L;

	/**
	 * 是否是胡
	 */
	private boolean win;
	
	/**
	 * 是否是碰
	 */
	private boolean bump;
	
	/**
	 * 是否是杠
	 */
	private boolean bridge;
	
	/**
	 * 是否是过
	 */
	private boolean pass;
	
	/**
	 * 杠的那张牌
	 */
	private Integer bridgeCard;

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public boolean isBump() {
		return bump;
	}

	public void setBump(boolean bump) {
		this.bump = bump;
	}

	public boolean isBridge() {
		return bridge;
	}

	public void setBridge(boolean bridge) {
		this.bridge = bridge;
	}

	public Integer getBridgeCard() {
		return bridgeCard;
	}

	public void setBridgeCard(Integer bridgeCard) {
		this.bridgeCard = bridgeCard;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}
}
