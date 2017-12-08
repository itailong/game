package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class Room implements Serializable {

	private static final long serialVersionUID = 5440829744395737484L;

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
	 * 牌墙的牌
	 */
	private Deque<Integer> cardList = new LinkedList<Integer>();
	
	/**
	 * 最后一个出牌的位置，开始为0
	 */
	private int lastPosition;
	
	/**
	 * 最后出的那张牌
	 */
	private Integer lastCard;
	
	/**
	 * 庄家的座位号
	 */
	private int bankerPosition = 1;
	
	/**
	 * 当前是第几盘
	 */
	private int roundCount;
	
	/**
	 * 游戏是否进行中
	 */
	private boolean playing;
	
	/**
	 * 是否正在结算
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
	 * 扎鸟的牌
	 */
	private Integer zhaniaoCard;
	
	/**
	 * 扎鸟的位置
	 */
	private int zhaniaoPosition;
	
	/**
	 * 是否为天胡
	 */
	private boolean skyWin = true;
	
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
	 * 获取牌墙队列
	 * @return
	 */
	public Deque<Integer> getCardList() {
		return cardList;
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
	 * 最后出的一张牌
	 * @return
	 */
	public Integer getLastCard() {
		return lastCard;
	}

	/**
	 * 最后出的一张牌
	 * @return
	 */
	public void setLastCard(Integer lastCard) {
		this.lastCard = lastCard;
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

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isSettling() {
		return settling;
	}

	public void setSettling(boolean settling) {
		this.settling = settling;
	}

	public ScheduledFuture<?> getDissolveScheduled() {
		return dissolveScheduled;
	}

	public void setDissolveScheduled(ScheduledFuture<?> dissolveScheduled) {
		this.dissolveScheduled = dissolveScheduled;
	}

	public Integer getApplyDissolveId() {
		return applyDissolveId;
	}

	public void setApplyDissolveId(Integer applyDissolveId) {
		this.applyDissolveId = applyDissolveId;
	}

	public Integer getZhaniaoCard() {
		return zhaniaoCard;
	}

	public void setZhaniaoCard(Integer zhaniaoCard) {
		this.zhaniaoCard = zhaniaoCard;
	}

	public int getZhaniaoPosition() {
		return zhaniaoPosition;
	}

	public void setZhaniaoPosition(int zhaniaoPosition) {
		this.zhaniaoPosition = zhaniaoPosition;
	}

	public boolean isSkyWin() {
		return skyWin;
	}

	public void setSkyWin(boolean skyWin) {
		this.skyWin = skyWin;
	}

	/**
	 * 重置房间数据
	 */
	public void reset() {
		cardList.clear();
		lastCard = null;
		lastPosition = 0;
		zhaniaoCard = null;
		zhaniaoPosition = 0;
		skyWin = true;
		for (Seat seat : seatList) {
			seat.reset();
		}
	}
}
