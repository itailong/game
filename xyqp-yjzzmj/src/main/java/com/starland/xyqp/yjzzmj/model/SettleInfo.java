package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SettleInfo implements Serializable {

	private static final long serialVersionUID = -1333171920931848324L;

	/**
	 * 当前分数
	 */
	private int currentScore;
	
	/**
	 * 自摸数量
	 */
	private int winSelfNum;
	
	/**
	 * 接炮数量
	 */
	private int winOtherNum;
	
	/**
	 * 暗杠数量
	 */
	private int hideBridgeNum;
	
	/**
	 * 明杠(包括过路杠)数量
	 */
	private int showBridgeNum;
	
	/**
	 * 点杠数量
	 */
	private int pointBridgeNum;
	
	/**
	 * 点炮
	 */
	private int pointCannonNum;
	
	/**
	 * 结算详情列表
	 */
	private List<SettleDetail> settleDetails = new ArrayList<>();

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public List<SettleDetail> getSettleDetails() {
		return settleDetails;
	}

	public int getWinSelfNum() {
		return winSelfNum;
	}

	public void setWinSelfNum(int winSelfNum) {
		this.winSelfNum = winSelfNum;
	}

	public int getWinOtherNum() {
		return winOtherNum;
	}

	public void setWinOtherNum(int winOtherNum) {
		this.winOtherNum = winOtherNum;
	}

	public int getHideBridgeNum() {
		return hideBridgeNum;
	}

	public void setHideBridgeNum(int hideBridgeNum) {
		this.hideBridgeNum = hideBridgeNum;
	}

	public int getShowBridgeNum() {
		return showBridgeNum;
	}

	public void setShowBridgeNum(int showBridgeNum) {
		this.showBridgeNum = showBridgeNum;
	}

	public int getPointBridgeNum() {
		return pointBridgeNum;
	}

	public void setPointBridgeNum(int pointBridgeNum) {
		this.pointBridgeNum = pointBridgeNum;
	}

	public int getPointCannonNum() {
		return pointCannonNum;
	}

	public void setPointCannonNum(int pointCannonNum) {
		this.pointCannonNum = pointCannonNum;
	}

	public void setSettleDetails(List<SettleDetail> settleDetails) {
		this.settleDetails = settleDetails;
	}
	
}
