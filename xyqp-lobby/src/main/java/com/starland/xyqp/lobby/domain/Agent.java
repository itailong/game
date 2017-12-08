package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Agent implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 上级代理编号
	 */
	private Integer upperId;
	
	/**
	 * 上上级代理商编号
	 */
	private Integer upperTwoId;
	
	/**
	 * 账户余额
	 */
	private java.math.BigDecimal money;
	
	/**
	 * 总的收益
	 */
	private java.math.BigDecimal totalIncome;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 手机号码
	 */
	private String telephone;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 联系地址
	 */
	private String address;
	
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
	 * 上级代理编号
	 */
	public Integer getUpperId() {
		return upperId;
	}
	
	/**
	 * 上级代理编号
	 */
	public void setUpperId(Integer upperId) {
		this.upperId = upperId;
	}
	
	/**
	 * 上上级代理商编号
	 */
	public Integer getUpperTwoId() {
		return upperTwoId;
	}
	
	/**
	 * 上上级代理商编号
	 */
	public void setUpperTwoId(Integer upperTwoId) {
		this.upperTwoId = upperTwoId;
	}
	
	/**
	 * 账户余额
	 */
	public java.math.BigDecimal getMoney() {
		return money;
	}
	
	/**
	 * 账户余额
	 */
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	/**
	 * 总的收益
	 */
	public java.math.BigDecimal getTotalIncome() {
		return totalIncome;
	}
	
	/**
	 * 总的收益
	 */
	public void setTotalIncome(java.math.BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	
	/**
	 * 创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 手机号码
	 */
	public String getTelephone() {
		return telephone;
	}
	
	/**
	 * 手机号码
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	/**
	 * 真实姓名
	 */
	public String getRealName() {
		return realName;
	}
	
	/**
	 * 真实姓名
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	/**
	 * 联系地址
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 联系地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
}
