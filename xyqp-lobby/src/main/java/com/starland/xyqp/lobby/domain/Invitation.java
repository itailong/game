package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Invitation implements Serializable {

	/**
	 * id  主键
	 */
	private Integer id;
	
	/**
	 * 持有邀请码人的姓名
	 */
	private String name;
	
	/**
	 * 邀请码的内容
	 */
	private Integer invitationCode;
	
	/**
	 * 绑定此邀请码的人数
	 */
	private Integer invitationBind;
	
	/**
	 * 代理手机号码
	 */
	private String invitationPhone;
	
	/**
	 * id  主键
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * id  主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 持有邀请码人的姓名
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 持有邀请码人的姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 邀请码的内容
	 */
	public Integer getInvitationCode() {
		return invitationCode;
	}
	
	/**
	 * 邀请码的内容
	 */
	public void setInvitationCode(Integer invitationCode) {
		this.invitationCode = invitationCode;
	}
	
	/**
	 * 绑定此邀请码的人数
	 */
	public Integer getInvitationBind() {
		return invitationBind;
	}
	
	/**
	 * 绑定此邀请码的人数
	 */
	public void setInvitationBind(Integer invitationBind) {
		this.invitationBind = invitationBind;
	}
	
	/**
	 * 代理手机号码
	 */
	public String getInvitationPhone() {
		return invitationPhone;
	}
	
	/**
	 * 代理手机号码
	 */
	public void setInvitationPhone(String invitationPhone) {
		this.invitationPhone = invitationPhone;
	}
	
}
