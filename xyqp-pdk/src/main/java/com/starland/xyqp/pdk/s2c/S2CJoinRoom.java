package com.starland.xyqp.pdk.s2c;

import java.util.List;

public class S2CJoinRoom {

	private int code;
	
	private String msg;
	
	private String roomId;
	
	private int seatPosition;
	
	/**
	 * 牌数，15,16
	 */
	private int cardCount;
	
	/**
	 * 人数，2,3
	 */
	private int personCount;
	
	/**
	 * 局数，10,20
	 */
	private int roundCount;
	
	/**
	 * 是否扎鸟
	 */
	private boolean zhaniao;
	
	private List<UserInfo> userInfos;
	
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
	
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	public int getSeatPosition() {
		return seatPosition;
	}

	public void setSeatPosition(int seatPosition) {
		this.seatPosition = seatPosition;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	public int getPersonCount() {
		return personCount;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public boolean isZhaniao() {
		return zhaniao;
	}

	public void setZhaniao(boolean zhaniao) {
		this.zhaniao = zhaniao;
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

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}
}
