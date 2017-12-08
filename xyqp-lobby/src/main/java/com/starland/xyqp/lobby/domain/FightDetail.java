package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class FightDetail implements Serializable {

	private static final long serialVersionUID = 4808809367659838604L;

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 战绩编号
	 */
	private Integer exploitsId;
	
	/**
	 * 用户编号
	 */
	private Integer userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 玩家位置
	 */
	private Integer position;
	
	/**
	 * 得分
	 */
	private Integer score;
	
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
	 * 战绩编号
	 */
	public Integer getExploitsId() {
		return exploitsId;
	}
	
	/**
	 * 战绩编号
	 */
	public void setExploitsId(Integer exploitsId) {
		this.exploitsId = exploitsId;
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
	 * 用户名
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 玩家位置
	 */
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * 玩家位置
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	/**
	 * 得分
	 */
	public Integer getScore() {
		return score;
	}
	
	/**
	 * 得分
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	
}
