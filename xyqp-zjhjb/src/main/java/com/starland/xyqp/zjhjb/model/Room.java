package com.starland.xyqp.zjhjb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class Room {

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
	 * 当前下注人的位置
	 */
	private int position;
	
	/**
	 * 是否正在游戏中
	 */
	private boolean gameing;
	
	/**
	 * 定时器
	 */
	private ScheduledFuture<?> scheduled;
	
	/**
	 * 当前轮数
	 */
	private int roundNums;
	
	/**
	 * 房间单注
	 */
	private int singleAnte;
	
	/**
	 * 桌上总筹码数
	 */
	private int totalChips;
	
	/**
	 * 赢家的位置
	 */
	private int winPosition;
	
	public Room(String id, int size){
		this.id = id;
		seatList = new ArrayList<>(size);
		for(int i = 0 ; i < size ; i++){
			Seat seat = new Seat(i + 1, this);
			seatList.add(seat);
		}
		seatList = Collections.unmodifiableList(seatList);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PlayWay getPlayWay() {
		return playWay;
	}

	public void setPlayWay(PlayWay playWay) {
		this.playWay = playWay;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	public int getBankerPosition() {
		return bankerPosition;
	}

	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
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
	
	public int getRoundNums() {
		return roundNums;
	}

	public void setRoundNums(int roundNums) {
		this.roundNums = roundNums;
	}
	
	public int getSingleAnte() {
		return singleAnte;
	}

	public void setSingleAnte(int singleAnte) {
		this.singleAnte = singleAnte;
	}

	public int getTotalChips() {
		return totalChips;
	}

	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	public int getWinPosition() {
		return winPosition;
	}

	public void setWinPosition(int winPosition) {
		this.winPosition = winPosition;
	}

	/**
	 * 重置房间数据
	 */
	public void reset(){
		gameing = true;
		roundNums = 1;
		totalChips = 0;
		position = 0;
		singleAnte = playWay.getAntes();
		for(Seat seat : seatList){
			seat.reset();
		}
	}
}
