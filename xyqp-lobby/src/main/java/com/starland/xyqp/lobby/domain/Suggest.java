package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Suggest implements Serializable {

	/**
	 * 主键    建议表
	 */
	private Integer id;
	
	/**
	 * 用户id 与用户id相对应
	 */
	private Integer userId;
	
	/**
	 * 建议的内容
	 */
	private String suggestContent;
	
	/**
	 * 时间
	 */
	private java.util.Date suggestDate;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 主键    建议表
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 主键    建议表
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 用户id 与用户id相对应
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户id 与用户id相对应
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 建议的内容
	 */
	public String getSuggestContent() {
		return suggestContent;
	}
	
	/**
	 * 建议的内容
	 */
	public void setSuggestContent(String suggestContent) {
		this.suggestContent = suggestContent;
	}
	
	/**
	 * 时间
	 */
	public java.util.Date getSuggestDate() {
		return suggestDate;
	}
	
	/**
	 * 时间
	 */
	public void setSuggestDate(java.util.Date suggestDate) {
		this.suggestDate = suggestDate;
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
	
}
