package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataMaps implements Serializable {

	private static final long serialVersionUID = -2274412546696112271L;

	/**
	 * 所有房间的集合
	 */
	private Map<String, Room> roomMap = new ConcurrentHashMap<String, Room>();
	
	/**
	 * 用户所在座位的集合
	 */
	private Map<Integer, Seat> seatMap = new ConcurrentHashMap<Integer, Seat>();

	public Map<String, Room> getRoomMap() {
		return roomMap;
	}

	public Map<Integer, Seat> getSeatMap() {
		return seatMap;
	}
	
}
