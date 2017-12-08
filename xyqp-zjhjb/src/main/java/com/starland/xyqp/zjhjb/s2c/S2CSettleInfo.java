package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

public class S2CSettleInfo {

	/**
	 * 赢家的位置
	 */
	private int winSeatPosition;
	
	/**
	 * 自己的牌 
	 */
	private List<Integer> cardList;
	
	/**
	 * 牌的类型
	 */
	private int type;
	
	private List<SeatGoldInfo> seatGoldInfo;
	
	public int getWinSeatPosition() {
		return winSeatPosition;
	}

	public void setWinSeatPosition(int winSeatPosition) {
		this.winSeatPosition = winSeatPosition;
	}

	public List<SeatGoldInfo> getSeatGoldInfo() {
		return seatGoldInfo;
	}

	public void setSeatGoldInfo(List<SeatGoldInfo> seatGoldInfo) {
		this.seatGoldInfo = seatGoldInfo;
	}
	
	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static class SeatGoldInfo{
		
		/**
		 * 玩家位置 
		 */
		private int seatPosition;
		
		/**
		 * 玩家的金币
		 */
		private int gold;
		
		/**
		 * 玩家下的总注数
		 */
		private int totalAnte;
		
		/**
		 * 玩家的牌
		 */
		private List<Integer> cardList;
		
		/**
		 * 玩家牌的类型
		 */
		private int type;
		
		public int getSeatPosition() {
			return seatPosition;
		}

		public void setSeatPosition(int seatPosition) {
			this.seatPosition = seatPosition;
		}

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
		}

		public int getTotalAnte() {
			return totalAnte;
		}

		public void setTotalAnte(int totalAnte) {
			this.totalAnte = totalAnte;
		}

		public List<Integer> getCardList() {
			return cardList;
		}

		public void setCardList(List<Integer> cardList) {
			this.cardList = cardList;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
	}
}
