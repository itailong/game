package com.starland.xyqp.zjhjb.s2c;

import java.util.List;

public class S2CAllocateCard {

	/**
	 * 出牌人的位置并且庄家的位置
	 */
	private int position;
	
	/**
	 * 桌上的总注数
	 */
	private int totalChips;
	
	/**
	 * 初始单注
	 */
	private int singleAnte;
	
	/**
	 * 每个人的金币信息
	 */
	private List<GoldInfo> goldInfos;
	

	public int getTotalChips() {
		return totalChips;
	}

	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	public List<GoldInfo> getGoldInfos() {
		return goldInfos;
	}

	public void setGoldInfos(List<GoldInfo> goldInfos) {
		this.goldInfos = goldInfos;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getSingleAnte() {
		return singleAnte;
	}

	public void setSingleAnte(int singleAnte) {
		this.singleAnte = singleAnte;
	}

	public static class GoldInfo{
		
		/**
		 * 玩家的金币数
		 */
		private int gold;
		
		/**
		 * 玩家下的总注数
		 */
		private int totalAnte;
		
		/**
		 * 玩家座位号
		 */
		private int seatPosition;

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

		public int getTotalAnte() {
			return totalAnte;
		}

		public void setTotalAnte(int totalAnte) {
			this.totalAnte = totalAnte;
		}
	}
}
