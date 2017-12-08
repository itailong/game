package com.starland.xyqp.pdk.model;

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
	 * 庄家的座位号
	 */
	private int bankerPosition = 1;
	
	/**
	 * 当前是第几盘
	 */
	private int roundCount;
	
	/**
	 * 当前出牌人的位置
	 */
	private int outCardPosition;
	
	/**
	 * 是否正在结算中
	 */
	private boolean settling;
	
	/**
	 * 解散房间的定时器
	 */
	transient private ScheduledFuture<?> dissolveScheduled;
	
	/**
	 * 申请解散人的编号
	 */
	private Integer applyDissolveId;
	
	/**
	 * 扎鸟的位置
	 */
	private int zhaniaoPosition;
	
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
	 * 扎鸟的位置
	 */
	public int getZhaniaoPosition() {
		return zhaniaoPosition;
	}

	/**
	 * 扎鸟的位置
	 */
	public void setZhaniaoPosition(int zhaniaoPosition) {
		this.zhaniaoPosition = zhaniaoPosition;
	}

	/**
	 * 重置房间数据
	 */
	public void reset() {
		lastCards.clear();
		lastCardShape = null;
		lastPosition = 0;
		outCardPosition = 0;
		settling = false;
		zhaniaoPosition = 0;
		for (Seat seat : seatList) {
			seat.reset();
		}
	}
}
