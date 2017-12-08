package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.List;

public class JudgerParam implements Serializable {
	
	private static final long serialVersionUID = -7214999526655427338L;

	/**
	 * 胡的那张牌
	 */
	private Integer card;

	/**
	 * 手上的的牌
	 */
	private List<Integer> cardList;
	
	/**
	 * 碰牌的列表
	 */
	private List<Integer> bumpList;
	
	/**
	 * 明杠的列表
	 */
	private List<Integer> showBridgeList;
	
	/**
	 * 暗杠的列表
	 */
	private List<Integer> hideBridgeList;
	
	/**
	 * 过路杠（碰后杠）的列表
	 */
	private List<Integer> passBridgeList;
	
	/**
	 * 是否天胡
	 */
	private boolean skyWin;
	
	/**
	 * 是否为海底
	 */
	private boolean seafloor;
	
	/**
	 * 是否为杠上开花
	 */
	private boolean bridgeWin;
	
	/**
	 * 是否为抢杠
	 */
	private boolean grabBridge;
	
	/**
	 * 是否是自摸
	 */
	private boolean takeSelf;
	
	/**
	 * 是否听牌
	 */
	private boolean hear;
	
	/**
	 * 玩法参数
	 */
	private PlayWay playWay;

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public Integer getCard() {
		return card;
	}

	public void setCard(Integer card) {
		this.card = card;
	}

	public boolean isSeafloor() {
		return seafloor;
	}

	public void setSeafloor(boolean seafloor) {
		this.seafloor = seafloor;
	}

	public boolean isBridgeWin() {
		return bridgeWin;
	}

	public void setBridgeWin(boolean bridgeWin) {
		this.bridgeWin = bridgeWin;
	}

	public boolean isGrabBridge() {
		return grabBridge;
	}

	public void setGrabBridge(boolean grabBridge) {
		this.grabBridge = grabBridge;
	}

	public List<Integer> getBumpList() {
		return bumpList;
	}

	public void setBumpList(List<Integer> bumpList) {
		this.bumpList = bumpList;
	}

	public List<Integer> getShowBridgeList() {
		return showBridgeList;
	}

	public void setShowBridgeList(List<Integer> showBridgeList) {
		this.showBridgeList = showBridgeList;
	}

	public List<Integer> getHideBridgeList() {
		return hideBridgeList;
	}

	public void setHideBridgeList(List<Integer> hideBridgeList) {
		this.hideBridgeList = hideBridgeList;
	}

	public List<Integer> getPassBridgeList() {
		return passBridgeList;
	}

	public void setPassBridgeList(List<Integer> passBridgeList) {
		this.passBridgeList = passBridgeList;
	}

	public boolean isTakeSelf() {
		return takeSelf;
	}

	public void setTakeSelf(boolean takeSelf) {
		this.takeSelf = takeSelf;
	}

	public boolean isSkyWin() {
		return skyWin;
	}

	public void setSkyWin(boolean skyWin) {
		this.skyWin = skyWin;
	}

	public PlayWay getPlayWay() {
		return playWay;
	}

	public void setPlayWay(PlayWay playWay) {
		this.playWay = playWay;
	}

	public boolean isHear() {
		return hear;
	}

	public void setHear(boolean hear) {
		this.hear = hear;
	}
	
}
