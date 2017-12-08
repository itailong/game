package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.Date;

public class BigSettle implements Serializable {

	private static final long serialVersionUID = 3831277155652029900L;

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 游戏场次编号
	 */
	private Integer sceneId;
	
	/**
	 * 玩家编号
	 */
	private Integer userId;
	
	private String userName;
	
	/**
	 * 房间编号
	 */
	private String roomId;
	
	/**
	 * 分数
	 */
	private Integer score;
	
	/**
	 * 座位的位置
	 */
	private Integer position;
	
	/**
	 * 属性，json数据
	 */
	private String properties;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
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
	 * 游戏场次编号
	 */
	public Integer getSceneId() {
		return sceneId;
	}
	
	/**
	 * 游戏场次编号
	 */
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}
	
	/**
	 * 玩家编号
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 玩家编号
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 分数
	 */
	public Integer getScore() {
		return score;
	}
	
	/**
	 * 分数
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	
	/**
	 * 座位的位置
	 */
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * 座位的位置
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	/**
	 * 属性，json数据
	 */
	public String getProperties() {
		return properties;
	}
	
	/**
	 * 属性，json数据
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
