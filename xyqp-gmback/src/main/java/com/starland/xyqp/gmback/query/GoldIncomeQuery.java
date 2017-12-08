package com.starland.xyqp.gmback.query;

import java.io.Serializable;
import java.util.Date;


public class GoldIncomeQuery implements Serializable {

	/**
	 * 
	 */
	private Integer id;
	
	/**
	 * 返利人的编号
	 */
	private Integer userId;
	
	/**
	 * 消耗者编号
	 */
	private Integer consumerId;
	
	/**
	 * 消耗着姓名
	 */
	private String consumerName;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 收益金额，单位元
	 */
	private java.math.BigDecimal income;
	
	/**
	 * 消耗的金币数
	 */
	private Integer consumeGole;
	
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
	 * 消耗者编号
	 */
	public Integer getConsumerId() {
		return consumerId;
	}
	
	/**
	 * 消耗者编号
	 */
	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}
	
	/**
	 * 消耗着姓名
	 */
	public String getConsumerName() {
		return consumerName;
	}
	
	/**
	 * 消耗着姓名
	 */
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
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
	 * 消耗的金币数
	 */
	public Integer getConsumeGole() {
		return consumeGole;
	}
	
	/**
	 * 消耗的金币数
	 */
	public void setConsumeGole(Integer consumeGole) {
		this.consumeGole = consumeGole;
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