package com.starland.xyqp.yjzzmj.s2c;

import java.util.List;

public class S2CJoinRoom {

	private int code;
	
	private String msg;
	
	private String roomId;
	
	private int seatPosition;
	
	private List<UserInfo> userInfos;
	
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
	}
}
