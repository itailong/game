package com.starland.xyqp.gmback.query;

import java.io.Serializable;


public class UserQuery implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 
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
	 * 用户绑定的邀请码
	 */
	private Integer invitationCode;
	
	/**
	 * 邀请码解绑次数
	 */
	private Integer unbundled;
	
	/**
	 * 加入的房间号，若没有加入房间则为空
	 */
	private String roomId;
	
	/**
	 * 金币
	 */
	private Long gold;
	
	/**
	 * 上级用户编号
	 */
	private Integer upperId;
	
	/**
	 * 用户收益，单位元
	 */
	private java.math.BigDecimal money;
	
	/**
	 * 密码
	 */
	private String password;
	
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
	 * 
	 */
	public String getUnionId() {
		return unionId;
	}
	
	/**
	 * 
	 */
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
	
	/**
	 * 用户绑定的邀请码
	 */
	public Integer getInvitationCode() {
		return invitationCode;
	}
	
	/**
	 * 用户绑定的邀请码
	 */
	public void setInvitationCode(Integer invitationCode) {
		this.invitationCode = invitationCode;
	}
	
	/**
	 * 邀请码解绑次数
	 */
	public Integer getUnbundled() {
		return unbundled;
	}
	
	/**
	 * 邀请码解绑次数
	 */
	public void setUnbundled(Integer unbundled) {
		this.unbundled = unbundled;
	}
	
	/**
	 * 加入的房间号，若没有加入房间则为空
	 */
	public String getRoomId() {
		return roomId;
	}
	
	/**
	 * 加入的房间号，若没有加入房间则为空
	 */
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	/**
	 * 金币
	 */
	public Long getGold() {
		return gold;
	}
	
	/**
	 * 金币
	 */
	public void setGold(Long gold) {
		this.gold = gold;
	}
	
	/**
	 * 上级用户编号
	 */
	public Integer getUpperId() {
		return upperId;
	}
	
	/**
	 * 上级用户编号
	 */
	public void setUpperId(Integer upperId) {
		this.upperId = upperId;
	}
	
	/**
	 * 用户收益，单位元
	 */
	public java.math.BigDecimal getMoney() {
		return money;
	}
	
	/**
	 * 用户收益，单位元
	 */
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	/**
	 * 密码
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}