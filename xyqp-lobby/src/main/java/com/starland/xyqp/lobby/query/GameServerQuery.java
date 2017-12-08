package com.starland.xyqp.lobby.query;

import java.io.Serializable;


public class GameServerQuery implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 游戏类型
	 */
	private String gameType;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 
	 */
	private String serverIp;
	
	/**
	 * 服务器端口
	 */
	private Integer serverPort;
	
	/**
	 * 是否限制创建新房间，0不限制，1限制
	 */
	private Integer limitRoom;
	
	/**
	 * 限制创建房间的原因
	 */
	private String limitReason;
	
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
	 * 游戏类型
	 */
	public String getGameType() {
		return gameType;
	}
	
	/**
	 * 游戏类型
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	
	/**
	 * 名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 */
	public String getServerIp() {
		return serverIp;
	}
	
	/**
	 * 
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	/**
	 * 服务器端口
	 */
	public Integer getServerPort() {
		return serverPort;
	}
	
	/**
	 * 服务器端口
	 */
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * 是否限制创建新房间，0不限制，1限制
	 */
	public Integer getLimitRoom() {
		return limitRoom;
	}
	
	/**
	 * 是否限制创建新房间，0不限制，1限制
	 */
	public void setLimitRoom(Integer limitRoom) {
		this.limitRoom = limitRoom;
	}
	
	/**
	 * 限制创建房间的原因
	 */
	public String getLimitReason() {
		return limitReason;
	}
	
	/**
	 * 限制创建房间的原因
	 */
	public void setLimitReason(String limitReason) {
		this.limitReason = limitReason;
	}
	
}