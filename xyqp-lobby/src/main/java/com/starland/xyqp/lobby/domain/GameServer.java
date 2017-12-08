package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class GameServer implements Serializable {

	private static final long serialVersionUID = -9161698439789847106L;

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
	 * websocket的服务器端口
	 */
	private Integer webServerPort;
	
	/**
	 * 是否可用，0不可用，1可用
	 */
	private Integer enable;
	
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

	public Integer getWebServerPort() {
		return webServerPort;
	}

	public void setWebServerPort(Integer webServerPort) {
		this.webServerPort = webServerPort;
	}

	/**
	 * 是否可用，0不可用，1可用
	 */
	public Integer getEnable() {
		return enable;
	}

	/**
	 * 是否可用，0不可用，1可用
	 */
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	
}
