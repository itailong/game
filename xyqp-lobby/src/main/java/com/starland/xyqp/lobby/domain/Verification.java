package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Verification implements Serializable {

	/**
	 * id 主键
	 */
	private Integer id;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 验证码
	 */
	private String verification;
	
	/**
	 * id 主键
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * id 主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 手机号码
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * 手机号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * 验证码
	 */
	public String getVerification() {
		return verification;
	}
	
	/**
	 * 验证码
	 */
	public void setVerification(String verification) {
		this.verification = verification;
	}
	
}
