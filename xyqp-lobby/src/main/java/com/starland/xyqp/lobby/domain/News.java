package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class News implements Serializable {

	/**
	 * 主键，自动递增
	 */
	private Integer id;
	
	/**
	 * 消息题目
	 */
	private String newsTitle;
	
	/**
	 * 内容的简介
	 */
	private String newsIntroduction;
	
	/**
	 * 消息内容
	 */
	private String newsContent;
	
	/**
	 * 消息发送的日期
	 */
	private java.util.Date newsDate;
	
	/**
	 * 主键，自动递增
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 主键，自动递增
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 消息题目
	 */
	public String getNewsTitle() {
		return newsTitle;
	}
	
	/**
	 * 消息题目
	 */
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	
	/**
	 * 内容的简介
	 */
	public String getNewsIntroduction() {
		return newsIntroduction;
	}
	
	/**
	 * 内容的简介
	 */
	public void setNewsIntroduction(String newsIntroduction) {
		this.newsIntroduction = newsIntroduction;
	}
	
	/**
	 * 消息内容
	 */
	public String getNewsContent() {
		return newsContent;
	}
	
	/**
	 * 消息内容
	 */
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	
	/**
	 * 消息发送的日期
	 */
	public java.util.Date getNewsDate() {
		return newsDate;
	}
	
	/**
	 * 消息发送的日期
	 */
	public void setNewsDate(java.util.Date newsDate) {
		this.newsDate = newsDate;
	}
	
}
