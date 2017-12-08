package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

public class S2CJoinRoom {

	private int code;
	
	
	private String msg;
	
	/**
	 * 房间号
	 */
	private String roomId;
	
	/**
	 * 用户的座位号
	 */
	private int seatPosition;
	
	/**
	 * 当前下注人的位置
	 */
	private int antePosition;
	
	/**
	 * 当前房间单注
	 */
	private int singleAnte;
	
	/**
	 * 当前房间总注数
	 */
	private int totalChips;
	
	/**
	 * 庄家的位置
	 */
	private int bankerPosition;
	
	/**
	 * 是否正在游戏中
	 */
	private boolean gameing;
	
	/**
	 * 赢家的位置
	 */
	private int winPosition;
	
	/**
	 * 其他玩家的相关信息
	 */
	private List<UserInfo> userInfos;
	
	/**
	 * 房间定时器
	 */
	private int roomTime;
	
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
	
	public int getAntePosition() {
		return antePosition;
	}

	public void setAntePosition(int antePosition) {
		this.antePosition = antePosition;
	}

	public int getSingleAnte() {
		return singleAnte;
	}

	public void setSingleAnte(int singleAnte) {
		this.singleAnte = singleAnte;
	}

	public int getTotalChips() {
		return totalChips;
	}

	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	public int getBankerPosition() {
		return bankerPosition;
	}

	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}

	public boolean isGameing() {
		return gameing;
	}

	public void setGameing(boolean gameing) {
		this.gameing = gameing;
	}
	
	public int getWinPosition() {
		return winPosition;
	}

	public void setWinPosition(int winPosition) {
		this.winPosition = winPosition;
	}
	
	public int getRoomTime() {
		return roomTime;
	}

	public void setRoomTime(int roomTime) {
		this.roomTime = roomTime;
	}

	public static class UserInfo{
		
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
		 * 金币
		 */
		private int gold;
		
		/**
		 * 座位位置
		 */
		private int seatPosition;
		
		/**
		 * 是否在线
		 */
		private boolean online;
		
		/**
		 * ip
		 */
		private String ip;
		
		/**
		 * 是否弃牌
		 */
		private boolean giveUpCard;
		
		/**
		 * 是否已经准备
		 */
		private boolean isReady;
		
		/**
		 * 是否已经看牌
		 */
		private boolean seeCard;
		
		/**
		 * 下的总注数
		 */
		private int totalAnte;
		
		/**
		 * 计时器时间
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

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
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

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public boolean isGiveUpCard() {
			return giveUpCard;
		}

		public void setGiveUpCard(boolean giveUpCard) {
			this.giveUpCard = giveUpCard;
		}

		public boolean isReady() {
			return isReady;
		}

		public void setReady(boolean isReady) {
			this.isReady = isReady;
		}

		public boolean isSeeCard() {
			return seeCard;
		}

		public void setSeeCard(boolean seeCard) {
			this.seeCard = seeCard;
		}

		public int getTotalAnte() {
			return totalAnte;
		}

		public void setTotalAnte(int totalAnte) {
			this.totalAnte = totalAnte;
		}

		public int getSeatTime() {
			return seatTime;
		}

		public void setSeatTime(int seatTime) {
			this.seatTime = seatTime;
		}
	}
}
