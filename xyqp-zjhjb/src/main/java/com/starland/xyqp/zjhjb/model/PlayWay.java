package com.starland.xyqp.zjhjb.model;

public class PlayWay {

	/**
	 * 类型
	 */
	private String type;
	/**
	 * 底注
	 */
	private int antes;
	
	/**
	 * 最低金币
	 */
	private int lowGold;
	
	public int getAntes() {
		return antes;
	}

	public void setAntes(int antes) {
		this.antes = antes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getLowGold() {
		return lowGold;
	}

	public void setLowGold(int lowGold) {
		this.lowGold = lowGold;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + antes;
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
		if (antes != other.antes)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
