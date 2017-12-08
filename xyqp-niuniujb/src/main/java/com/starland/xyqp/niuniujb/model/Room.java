package com.starland.xyqp.niuniujb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class Room implements Serializable {

	private static final long serialVersionUID = -8380417993990028918L;
	
	/**
	 * 房间号
	 */
	private String id;
	
	/**
	 * 玩法
	 */
	private PlayWay playWay;
	
	/**
	 * 座位的列表
	 */
	private List<Seat> seatList;
	
	/**
	 * 庄家的座位号
	 */
	private int bankerPosition;
	
	/**
	 * 是否正在结算中
	 */
	private boolean settling;
	
	/**
	 * 是否正在游戏中
	 */
	private boolean gameing;
	
	/**
	 * 定时器
	 */
	private ScheduledFuture<?> scheduled;
	
	/**
	 * 创建房间
	 * @param id 房间编号
	 * @param size 房间的座位数量
	 */
	public Room(String id, int size) {
		this.id = id;
		seatList = new ArrayList<Seat>(size);
		for (int i = 0; i < size; i++) {
			Seat seat = new Seat(i + 1, this);
			seatList.add(seat);
		}
		// 座位创建后就不能改变
		seatList = Collections.unmodifiableList(seatList);
	}
	

	/**
	 * 获取房间编号
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 玩法
	 */
	public PlayWay getPlayWay() {
		return playWay;
	}

	/**
	 * 玩法
	 */
	public void setPlayWay(PlayWay playWay) {
		this.playWay = playWay;
	}

	/**
	 * 获取座位列表
	 * @return
	 */
	public List<Seat> getSeatList() {
		return seatList;
	}
	
	/**
	 * 庄家的位置
	 * @return
	 */
	public int getBankerPosition() {
		return bankerPosition;
	}

	/**
	 * 庄家的位置
	 * @return
	 */
	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}

	/**
	 * 是否正在结算中
	 */
	public boolean isSettling() {
		return settling;
	}

	/**
	 * 是否正在结算中
	 */
	public void setSettling(boolean settling) {
		this.settling = settling;
	}
	
	public boolean isGameing() {
		return gameing;
	}

	public void setGameing(boolean gameing) {
		this.gameing = gameing;
	}
	
	public ScheduledFuture<?> getScheduled() {
		return scheduled;
	}

	public void setScheduled(ScheduledFuture<?> scheduled) {
		this.scheduled = scheduled;
	}


	/**
	 * 重置房间数据
	 */
	public void reset() {
		bankerPosition = 0;
		gameing = true;
		settling = false;
		scheduled = null;
		for (Seat seat : seatList) {
			seat.reset();
		}
	}
}
