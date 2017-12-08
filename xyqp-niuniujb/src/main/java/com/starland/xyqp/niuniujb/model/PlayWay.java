package com.starland.xyqp.niuniujb.model;

import java.io.Serializable;

public class PlayWay implements Serializable {

	private static final long serialVersionUID = 1342201302456648876L;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 底分
	 */
	private int bottomScore;
	
	/**
	 * 准入分
	 */
	private int accessPoint;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBottomScore() {
		return bottomScore;
	}

	public void setBottomScore(int bottomScore) {
		this.bottomScore = bottomScore;
	}
	
	public int getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(int accessPoint) {
		this.accessPoint = accessPoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottomScore;
		result = prime * result + accessPoint;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayWay other = (PlayWay) obj;
		if (bottomScore != other.bottomScore)
			return false;
		if (accessPoint != other.accessPoint)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
