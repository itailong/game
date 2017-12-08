package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

import com.starland.xyqp.zjhjb.model.AnteNums;

public class S2COperate {

	/**
	 * 是否能跟注
	 */
	private boolean ante;
	
	/**
	 * 能否加注
	 */
	private boolean addAnte;
	
	/**
	 * 显示筹码数
	 */
	private List<AnteNums> addAnteNums;
	
	/**
	 * 可以比牌的座位号
	 */
	private List<Integer> positionList;
	
	/**
	 * 是否能比牌
	 */
	private boolean compareCard;
	
	/**
	 * 是否能放弃
	 */
	private boolean giveUp = true;
	
	/**
	 * 是否能看牌
	 */
	private boolean lookCard = true;
	
	/**
	 * 是否能开牌
	 */
	private boolean openCard;
	
	public boolean isAnte() {
		return ante;
	}

	public void setAnte(boolean ante) {
		this.ante = ante;
	}

	public boolean isAddAnte() {
		return addAnte;
	}

	public void setAddAnte(boolean addAnte) {
		this.addAnte = addAnte;
	}

	public boolean isCompareCard() {
		return compareCard;
	}

	public void setCompareCard(boolean compareCard) {
		this.compareCard = compareCard;
	}

	public boolean isGiveUp() {
		return giveUp;
	}

	public void setGiveUp(boolean giveUp) {
		this.giveUp = giveUp;
	}

	public boolean isLookCard() {
		return lookCard;
	}

	public void setLookCard(boolean lookCard) {
		this.lookCard = lookCard;
	}

	public boolean isOpenCard() {
		return openCard;
	}

	public void setOpenCard(boolean openCard) {
		this.openCard = openCard;
	}

	public List<AnteNums> getAddAnteNums() {
		return addAnteNums;
	}

	public void setAddAnteNums(List<AnteNums> addAnteNums) {
		this.addAnteNums = addAnteNums;
	}

	public List<Integer> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Integer> positionList) {
		this.positionList = positionList;
	}
}
