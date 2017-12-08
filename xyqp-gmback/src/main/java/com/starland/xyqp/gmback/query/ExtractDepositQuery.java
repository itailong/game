package com.starland.xyqp.gmback.query;

import java.io.Serializable;
import java.util.Date;


public class ExtractDepositQuery implements Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 代理商编号
	 */
	private Integer agentId;
	
	/**
	 * 提现金额，单位元
	 */
	private java.math.BigDecimal money;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 收款人姓名
	 */
	private String receiverName;
	
	/**
	 * 开户行
	 */
	private String bankName;
	
	/**
	 * 银行卡账号
	 */
	private String bankAccount;
	
	/**
	 * 状态，1提现中，2提现成功，3提现失败
	 */
	private Integer status;
	
	private Date startTime;
	
	private Date endTime;
	
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
	 * 代理商编号
	 */
	public Integer getAgentId() {
		return agentId;
	}
	
	/**
	 * 代理商编号
	 */
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	
	/**
	 * 提现金额，单位元
	 */
	public java.math.BigDecimal getMoney() {
		return money;
	}
	
	/**
	 * 提现金额，单位元
	 */
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
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
	 * 备注
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 收款人姓名
	 */
	public String getReceiverName() {
		return receiverName;
	}
	
	/**
	 * 收款人姓名
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	/**
	 * 开户行
	 */
	public String getBankName() {
		return bankName;
	}
	
	/**
	 * 开户行
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	/**
	 * 银行卡账号
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	
	/**
	 * 银行卡账号
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	/**
	 * 状态，1提现中，2提现成功，3提现失败
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 状态，1提现中，2提现成功，3提现失败
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}