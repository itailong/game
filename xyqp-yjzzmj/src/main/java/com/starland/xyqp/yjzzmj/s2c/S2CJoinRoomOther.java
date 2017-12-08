package com.starland.xyqp.yjzzmj.s2c;

public class S2CJoinRoomOther {

	/**
	 * 用户编号
	 */
	private int userId;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 微信对应的openId
	 */
	private String openId;
	
	/**
	 * 0未设置，1男，2女
	 */
	private int sex;
	
	/**
	 * 头像
	 */
	private String headImg;
	
	/**
	 * 钻石
	 */
	private int diamond;
	
	/**
	 * 座位位置
	 */
	private int seatPosition;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public int getSeatPosition() {
		return seatPosition;
	}

	public void setSeatPosition(int seatPosition) {
		this.seatPosition = seatPosition;
	}
	
}
