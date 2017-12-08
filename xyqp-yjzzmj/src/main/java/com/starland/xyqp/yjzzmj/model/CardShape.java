package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;

public class CardShape implements Serializable {

	private static final long serialVersionUID = -1443096114926121304L;

	/**
	 * 牌型编号
	 */
	private String id;
	
	/**
	 * 牌型名称
	 */
	private String name;
	
	/**
	 * 番数
	 */
	private int count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
