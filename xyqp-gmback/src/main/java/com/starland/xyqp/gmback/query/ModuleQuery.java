package com.starland.xyqp.gmback.query;

import java.io.Serializable;


public class ModuleQuery implements Serializable {

	/**
	 * 
	 */
	private Integer id;
	
	/**
	 * 模块名称
	 */
	private String name;
	
	/**
	 * 功能模块对应的路径
	 */
	private String url;
	
	/**
	 * 模块组编号
	 */
	private Integer groupId;
	
	/**
	 * 
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 模块名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 模块名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 功能模块对应的路径
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 功能模块对应的路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 模块组编号
	 */
	public Integer getGroupId() {
		return groupId;
	}
	
	/**
	 * 模块组编号
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
}