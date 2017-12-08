package com.starland.xyqp.ddz.s2c;

public class S2CLoginByToken {

	/**
	 * 状态码
	 */
	private int code;
	
	/**
	 * 错误消息
	 */
	private String msg;
	
	/**
	 * 用户编号
	 */
	private int userId;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 登录token
	 */
	private String token;
	
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
	 * IP地址
	 */
	private String ip;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
