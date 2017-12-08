package com.starland.xyqp.gmback.domain;

import java.io.Serializable;

public class RoleModule implements Serializable {

	/**
	 * 
	 */
	private Integer id;
	
	/**
	 * 角色编号
	 */
	private Integer roleId;
	
	/**
	 * 功能模块编号
	 */
	private Integer moduleId;
	
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
	
	/**
	 * 功能模块编号
	 */
	public Integer getModuleId() {
		return moduleId;
	}
	
	/**
	 * 功能模块编号
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	
}
