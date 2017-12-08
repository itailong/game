package com.starland.xyqp.ddz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
	 * 最后一个出牌的位置，开始为0
	 */
	private int lastPosition;
	
	/**
	 * 最后出的那些牌
	 */
	private List<Integer> lastCards = new LinkedList<>();
	
	/**
	 * 最后出的牌的牌型
	 */
	private CardShape lastCardShape;
	
	/**
	 * 底牌
	 */
	private List<Integer> floorCards = new LinkedList<>();
	
	/**
	 * 庄家的座位号
	 */
	private int bankerPosition = 1;
	
	/**
	 * 当前是第几盘
	 */
	private int roundCount;
	
	/**
	 * 当前哪个位置在叫地主，0代表没有在叫地主
	 */
	private int callPosition;
	
	/**
	 * 叫地主的分数
	 */
	private int callScore;
	
	/**
	 * 地主的位置
	 */
	private int landlordPosition;
	
	/**
	 * 当前出牌人的位置
	 */
	private int outCardPosition;
	
	/**
	 * 是否正在结算中
	 */
	private boolean settling;
	
	/**
	 * 炸弹数量
	 */
	private int bombCount;
	
	/**
	 * 解散房间的定时器
	 */
	transient private ScheduledFuture<?> dissolveScheduled;
	
	/**
	 * 申请解散人的编号
	 */
	private Integer applyDissolveId;
	
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
	 * 最后一个出牌的位置
	 * @return
	 */
	public int getLastPosition() {
		return lastPosition;
	}

	/**
	 * 最后一个出牌的位置
	 * @return
	 */
	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}

	/**
	 * 最后出的那写牌
	 */
	public List<Integer> getLastCards() {
		return lastCards;
	}

	/**
	 * 最后出的牌的牌型
	 */
	public CardShape getLastCardShape() {
		return lastCardShape;
	}

	/**
	 * 最后出的牌的牌型
	 */
	public void setLastCardShape(CardShape lastCardShape) {
		this.lastCardShape = lastCardShape;
	}

	/**
	 * 底牌
	 */
	public List<Integer> getFloorCards() {
		return floorCards;
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
	 * 当前是第几盘
	 */
	public int getRoundCount() {
		return roundCount;
	}

	/**
	 * 当前是第几盘
	 */
	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	/**
	 * 当前哪个位置在叫地主，0代表没有在叫地主
	 */
	public int getCallPosition() {
		return callPosition;
	}

	/**
	 * 当前哪个位置在叫地主，0代表没有在叫地主
	 */
	public void setCallPosition(int callPosition) {
		this.callPosition = callPosition;
	}

	/**
	 * 叫地主的分数
	 */
	public int getCallScore() {
		return callScore;
	}

	/**
	 * 叫地主的分数
	 */
	public void setCallScore(int callScore) {
		this.callScore = callScore;
	}

	/**
	 * 地主的位置
	 */
	public int getLandlordPosition() {
		return landlordPosition;
	}

	/**
	 * 地主的位置
	 */
	public void setLandlordPosition(int landlordPosition) {
		this.landlordPosition = landlordPosition;
	}
	
	/**
	 * 当前出牌人的位置
	 */
	public int getOutCardPosition() {
		return outCardPosition;
	}

	/**
	 * 当前出牌人的位置
	 */
	public void setOutCardPosition(int outCardPosition) {
		this.outCardPosition = outCardPosition;
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

	/**
	 * 炸弹数量
	 */
	public int getBombCount() {
		return bombCount;
	}

	/**
	 * 炸弹数量
	 */
	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	/**
	 * 解散房间的定时器
	 */
	public ScheduledFuture<?> getDissolveScheduled() {
		return dissolveScheduled;
	}

	/**
	 * 解散房间的定时器
	 */
	public void setDissolveScheduled(ScheduledFuture<?> dissolveScheduled) {
		this.dissolveScheduled = dissolveScheduled;
	}

	/**
	 * 申请解散人的编号
	 */
	public Integer getApplyDissolveId() {
		return applyDissolveId;
	}

	/**
	 * 申请解散人的编号
	 */
	public void setApplyDissolveId(Integer applyDissolveId) {
		this.applyDissolveId = applyDissolveId;
	}

	/**
	 * 重置房间数据
	 */
	public void reset() {
		lastCards.clear();
		lastCardShape = null;
		floorCards.clear();
		lastPosition = 0;
		callPosition = bankerPosition;
		landlordPosition = 0;
		outCardPosition = 0;
		settling = false;
		bombCount = 0;
		callScore = 0;
		for (Seat seat : seatList) {
			seat.reset();
		}
	}
}
