package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Notice implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 公告
	 */
	private String content;
	
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
	 * 公告
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 公告
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
}
