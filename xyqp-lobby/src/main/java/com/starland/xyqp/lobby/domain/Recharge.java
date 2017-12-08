package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Recharge implements Serializable {

	/**
	 * 交易记录表  id 主键  
	 */
	private Integer id;
	
	/**
	 * 用户的id  与用户表的id相对应
	 */
	private Integer userId;
	
	/**
	 * 支付金额  
	 */
	private Double amountPaid;
	
	/**
	 * 订单号  
	 */
	private String orderNumber;
	
	/**
	 *  代理id
	 */
	private String proxyId;
	
	/**
	 * 状态  成功 失败 进行中
	 */
	private Integer state;
	
	/**
	 * 时间   交易时间
	 */
	private java.util.Date orderTime;
	
	/**
	 * 交易记录表  id 主键  
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 交易记录表  id 主键  
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 用户的id  与用户表的id相对应
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户的id  与用户表的id相对应
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 支付金额  
	 */
	public Double getAmountPaid() {
		return amountPaid;
	}
	
	/**
	 * 支付金额  
	 */
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	/**
	 * 订单号  
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	
	/**
	 * 订单号  
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	/**
	 *  代理id
	 */
	public String getProxyId() {
		return proxyId;
	}
	
	/**
	 *  代理id
	 */
	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}
	
	/**
	 * 状态  成功 失败 进行中
	 */
	public Integer getState() {
		return state;
	}
	
	/**
	 * 状态  成功 失败 进行中
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	
	/**
	 * 时间   交易时间
	 */
	public java.util.Date getOrderTime() {
		return orderTime;
	}
	
	/**
	 * 时间   交易时间
	 */
	public void setOrderTime(java.util.Date orderTime) {
		this.orderTime = orderTime;
	}
	
}
