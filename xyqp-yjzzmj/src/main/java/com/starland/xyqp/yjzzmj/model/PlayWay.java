package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;

/**
 * 玩法
 */
public class PlayWay implements Serializable {
	
	private static final long serialVersionUID = 1939254416914855900L;

	/**
	 * 局数，6局，12局
	 */
	private int roundNum;
	
	/**
	 * 是否有最大番数
	 */
	private boolean hasMultipleMax;
	
	/**
	 * 是否有门清
	 */
	private boolean hasMenqing;
	
	/**
	 * 门清将将胡，是否可接炮
	 */
	private boolean mqjjh;
	
	/**
	 * 一字撬，是否有喜
	 */
	private boolean yiziqiao;
	
	/**
	 * 是否扎鸟
	 */
	private boolean zhaniao = true;

	public int getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(int roundNum) {
		this.roundNum = roundNum;
	}

	public boolean isHasMultipleMax() {
		return hasMultipleMax;
	}

	public void setHasMultipleMax(boolean hasMultipleMax) {
		this.hasMultipleMax = hasMultipleMax;
	}

	public boolean isHasMenqing() {
		return hasMenqing;
	}

	public void setHasMenqing(boolean hasMenqing) {
		this.hasMenqing = hasMenqing;
	}

	public boolean isMqjjh() {
		return mqjjh;
	}

	public void setMqjjh(boolean mqjjh) {
		this.mqjjh = mqjjh;
	}

	public boolean isYiziqiao() {
		return yiziqiao;
	}

	public void setYiziqiao(boolean yiziqiao) {
		this.yiziqiao = yiziqiao;
	}

	public boolean isZhaniao() {
		return zhaniao;
	}

	public void setZhaniao(boolean zhaniao) {
		this.zhaniao = zhaniao;
	}
	
}
