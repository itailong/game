package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

public class S2CReconnection {

	/**
	 * 状态码
	 */
	private int code;
	
	/**
	 * 状态信息
	 */
	private String msg;
	
	/**
	 * 房间总注数
	 */
	private int totalChips;
	
	/**
	 * 房间单注
	 */
	private int totalAnte;
	
	/**
	 * 房间其他信息
	 */
	private List<UserInfos> userInfos;
	
	/**
	 * 当前下注人的位置
	 */
	private int outAntePosition;
	
	/**
	 * 赢家的位置
	 */
	private int winPosition;
	
	/**
	 * 是否正在游戏中
	 */
	private boolean gameing;
	
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

	public int getTotalChips() {
		return totalChips;
	}

	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	public int getTotalAnte() {
		return totalAnte;
	}

	public void setTotalAnte(int totalAnte) {
		this.totalAnte = totalAnte;
	}

	public List<UserInfos> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfos> userInfos) {
		this.userInfos = userInfos;
	}
	
	public int getOutAntePosition() {
		return outAntePosition;
	}

	public void setOutAntePosition(int outAntePosition) {
		this.outAntePosition = outAntePosition;
	}
	
	public int getWinPosition() {
		return winPosition;
	}

	public void setWinPosition(int winPosition) {
		this.winPosition = winPosition;
	}
	
	public boolean isGameing() {
		return gameing;
	}

	public void setGameing(boolean gameing) {
		this.gameing = gameing;
	}

	public static class UserInfos{
		
		/**
		 * 用户编号
		 */
		private int userId;
		
		/**
		 * 姓名
		 */
		private String name;
		
		/**
		 * 性别 
		 */
		private int sex;
		
		/**
		 * 头像
		 */
		private String headImg;
		
		/**
		 * 座位位置
		 */
		private int seatPosition;
		
		/**
		 * 是否在线
		 */
		private boolean online;
		
		/**
		 * 是否已经准备
		 */
		private boolean isReady;
		
		/**
		 * 当前金币
		 */
		private int gold;
		
		/**
		 * 下注总数
		 */
		private int ante;
		
		/**
		 * 是否已经弃牌
		 */
		private boolean giveUpCard;

		/**
		 * 是否已经看牌
		 */
		private boolean seeCard;
		
		/**
		 * ip
		 */
		private String ip;
		/**
		 * 玩家手上的牌
		 */
		private List<Integer> cardList;
		
		/**
		 *作为定时器 
		 */
		private int seatTime;
		
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

		public boolean isReady() {
			return isReady;
		}

		public void setReady(boolean isReady) {
			this.isReady = isReady;
		}

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
		}

		public int getAnte() {
			return ante;
		}

		public void setAnte(int ante) {
			this.ante = ante;
		}

		public boolean isGiveUpCard() {
			return giveUpCard;
		}

		public void setGiveUpCard(boolean giveUpCard) {
			this.giveUpCard = giveUpCard;
		}

		public boolean isSeeCard() {
			return seeCard;
		}

		public void setSeeCard(boolean seeCard) {
			this.seeCard = seeCard;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public List<Integer> getCardList() {
			return cardList;
		}

		public void setCardList(List<Integer> cardList) {
			this.cardList = cardList;
		}

		public int getSeatTime() {
			return seatTime;
		}

		public void setSeatTime(int seatTime) {
			this.seatTime = seatTime;
		}
	}
}
