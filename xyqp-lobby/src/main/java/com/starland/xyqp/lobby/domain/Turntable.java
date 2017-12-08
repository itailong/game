package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Turntable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id  主键
	 */
	private Integer id;
	
	/**
	 * 用户id  
	 */
	private Integer userId;
	
	/**
	 * 每日分享次数 
	 */
	private Integer share;
	
	/**
	 * 
	 */
	private Integer shareNum;
	
	/**
	 * 抽奖次数
	 */
	private Integer lottery;
	
	/**
	 * 获得的奖品
	 */
	private Integer prize;
	
	/**
	 * 雀神是否抽过奖
	 */
	private Integer sparrowShare;
	
	/**
	 * 雀神次数
	 */
	private Integer sparrowNum;
	
	/**
	 * 周雀神次数
	 */
	private Integer weekSparrow;
	
	/**
	 * 月雀神次数
	 */
	private Integer monthSparrow;
	
	/**
	 * 周负分次数
	 */
	private Integer weekNegative;
	
	/**
	 * 月负分次数
	 */
	private Integer monthNegative;
	
	/**
	 * 周消费房卡数
	 */
	private Integer weekRoomcard;
	
	/**
	 * 月消费房卡数
	 */
	private Integer monthRoomcard;
	
	/**
	 * id  主键
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * id  主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 用户id  
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户id  
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 每日分享次数 
	 */
	public Integer getShare() {
		return share;
	}
	
	/**
	 * 每日分享次数 
	 */
	public void setShare(Integer share) {
		this.share = share;
	}
	
	/**
	 * 
	 */
	public Integer getShareNum() {
		return shareNum;
	}
	
	/**
	 * 
	 */
	public void setShareNum(Integer shareNum) {
		this.shareNum = shareNum;
	}
	
	/**
	 * 抽奖次数
	 */
	public Integer getLottery() {
		return lottery;
	}
	
	/**
	 * 抽奖次数
	 */
	public void setLottery(Integer lottery) {
		this.lottery = lottery;
	}
	
	/**
	 * 获得的奖品
	 */
	public Integer getPrize() {
		return prize;
	}
	
	/**
	 * 获得的奖品
	 */
	public void setPrize(Integer prize) {
		this.prize = prize;
	}
	
	/**
	 * 雀神是否抽过奖
	 */
	public Integer getSparrowShare() {
		return sparrowShare;
	}
	
	/**
	 * 雀神是否抽过奖
	 */
	public void setSparrowShare(Integer sparrowShare) {
		this.sparrowShare = sparrowShare;
	}
	
	/**
	 * 雀神次数
	 */
	public Integer getSparrowNum() {
		return sparrowNum;
	}
	
	/**
	 * 雀神次数
	 */
	public void setSparrowNum(Integer sparrowNum) {
		this.sparrowNum = sparrowNum;
	}
	
	/**
	 * 周雀神次数
	 */
	public Integer getWeekSparrow() {
		return weekSparrow;
	}
	
	/**
	 * 周雀神次数
	 */
	public void setWeekSparrow(Integer weekSparrow) {
		this.weekSparrow = weekSparrow;
	}
	
	/**
	 * 月雀神次数
	 */
	public Integer getMonthSparrow() {
		return monthSparrow;
	}
	
	/**
	 * 月雀神次数
	 */
	public void setMonthSparrow(Integer monthSparrow) {
		this.monthSparrow = monthSparrow;
	}
	
	/**
	 * 
	 */
	public Integer getWeekNegative() {
		return weekNegative;
	}
	
	/**
	 * 
	 */
	public void setWeekNegative(Integer weekNegative) {
		this.weekNegative = weekNegative;
	}
	
	/**
	 * 月负分次数
	 */
	public Integer getMonthNegative() {
		return monthNegative;
	}
	
	/**
	 * 月负分次数
	 */
	public void setMonthNegative(Integer monthNegative) {
		this.monthNegative = monthNegative;
	}
	
	/**
	 * 周消费房卡数
	 */
	public Integer getWeekRoomcard() {
		return weekRoomcard;
	}
	
	/**
	 * 周消费房卡数
	 */
	public void setWeekRoomcard(Integer weekRoomcard) {
		this.weekRoomcard = weekRoomcard;
	}
	
	/**
	 * 月消费房卡数
	 */
	public Integer getMonthRoomcard() {
		return monthRoomcard;
	}
	
	/**
	 * 月消费房卡数
	 */
	public void setMonthRoomcard(Integer monthRoomcard) {
		this.monthRoomcard = monthRoomcard;
	}
	
}
