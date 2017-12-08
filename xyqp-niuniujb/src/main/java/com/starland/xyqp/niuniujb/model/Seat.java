package com.starland.xyqp.niuniujb.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.starland.tools.network.RouteSession;
import com.starland.xyqp.db.domain.User;

public class Seat implements Serializable {

	private static final long serialVersionUID = 5396330254445934731L;

	/**
	 * 位置，1,2,3
	 */
	private int position;
	
	/**
	 * 所在的房间
	 */
	private Room room;
	
	/**
	 * 手中的牌的列表
	 */
	private List<Integer> cardList = new LinkedList<>();
	
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
	 * 是否抢庄 0未设置  1抢庄  2不抢庄
	 */
	private int grabBanker;
	
	/**
	 * 下的筹码数
	 */
	private int caseAnte;
	
	/**
	 * 用户金币
	 */
	private int gold;

	/**
	 * 牌的类型对象
	 */
	private CardShape cardShape;
	
	/**
	 * 是否开牌 
	 */
	private boolean openCard;
	
	/**
	 * 结算信息
	 */ 
	private SettleInfo settleInfo = new SettleInfo();
	
	public Seat(int position, Room room) {
		this.position = position;
		this.room = room;
	}
	
	/**
	 * 所在的房间
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * 是否在线
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * 是否在线
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * 是否已经准备
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * 是否已经准备
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * 位置，1,2,3
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * 手中的牌的列表
	 */
	public List<Integer> getCardList() {
		return cardList;
	}

	/**
	 * 消息回话
	 */
	public RouteSession getRouteSession() {
		return routeSession;
	}

	/**
	 * 消息会话
	 */
	public void setRouteSession(RouteSession routeSession) {
		this.routeSession = routeSession;
	}

	/**
	 * 用户对象
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 用户对象
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getGrabBanker() {
		return grabBanker;
	}

	public void setGrabBanker(int grabBanker) {
		this.grabBanker = grabBanker;
	}

	public int getCaseAnte() {
		return caseAnte;
	}

	public void setCaseAnte(int caseAnte) {
		this.caseAnte = caseAnte;
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

	public boolean isOpenCard() {
		return openCard;
	}

	public void setOpenCard(boolean openCard) {
		this.openCard = openCard;
	}

	public SettleInfo getSettleInfo() {
		return settleInfo;
	}

	public void setSettleInfo(SettleInfo settleInfo) {
		this.settleInfo = settleInfo;
	}

	/**
	 * 重置座位信息
	 */
	public void reset() {
		cardList.clear();
		grabBanker = 0;
		caseAnte = 0;
		cardShape = null;
		openCard = false;
		settleInfo = new SettleInfo();
	}
}
