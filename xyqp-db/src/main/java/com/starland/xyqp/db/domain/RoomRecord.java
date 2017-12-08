package com.starland.xyqp.db.domain;

import java.io.Serializable;

public class RoomRecord implements Serializable {

	private static final long serialVersionUID = 1409949159668773655L;

	/**
	 * 编号
	 */
	private Integer id;
	
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
	 * 创建人编号
	 */
	private Integer creatorId;
	
	/**
	 * 是否为替人开房，0不是，1是
	 */
	private Integer instead;
	
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
