package com.starland.xyqp.pdk.model;

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
	 * 出过的牌
	 */
	private List<List<Integer>> outList = new LinkedList<>();
	
	/**
	 * 是否在线
	 */
	private boolean online;
	
	/**
	 * 是否为空座位
	 */
	private boolean empty = true;
	
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
	 * 小结算信息
	 */
	private SmallSettleInfo smallSettleInfo = new SmallSettleInfo();
	
	/**
	 * 大结算信息
	 */
	private BigSettleInfo bigSettleInfo = new BigSettleInfo();

	/**
	 * 炸弹数
	 */
	private int bombNums;
	
	/**
	 * 是否同意解散房间，0未投票，1同意，2不同意
	 */
	transient private int agreeDissolve;
	
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
	 * 是否为空座位
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * 是否为空座位
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
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
	 * 出过的牌
	 */
	public List<List<Integer>> getOutList() {
		return outList;
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

	/**
	 * 小结算信息
	 */
	public SmallSettleInfo getSmallSettleInfo() {
		return smallSettleInfo;
	}

	/**
	 * 大结算信息
	 */
	public BigSettleInfo getBigSettleInfo() {
		return bigSettleInfo;
	}

	/**
	 * 是否同意解散房间，0未投票，1同意，2不同意
	 */
	public int getAgreeDissolve() {
		return agreeDissolve;
	}

	/**
	 * 是否同意解散房间，0未投票，1同意，2不同意
	 */
	public void setAgreeDissolve(int agreeDissolve) {
		this.agreeDissolve = agreeDissolve;
	}

	public int getBombNums() {
		return bombNums;
	}

	public void setBombNums(int bombNums) {
		this.bombNums = bombNums;
	}

	/**
	 * 重置座位信息
	 */
	public void reset() {
		cardList.clear();
		outList.clear();
		smallSettleInfo = new SmallSettleInfo();
		ready = false;
		bombNums = 1;
		
	}
}
