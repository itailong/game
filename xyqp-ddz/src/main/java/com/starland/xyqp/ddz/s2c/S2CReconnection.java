package com.starland.xyqp.ddz.s2c;

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
	 * 地主的位置
	 */
	private int landlordPosition;
	
	private int roundCount;
	
	private List<UserInfo> userInfos;
	
	private PlayWay playWay;
	
	private List<Integer> floorCards;
	
	/**
	 * 炸弹数量
	 */
	private int bombCount;
	
	/**
	 * 是否正在结算中
	 */
	private boolean settling;
	
	/**
	 * 当前哪个位置在叫地主，0代表没有在叫地主
	 */
	private int callPosition;
	
	/**
	 * 当前出牌人的位置
	 */
	private int outCardPosition;
	
	/**
	 * 倍数
	 */
	private int multiple;
	
	/**
	 * 叫地主的分数
	 */
	private int callScore;

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

	public int getBankerPosition() {
		return bankerPosition;
	}

	public int getCallPosition() {
		return callPosition;
	}

	public void setCallPosition(int callPosition) {
		this.callPosition = callPosition;
	}

	public int getOutCardPosition() {
		return outCardPosition;
	}

	public void setOutCardPosition(int outCardPosition) {
		this.outCardPosition = outCardPosition;
	}

	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}

	public int getLandlordPosition() {
		return landlordPosition;
	}

	public void setLandlordPosition(int landlordPosition) {
		this.landlordPosition = landlordPosition;
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

	public List<Integer> getFloorCards() {
		return floorCards;
	}

	public void setFloorCards(List<Integer> floorCards) {
		this.floorCards = floorCards;
	}

	public int getBombCount() {
		return bombCount;
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public boolean isSettling() {
		return settling;
	}

	public void setSettling(boolean settling) {
		this.settling = settling;
	}
	
	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getCallScore() {
		return callScore;
	}

	public void setCallScore(int callScore) {
		this.callScore = callScore;
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
		 * 是否已经准备
		 */
		private boolean ready;
		
		/**
		 * 当前分数
		 */
		private int currentScore;

		private String ip;
		
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

		public boolean isReady() {
			return ready;
		}

		public void setReady(boolean ready) {
			this.ready = ready;
		}

		public int getCurrentScore() {
			return currentScore;
		}

		public void setCurrentScore(int currentScore) {
			this.currentScore = currentScore;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}
	
	public static class PlayWay {
		/**
		 * 局数，6,12,20
		 */
		private int roundCount;
		
		/**
		 * 底牌数量，3,4
		 */
		private int floorCount;
		
		/**
		 * 最大炸弹数
		 */
		private int maxBomb;

		public int getRoundCount() {
			return roundCount;
		}

		public void setRoundCount(int roundCount) {
			this.roundCount = roundCount;
		}

		public int getFloorCount() {
			return floorCount;
		}

		public void setFloorCount(int floorCount) {
			this.floorCount = floorCount;
		}

		public int getMaxBomb() {
			return maxBomb;
		}

		public void setMaxBomb(int maxBomb) {
			this.maxBomb = maxBomb;
		}
	}
}
