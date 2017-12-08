package com.starland.xyqp.lobby.domain;

import java.io.Serializable;

public class Prize implements Serializable {

	/**
	 * id主键
	 */
	private Integer id;
	
	/**
	 * 奖品的id
	 */
	private Integer prizeId;
	
	/**
	 * 奖品的名称
	 */
	private String prizeName;
	
	/**
	 * 奖品的权重
	 */
	private Integer weight;
	
	/**
	 * id主键
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * id主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 奖品的id
	 */
	public Integer getPrizeId() {
		return prizeId;
	}
	
	/**
	 * 奖品的id
	 */
	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}
	
	/**
	 * 奖品的名称
	 */
	public String getPrizeName() {
		return prizeName;
	}
	
	/**
	 * 奖品的名称
	 */
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	
	/**
	 * 奖品的权重
	 */
	public Integer getWeight() {
		return weight;
	}
	
	/**
	 * 奖品的权重
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
}
