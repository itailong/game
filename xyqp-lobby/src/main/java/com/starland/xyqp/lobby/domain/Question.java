package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Question implements Serializable {

	/**
	 * 问题表的id 主键  自动递增  
	 */
	private Integer id;
	
	/**
	 * 用户表的id  与用户表的id相对应
	 */
	private Integer userId;
	
	/**
	 * 建议的内容  
	 */
	private String questionContent;
	
	/**
	 * 日期  建议的日期 可以为空值
	 */
	private java.util.Date questionDate;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 问题表的id 主键  自动递增  
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 问题表的id 主键  自动递增  
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 用户表的id  与用户表的id相对应
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户表的id  与用户表的id相对应
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 建议的内容  
	 */
	public String getQuestionContent() {
		return questionContent;
	}
	
	/**
	 * 建议的内容  
	 */
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	
	/**
	 * 日期  建议的日期 可以为空值
	 */
	public java.util.Date getQuestionDate() {
		return questionDate;
	}
	
	/**
	 * 日期  建议的日期 可以为空值
	 */
	public void setQuestionDate(java.util.Date questionDate) {
		this.questionDate = questionDate;
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
