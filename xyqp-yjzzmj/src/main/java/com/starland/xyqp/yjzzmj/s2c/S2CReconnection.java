package com.starland.xyqp.yjzzmj.s2c;

import java.util.LinkedList;
import java.util.List;

public class S2CReconnection {

	/**
	 * 状态码
	 */
	private int code;
	
	/**
	 * 错误消息
	 */
	private String msg;
	
	private String roomId;
	
	private int seatPosition;
	
	private int lastPosition;
	
	private int bankerPosition;
	
	/**
	 * 游戏是否进行中
	 */
	private boolean playing;
	
	private int roundCount;
	
	private int surplusCardNum;
	
	private List<UserInfo> userInfos;
	
	private PlayWay playWay;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getSeatPosition() {
		return seatPosition;
	}

	public void setSeatPosition(int seatPosition) {
		this.seatPosition = seatPosition;
	}

	public List<UserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	public int getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public int getBankerPosition() {
		return bankerPosition;
	}

	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public PlayWay getPlayWay() {
		return playWay;
	}

	public void setPlayWay(PlayWay playWay) {
		this.playWay = playWay;
	}

	public int getSurplusCardNum() {
		return surplusCardNum;
	}

	public void setSurplusCardNum(int surplusCardNum) {
		this.surplusCardNum = surplusCardNum;
	}

	public static class UserInfo {
		/**
		 * 用户编号
		 */
		private int userId;
		
		/**
		 * 姓名
		 */
		private String name;
		
		/**
		 * 微信对应的openId
		 */
		private String openId;
		
		/**
		 * 0未设置，1男，2女
		 */
		private int sex;
		
		/**
		 * 头像
		 */
		private String headImg;
		
		/**
		 * 钻石
		 */
		private int diamond;
		
		/**
		 * 座位位置
		 */
		private int seatPosition;
		
		/**
		 * 是否在线
		 */
		private boolean online;
		
		/**
		 * 手上的牌
		 */
		private List<Integer> cardList;
		
		/**
		 * 接的牌，没有接牌时为空
		 */
		private Integer card;
		
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
		 * 是否听牌
		 */
		private boolean hear;
		
		/**
		 * 当前分数
		 */
		private int currentScore;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOpenId() {
			return openId;
		}

		public void setOpenId(String openId) {
			this.openId = openId;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public String getHeadImg() {
			return headImg;
		}

		public void setHeadImg(String headImg) {
			this.headImg = headImg;
		}

		public int getDiamond() {
			return diamond;
		}

		public void setDiamond(int diamond) {
			this.diamond = diamond;
		}

		public int getSeatPosition() {
			return seatPosition;
		}

		public void setSeatPosition(int seatPosition) {
			this.seatPosition = seatPosition;
		}

		public boolean isOnline() {
			return online;
		}

		public void setOnline(boolean online) {
			this.online = online;
		}

		public List<Integer> getCardList() {
			return cardList;
		}

		public void setCardList(List<Integer> cardList) {
			this.cardList = cardList;
		}

		public Integer getCard() {
			return card;
		}

		public void setCard(Integer card) {
			this.card = card;
		}

		public boolean isReady() {
			return ready;
		}

		public void setReady(boolean ready) {
			this.ready = ready;
		}

		public List<Integer> getOutList() {
			return outList;
		}

		public void setOutList(List<Integer> outList) {
			this.outList = outList;
		}

		public List<Integer> getBumpList() {
			return bumpList;
		}

		public void setBumpList(List<Integer> bumpList) {
			this.bumpList = bumpList;
		}

		public List<Integer> getShowBridgeList() {
			return showBridgeList;
		}

		public void setShowBridgeList(List<Integer> showBridgeList) {
			this.showBridgeList = showBridgeList;
		}

		public List<Integer> getHideBridgeList() {
			return hideBridgeList;
		}

		public void setHideBridgeList(List<Integer> hideBridgeList) {
			this.hideBridgeList = hideBridgeList;
		}

		public List<Integer> getPassBridgeList() {
			return passBridgeList;
		}

		public void setPassBridgeList(List<Integer> passBridgeList) {
			this.passBridgeList = passBridgeList;
		}

		public boolean isHear() {
			return hear;
		}

		public void setHear(boolean hear) {
			this.hear = hear;
		}

		public int getCurrentScore() {
			return currentScore;
		}

		public void setCurrentScore(int currentScore) {
			this.currentScore = currentScore;
		}
	}
	
	public static class PlayWay {
		
		/**
		 * 局数，6局，12局
		 */
		private int roundNum;
		
		/**
		 * 是否有最大番数
		 */
		private boolean hasMultipleMax;
		
		/**
		 * 是否有门清
		 */
		private boolean hasMenqing;
		
		/**
		 * 门清将将胡，是否可接炮
		 */
		private boolean mqjjh;
		
		/**
		 * 一字撬，是否有喜
		 */
		private boolean yiziqiao;

		public int getRoundNum() {
			return roundNum;
		}

		public void setRoundNum(int roundNum) {
			this.roundNum = roundNum;
		}

		public boolean isHasMultipleMax() {
			return hasMultipleMax;
		}

		public void setHasMultipleMax(boolean hasMultipleMax) {
			this.hasMultipleMax = hasMultipleMax;
		}

		public boolean isHasMenqing() {
			return hasMenqing;
		}

		public void setHasMenqing(boolean hasMenqing) {
			this.hasMenqing = hasMenqing;
		}

		public boolean isMqjjh() {
			return mqjjh;
		}

		public void setMqjjh(boolean mqjjh) {
			this.mqjjh = mqjjh;
		}

		public boolean isYiziqiao() {
			return yiziqiao;
		}

		public void setYiziqiao(boolean yiziqiao) {
			this.yiziqiao = yiziqiao;
		}
		
	}
}
