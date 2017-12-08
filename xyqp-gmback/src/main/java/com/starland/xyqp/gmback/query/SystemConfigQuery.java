package com.starland.xyqp.gmback.query;

import java.io.Serializable;


public class SystemConfigQuery implements Serializable {

	/**
	 * 编号
	 */
	private String id;
	
	/**
	 * 配置对应的值
	 */
	private String value;
	
	/**
	 * 配置名称
	 */
	private String name;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 编号
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 编号
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 配置对应的值
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * 配置对应的值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 配置名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 配置名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 描述
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}