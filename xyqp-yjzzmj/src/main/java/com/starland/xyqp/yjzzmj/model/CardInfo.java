package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.List;

public class CardInfo implements Serializable {

	private static final long serialVersionUID = 6735859738351986135L;

	/**
	 * 是否能胡
	 */
	private boolean canWin;
	
	/**
	 * 是否能碰
	 */
	private boolean canBump;
	
	/**
	 * 是否能杠
	 */
	private boolean canBridge;
	
	/**
	 * 可以杠牌的列表
	 */
	private List<Integer> bridgeList;
	
	/**
	 * 是否能听
	 */
	private boolean canHear;
	
	/**
	 * 听牌列表
	 */
	private List<HearInfo> hearInfoList;
	
	public boolean isCanWin() {
		return canWin;
	}

	public void setCanWin(boolean canWin) {
		this.canWin = canWin;
	}

	public boolean isCanBump() {
		return canBump;
	}

	public void setCanBump(boolean canBump) {
		this.canBump = canBump;
	}

	public boolean isCanBridge() {
		return canBridge;
	}

	public void setCanBridge(boolean canBridge) {
		this.canBridge = canBridge;
	}

	public List<Integer> getBridgeList() {
		return bridgeList;
	}

	public void setBridgeList(List<Integer> bridgeList) {
		this.bridgeList = bridgeList;
	}

	public boolean isCanHear() {
		return canHear;
	}

	public void setCanHear(boolean canHear) {
		this.canHear = canHear;
	}

	public List<HearInfo> getHearInfoList() {
		return hearInfoList;
	}

	public void setHearInfoList(List<HearInfo> hearInfoList) {
		this.hearInfoList = hearInfoList;
	}

}
