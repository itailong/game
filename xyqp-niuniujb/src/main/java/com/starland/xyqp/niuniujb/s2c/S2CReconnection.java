package com.starland.xyqp.niuniujb.s2c;
	
import java.util.List;
	
public class S2CReconnection {
	
	private int code;
	
	private String msg;
	
	private int bankerPosition;
	/**
	 * 发的牌
	 */
	private List<Integer> cardList;

	private boolean gameing;
	
	private int caseAnte;
	
	private List<UserInfo> userInfos;
	
	private int position;
	
	private List<Integer> openCardList;
	
	private int type;
	
	private int time;
	
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
	
	public int getBankerPosition() {
		return bankerPosition;
	}
	
	public void setBankerPosition(int bankerPosition) {
		this.bankerPosition = bankerPosition;
	}
	
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}
	
	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}
	
	public List<Integer> getCardList() {
		return cardList;
	}
	
	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}
	
	public int getCaseAnte() {
		return caseAnte;
	}
	
	public void setCaseAnte(int caseAnte) {
		this.caseAnte = caseAnte;
	}

	public boolean isGameing() {
		return gameing;
	}

	public void setGameing(boolean gameing) {
		this.gameing = gameing;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Integer> getOpenCardList() {
		return openCardList;
	}

	public void setOpenCardList(List<Integer> openCardList) {
		this.openCardList = openCardList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
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
		 * 性别
		 */
		private int sex;
		
		/**
		 * 金币
		 */
		private int gold;
		
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
		 * ip
		 */
		private String ip;
		
		/**
		 * 牌型
		 */
		private int type;
		
		/**
		 * 玩家手中的牌
		 */
		private List<Integer> cardList;
		
		/**
		 * 开牌时的牌
		 */
		private List<Integer> openCardList;
		
		/**
		 * 玩家下的注数
		 */
		private int caseAnte;
		
		/**
		 * 判断是否开牌
		 */
		private boolean openCard;
		
		/**
		 * 是否准备
		 */
		private boolean ready;
		
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

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
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
		
		public String getIp() {
			return ip;
		}
		
		public void setIp(String ip) {
			this.ip = ip;
		}
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
		public List<Integer> getCardList() {
			return cardList;
		}
		
		public void setCardList(List<Integer> cardList) {
			this.cardList = cardList;
		}
		
		public int getCaseAnte() {
			return caseAnte;
		}
		
		public void setCaseAnte(int caseAnte) {
			this.caseAnte = caseAnte;
		}

		public List<Integer> getOpenCardList() {
			return openCardList;
		}

		public void setOpenCardList(List<Integer> openCardList) {
			this.openCardList = openCardList;
		}

		public boolean isOpenCard() {
			return openCard;
		}

		public void setOpenCard(boolean openCard) {
			this.openCard = openCard;
		}

		public boolean isReady() {
			return ready;
		}

		public void setReady(boolean ready) {
			this.ready = ready;
		}
	}
}
