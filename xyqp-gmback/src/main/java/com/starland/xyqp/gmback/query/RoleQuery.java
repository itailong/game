package com.starland.xyqp.gmback.query;

import java.io.Serializable;


public class RoleQuery implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 角色名称
	 */
	private String name;
	
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
	 * 角色名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 角色名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}