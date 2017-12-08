package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 微信对应的unionId
	 */
	private String unionId;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 0未设置，1男，2女
	 */
	private Integer sex;
	
	/**
	 * 头像
	 */
	private String headImg;
	
	/**
	 * 钻石
	 */
	private Integer diamond;
	
	/**
	 * 金币
	 */
	private Long gold;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 登录token
	 */
	private String token;
	
	/**
	 * token的有效时间
	 */
	private java.util.Date tokenTime;
	
	/**
	 * 用户类型，1游客，2微信
	 */
	private Integer userType;
	
	/**
	 * 用户手机号码
	 */
	private String userPhone;
	
	/**
	 * 用户积分
	 */
	private Integer integral;
	
	/**
	 * 用户所在房间编号
	 */
	private String roomId;
	
	/**
	 * 上级代理商编号
	 */
	private Integer upperId;
	
	/**
	 * 临时上级代理商编号
	 */
	private Integer tempUpperId;
	
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
	
	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
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
	 * 0未设置，1男，2女
	 */
	public Integer getSex() {
		return sex;
	}
	
	/**
	 * 0未设置，1男，2女
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	/**
	 * 头像
	 */
	public String getHeadImg() {
		return headImg;
	}
	
	/**
	 * 头像
	 */
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
	/**
	 * 钻石
	 */
	public Integer getDiamond() {
		return diamond;
	}
	
	/**
	 * 钻石
	 */
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
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
	 * 登录token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * 登录token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * token的有效时间
	 */
	public java.util.Date getTokenTime() {
		return tokenTime;
	}
	
	/**
	 * token的有效时间
	 */
	public void setTokenTime(java.util.Date tokenTime) {
		this.tokenTime = tokenTime;
	}
	
	/**
	 * 用户类型，1游客，2微信
	 */
	public Integer getUserType() {
		return userType;
	}
	
	/**
	 * 用户类型，1游客，2微信
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	/**
	 * 用户手机号码
	 */
	public String getUserPhone() {
		return userPhone;
	}
	
	/**
	 * 用户手机号码
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	/**
	 * 用户积分
	 */
	public Integer getIntegral() {
		return integral;
	}
	
	/**
	 * 用户积分
	 */
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Integer getUpperId() {
		return upperId;
	}

	public void setUpperId(Integer upperId) {
		this.upperId = upperId;
	}

	public Integer getTempUpperId() {
		return tempUpperId;
	}

	public void setTempUpperId(Integer tempUpperId) {
		this.tempUpperId = tempUpperId;
	}

	public Long getGold() {
		return gold;
	}

	public void setGold(Long gold) {
		this.gold = gold;
	}
	
}
