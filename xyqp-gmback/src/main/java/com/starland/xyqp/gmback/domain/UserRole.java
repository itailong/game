package com.starland.xyqp.gmback.domain;

import java.io.Serializable;

public class UserRole implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 用户编号
	 */
	private Integer userId;
	
	/**
	 * 角色编号
	 */
	private Integer roleId;
	
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
	 * 用户编号
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户编号
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 角色编号
	 */
	public Integer getRoleId() {
		return roleId;
	}
	
	/**
	 * 角色编号
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}
