package com.starland.xyqp.gmback.query;

import java.io.Serializable;
import java.util.Date;


public class DiamondIncomeQuery implements Serializable {

	/**
	 * 
	 */
	private Integer id;
	
	/**
	 * 返利人的编号
	 */
	private Integer userId;
	
	/**
	 * 购买者编号
	 */
	private Integer buyerId;
	
	/**
	 * 购买人姓名
	 */
	private String buyerName;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 收益金额，单位元
	 */
	private java.math.BigDecimal income;
	
	/**
	 * 购买金额，单位元
	 */
	private java.math.BigDecimal buyMoney;
	
	/**
	 * 购买钻石数
	 */
	private Integer buyDiamond;
	
	/**
	 * 返利的比率
	 */
	private Float rate;
	
	/**
	 * 向下的等层级
	 */
	private Integer downLevel;
	
	private Date startTime;
	
	private Date endTime;
	
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
	 * 返利人的编号
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 返利人的编号
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 购买者编号
	 */
	public Integer getBuyerId() {
		return buyerId;
	}
	
	/**
	 * 购买者编号
	 */
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	
	/**
	 * 购买人姓名
	 */
	public String getBuyerName() {
		return buyerName;
	}
	
	/**
	 * 购买人姓名
	 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
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
	 * 收益金额，单位元
	 */
	public java.math.BigDecimal getIncome() {
		return income;
	}
	
	/**
	 * 收益金额，单位元
	 */
	public void setIncome(java.math.BigDecimal income) {
		this.income = income;
	}
	
	/**
	 * 购买金额，单位元
	 */
	public java.math.BigDecimal getBuyMoney() {
		return buyMoney;
	}
	
	/**
	 * 购买金额，单位元
	 */
	public void setBuyMoney(java.math.BigDecimal buyMoney) {
		this.buyMoney = buyMoney;
	}
	
	/**
	 * 购买钻石数
	 */
	public Integer getBuyDiamond() {
		return buyDiamond;
	}
	
	/**
	 * 购买钻石数
	 */
	public void setBuyDiamond(Integer buyDiamond) {
		this.buyDiamond = buyDiamond;
	}
	
	/**
	 * 返利的比率
	 */
	public Float getRate() {
		return rate;
	}
	
	/**
	 * 返利的比率
	 */
	public void setRate(Float rate) {
		this.rate = rate;
	}
	
	/**
	 * 向下的等层级
	 */
	public Integer getDownLevel() {
		return downLevel;
	}
	
	/**
	 * 向下的等层级
	 */
	public void setDownLevel(Integer downLevel) {
		this.downLevel = downLevel;
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