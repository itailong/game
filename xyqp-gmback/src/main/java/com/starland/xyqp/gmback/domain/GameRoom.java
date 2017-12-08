package com.starland.xyqp.gmback.domain;

import java.io.Serializable;

public class GameRoom implements Serializable {

	/**
	 * 房间编号
	 */
	private String id;
	
	/**
	 * 底分
	 */
	private Integer score;
	
	/**
	 * 局数
	 */
	private Integer roundCount;
	
	/**
	 * 最大人数
	 */
	private Integer maxPerson;
	
	/**
	 * 当前人数
	 */
	private Integer currentPerson;
	
	/**
	 * 服务器编号
	 */
	private Integer serverId;
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 创建人编号
	 */
	private Integer creatorId;
	
	/**
	 * 是否为替人开房，0不是，1是
	 */
	private Integer instead;
	
	/**
	 * 房间编号
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 房间编号
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 底分
	 */
	public Integer getScore() {
		return score;
	}
	
	/**
	 * 底分
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	
	/**
	 * 局数
	 */
	public Integer getRoundCount() {
		return roundCount;
	}
	
	/**
	 * 局数
	 */
	public void setRoundCount(Integer roundCount) {
		this.roundCount = roundCount;
	}
	
	/**
	 * 最大人数
	 */
	public Integer getMaxPerson() {
		return maxPerson;
	}
	
	/**
	 * 最大人数
	 */
	public void setMaxPerson(Integer maxPerson) {
		this.maxPerson = maxPerson;
	}
	
	/**
	 * 当前人数
	 */
	public Integer getCurrentPerson() {
		return currentPerson;
	}
	
	/**
	 * 当前人数
	 */
	public void setCurrentPerson(Integer currentPerson) {
		this.currentPerson = currentPerson;
	}
	
	/**
	 * 服务器编号
	 */
	public Integer getServerId() {
		return serverId;
	}
	
	/**
	 * 服务器编号
	 */
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	
	/**
	 * 游戏名称
	 */
	public String getGameName() {
		return gameName;
	}
	
	/**
	 * 游戏名称
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	/**
	 * 创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 创建人编号
	 */
	public Integer getCreatorId() {
		return creatorId;
	}
	
	/**
	 * 创建人编号
	 */
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	
	/**
	 * 是否为替人开房，0不是，1是
	 */
	public Integer getInstead() {
		return instead;
	}
	
	/**
	 * 是否为替人开房，0不是，1是
	 */
	public void setInstead(Integer instead) {
		this.instead = instead;
	}
	
}
