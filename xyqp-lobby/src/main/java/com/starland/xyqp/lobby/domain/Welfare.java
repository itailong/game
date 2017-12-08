package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Welfare implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 用户编号
	 */
	private Integer userId;
	
	/**
	 * 进度，0代表重未领取过，1代表领了一次
	 */
	private Integer progress;
	
	/**
	 * 最后一次领取的时间
	 */
	private java.util.Date lastTime;
	
	/**
	 * 编号
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 编号
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 用户编号
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户编号
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 进度，0代表重未领取过，1代表领了一次
	 */
	public Integer getProgress() {
		return progress;
	}
	
	/**
	 * 进度，0代表重未领取过，1代表领了一次
	 */
	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
	/**
	 * 最后一次领取的时间
	 */
	public java.util.Date getLastTime() {
		return lastTime;
	}
	
	/**
	 * 最后一次领取的时间
	 */
	public void setLastTime(java.util.Date lastTime) {
		this.lastTime = lastTime;
	}
	
}
