package com.starland.xyqp.zjhjb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.starland.tools.network.RouteSession;
import com.starland.xyqp.db.domain.User;

public class Seat {

	/**
	 * 位置
	 */
	private int position;
	
	/**
	 * 所在房间
	 */
	private Room room;
	
	/**
	 * 手中牌的列表
	 */
	private List<Integer> cardList = new ArrayList<>();
	
	/**
	 * 是否在线
	 */
	private boolean online;
	
	/**
	 * 消息会话
	 */
	private transient RouteSession routeSession;
	
	/**
	 * 用户对象
	 */
	private User user;
	
	/**
	 * 是否已经准备
	 */
	private boolean ready;
	
	/**
	 * 定时器
	 */
	private ScheduledFuture<?> schedule;
	
	/**
	 * 是否看牌
	 */
	private boolean seeCard;

	/**
	 * 金币
	 */
	private int gold;
	
	/**
	 * 下注总数
	 */
	private int totalAnte;
	
	/**
	 * 牌型对象
	 */
	private CardShape cardShape;
	
	/**
	 * 是否弃牌
	 */
	private boolean giveUp;
	
	/**
	 * 比牌的位置
	 */
	private List<Integer> positionList = new ArrayList<>();
	
	public Seat(int position, Room room){
		this.position = position;
		this.room = room;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public RouteSession getRouteSession() {
		return routeSession;
	}

	public void setRouteSession(RouteSession routeSession) {
		this.routeSession = routeSession;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public ScheduledFuture<?> getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduledFuture<?> schedule) {
		this.schedule = schedule;
	}
	
	public boolean isSeeCard() {
		return seeCard;
	}

	public void setSeeCard(boolean seeCard) {
		this.seeCard = seeCard;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public CardShape getCardShape() {
		return cardShape;
	}

	public void setCardShape(CardShape cardShape) {
		this.cardShape = cardShape;
	}

	public boolean isGiveUp() {
		return giveUp;
	}

	public void setGiveUp(boolean giveUp) {
		this.giveUp = giveUp;
	}

	public int getTotalAnte() {
		return totalAnte;
	}

	public void setTotalAnte(int totalAnte) {
		this.totalAnte = totalAnte;
	}

	public List<Integer> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Integer> positionList) {
		this.positionList = positionList;
	}

	/**
	 * 重置座位信息
	 */
	public void reset(){
		cardList.clear();
		seeCard = false;
		giveUp = false;
		totalAnte = 0;
		positionList.clear();
		schedule = null;
	}

}
