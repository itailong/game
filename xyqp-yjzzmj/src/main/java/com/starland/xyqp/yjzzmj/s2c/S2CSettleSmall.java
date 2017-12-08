package com.starland.xyqp.yjzzmj.s2c;

import java.util.List;

public class S2CSettleSmall {

	/**
	 * 是否还有下一局
	 */
	private boolean hasNext;
	
	/**
	 * 是否流局
	 */
	private boolean flow;
	
	/**
	 * 扎鸟的牌
	 */
	private Integer zhaniaoCard;
	
	/**
	 * 扎鸟的位置
	 */
	private int zhaniaoPosition;
	
	private List<Detail> details;
	
	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isFlow() {
		return flow;
	}

	public void setFlow(boolean flow) {
		this.flow = flow;
	}

	public Integer getZhaniaoCard() {
		return zhaniaoCard;
	}

	public void setZhaniaoCard(Integer zhaniaoCard) {
		this.zhaniaoCard = zhaniaoCard;
	}

	public int getZhaniaoPosition() {
		return zhaniaoPosition;
	}

	public void setZhaniaoPosition(int zhaniaoPosition) {
		this.zhaniaoPosition = zhaniaoPosition;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public static class Detail {
		
		/**
		 * 位置
		 */
		private int position;
		
		/**
		 * 分数的变化量
		 */
		private int offsetScore;
		
		/**
		 * 当前分数
		 */
		private int currentScore;
		
		/**
		 * 描述
		 */
		private String description;
		
		/**
		 * 杠，报喜的分数
		 */
		private int bridgeScore;
		
		/**
		 * 胡牌的分数
		 */
		private int winScroe;
		
		/**
		 * 手上的牌
		 */
		private List<Integer> cardList;
		
		/**
		 * 胡的那张牌
		 */
		private Integer winCard;
		
		/**
		 * 碰牌的列表
		 */
		private List<Integer> bumpList;
		
		/**
		 * 明杠的列表
		 */
		private List<Integer> showBridgeList;
		
		/**
		 * 暗杠的列表
		 */
		private List<Integer> hideBridgeList;
		
		/**
		 * 过路杠（碰后杠）的列表
		 */
		private List<Integer> passBridgeList;
		
		/**
		 * 是否赢了
		 */
		private boolean win;
		
		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public int getOffsetScore() {
			return offsetScore;
		}

		public void setOffsetScore(int offsetScore) {
			this.offsetScore = offsetScore;
		}

		public int getCurrentScore() {
			return currentScore;
		}

		public void setCurrentScore(int currentScore) {
			this.currentScore = currentScore;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public List<Integer> getBumpList() {
			return bumpList;
		}

		public void setBumpList(List<Integer> bumpList) {
			this.bumpList = bumpList;
		}

		public List<Integer> getCardList() {
			return cardList;
		}

		public void setCardList(List<Integer> cardList) {
			this.cardList = cardList;
		}

		public Integer getWinCard() {
			return winCard;
		}

		public void setWinCard(Integer winCard) {
			this.winCard = winCard;
		}

		public boolean isWin() {
			return win;
		}

		public void setWin(boolean win) {
			this.win = win;
		}

		public int getBridgeScore() {
			return bridgeScore;
		}

		public void setBridgeScore(int bridgeScore) {
			this.bridgeScore = bridgeScore;
		}

		public int getWinScroe() {
			return winScroe;
		}

		public void setWinScroe(int winScroe) {
			this.winScroe = winScroe;
		}

	}
	
}
