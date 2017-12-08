package com.starland.xyqp.db.query;

import java.io.Serializable;


public class DiamondConsumeQuery implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 用户编号
	 */
	private Integer userId;
	
	/**
	 * 房间编号
	 */
	private String roomId;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 服务器编号
	 */
	private Integer serverId;
	
	/**
	 * 消耗钻石数量
	 */
	private Integer consume;
	
	/**
	 * 局数
	 */
	private Integer roundCount;
	
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
	 * 房间编号
	 */
	public String getRoomId() {
		return roomId;
	}
	
	/**
	 * 房间编号
	 */
	public void setRoomId(String roomId) {
		this.roomId = roomId;
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
	 * 消耗钻石数量
	 */
	public Integer getConsume() {
		return consume;
	}
	
	/**
	 * 消耗钻石数量
	 */
	public void setConsume(Integer consume) {
		this.consume = consume;
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
	
}