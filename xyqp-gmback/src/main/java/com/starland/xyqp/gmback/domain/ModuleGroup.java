package com.starland.xyqp.gmback.domain;

import java.io.Serializable;

public class ModuleGroup implements Serializable {

	/**
	 * 
	 */
	private Integer id;
	
	/**
	 * 模块组名称
	 */
	private String name;
	
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
	 * 模块组名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 模块组名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
