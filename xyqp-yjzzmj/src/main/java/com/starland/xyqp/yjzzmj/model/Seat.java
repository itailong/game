package com.starland.xyqp.yjzzmj.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.starland.tools.network.RouteSession;
import com.starland.xyqp.db.domain.User;

public class Seat implements Serializable {

	private static final long serialVersionUID = -4172295973683369359L;

	/**
	 * 位置，1,2,3,4
	 */
	private int position;
	
	/**
	 * 所在的房间
	 */
	private Room room;
	
	/**
	 * 手中的牌的列表, 只能是1,4,7,10,13张，不包括接的牌
	 */
	private List<Integer> cardList = new LinkedList<Integer>();
	
	/**
	 * 接的牌，没有接牌时为空
	 */
	private Integer card;
	
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
	 * 出牌的列表
	 */
	private List<Integer> outList = new LinkedList<Integer>();
	
	/**
	 * 碰牌的列表
	 */
	private List<Integer> bumpList = new LinkedList<Integer>();
	
	/**
	 * 明杠的列表
	 */
	private List<Integer> showBridgeList = new LinkedList<Integer>();
	
	/**
	 * 暗杠的列表
	 */
	private List<Integer> hideBridgeList = new LinkedList<Integer>();
	
	/**
	 * 过路杠（碰后杠）的列表
	 */
	private List<Integer> passBridgeList = new LinkedList<Integer>();
	
	/**
	 * 对玩家牌分析后的信息
	 */
	private CardInfo cardInfo;
	
	/**
	 * 玩家做出的操作
	 */
	private Operate operate;
	
	/**
	 * 是否同意解散房间，0未投票，1同意，2不同意
	 */
	transient private int agreeDissolve;
	
	/**
	 * 结算信息
	 */
	private SettleInfo settleInfo = new SettleInfo();
	
	/**
	 * 这一局放杠的数量
	 */
	private int sendBridgeNum;
	
	/**
	 * 是否刚杠过，杠上花用
	 */
	private boolean justBridge;
	
	/**
	 * 判断抢杠胡的牌
	 */
	private Integer grabBridgeCard;
	
	/**
	 * 胡过的牌，即可以胡但没胡的牌，要等到下一个圈才能胡这张牌
	 */
	private List<Integer> winPassCards = new LinkedList<>();
	
	/**
	 * 是否为第一次接牌，听牌使用
	 */
	private boolean firstTakeCard = true;
	
	/**
	 * 听牌的列表
	 */
	private List<Integer> hearList = new LinkedList<>();
	
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
	 * 接的牌，没有接牌时为空
	 */
	public Integer getCard() {
		return card;
	}

	/**
	 * 接的牌，没有接牌时为空
	 */
	public void setCard(Integer card) {
		this.card = card;
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
	 * 位置，1,2,3,4
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * 手中的牌的列表, 只能是1,4,7,10,13张，不包括接的牌
	 */
	public List<Integer> getCardList() {
		return cardList;
	}

	/**
	 * 出牌的列表
	 */
	public List<Integer> getOutList() {
		return outList;
	}

	/**
	 * 碰牌的列表
	 */
	public List<Integer> getBumpList() {
		return bumpList;
	}

	/**
	 * 明杠的列表
	 */
	public List<Integer> getShowBridgeList() {
		return showBridgeList;
	}

	/**
	 * 暗杠的列表
	 */
	public List<Integer> getHideBridgeList() {
		return hideBridgeList;
	}

	/**
	 * 过路杠（碰后杠）的列表
	 */
	public List<Integer> getPassBridgeList() {
		return passBridgeList;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 对玩家牌分析后的信息
	 * @return
	 */
	public CardInfo getCardInfo() {
		return cardInfo;
	}

	/**
	 * 对玩家牌分析后的信息
	 * @param cardInfo
	 */
	public void setCardInfo(CardInfo cardInfo) {
		this.cardInfo = cardInfo;
	}

	/**
	 * 玩家做出的操作
	 */
	public Operate getOperate() {
		return operate;
	}

	/**
	 * 玩家做出的操作
	 */
	public void setOperate(Operate operate) {
		this.operate = operate;
	}

	/**
	 * 结算信息
	 * @return
	 */
	public SettleInfo getSettleInfo() {
		return settleInfo;
	}

	/**
	 * 是否同意解散房间，0位投票，1同意，2不同意
	 * @return
	 */
	public int getAgreeDissolve() {
		return agreeDissolve;
	}

	/**
	 * 是否同意解散房间，0未投票，1同意，2不同意
	 * @param agreeDissolve
	 */
	public void setAgreeDissolve(int agreeDissolve) {
		this.agreeDissolve = agreeDissolve;
	}

	public int getSendBridgeNum() {
		return sendBridgeNum;
	}

	public void setSendBridgeNum(int sendBridgeNum) {
		this.sendBridgeNum = sendBridgeNum;
	}

	public boolean isJustBridge() {
		return justBridge;
	}

	public void setJustBridge(boolean justBridge) {
		this.justBridge = justBridge;
	}

	public Integer getGrabBridgeCard() {
		return grabBridgeCard;
	}

	public void setGrabBridgeCard(Integer grabBridgeCard) {
		this.grabBridgeCard = grabBridgeCard;
	}

	public List<Integer> getWinPassCards() {
		return winPassCards;
	}

	public List<Integer> getHearList() {
		return hearList;
	}

	public boolean isFirstTakeCard() {
		return firstTakeCard;
	}

	public void setFirstTakeCard(boolean firstTakeCard) {
		this.firstTakeCard = firstTakeCard;
	}

	/**
	 * 重置座位信息
	 */
	public void reset() {
		cardList.clear();
		card = null;
		outList.clear();
		bumpList.clear();
		showBridgeList.clear();
		hideBridgeList.clear();
		passBridgeList.clear();
		cardInfo = null;
		operate = null;
		sendBridgeNum = 0;
		justBridge = false;
		grabBridgeCard = null;
		winPassCards.clear();
		firstTakeCard = true;
		hearList.clear();
	}

}
