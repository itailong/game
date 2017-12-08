package com.starland.xyqp.gmback.vo;

import java.io.Serializable;
import java.util.Set;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = -3569543716634738421L;
	
	/**
	 * 存在session中的key
	 */
	public static final String SESSION_KEY = "LOGIN_INFO";

	/**
	 * 用户的编号
	 */
	private Integer userId;
	
	/**
	 * 拥有权限的路径
	 */
	private Set<String> withinUrls;
	
	/**
	 * 没有权限的路径
	 */
	private Set<String> withoutUrls;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Set<String> getWithinUrls() {
		return withinUrls;
	}

	public void setWithinUrls(Set<String> withinUrls) {
		this.withinUrls = withinUrls;
	}

	public Set<String> getWithoutUrls() {
		return withoutUrls;
	}

	public void setWithoutUrls(Set<String> withoutUrls) {
		this.withoutUrls = withoutUrls;
	}
	
}
