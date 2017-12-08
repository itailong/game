package com.starland.xyqp.ddz.s2c;

import java.util.List;

public class S2CSmallSettle {

	/**
	 * 是否还有下一局
	 */
	private boolean hasNext;
	
	/**
	 * 炸弹数量
	 */
	private int bombCount;
	
	/**
	 * 是否是春天
	 */
	private boolean spring;
	
	/**
	 * 当前是第几盘
	 */
	private int roundCount;
	
	private List<Detail> details;
	
	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public int getBombCount() {
		return bombCount;
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public boolean isSpring() {
		return spring;
	}

	public void setSpring(boolean spring) {
		this.spring = spring;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
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
		 * 是否是地主
		 */
		private boolean landlord;
		
		/**
		 * 分数的变化量
		 */
		private int offsetScore;
		
		/**
		 * 当前分数
		 */
		private int currentScore;
		
		/**
		 * 是否赢了
		 */
		private boolean win;
		
		/**
		 * 倍数
		 */
		private int multiple;
		
		/**
		 * 底分
		 */
		private int floorScore;
		
		private List<Integer> cards;
		
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

		public boolean isWin() {
			return win;
		}

		public void setWin(boolean win) {
			this.win = win;
		}

		public boolean isLandlord() {
			return landlord;
		}

		public void setLandlord(boolean landlord) {
			this.landlord = landlord;
		}

		public int getMultiple() {
			return multiple;
		}

		public void setMultiple(int multiple) {
			this.multiple = multiple;
		}

		public List<Integer> getCards() {
			return cards;
		}

		public void setCards(List<Integer> cards) {
			this.cards = cards;
		}

		public int getFloorScore() {
			return floorScore;
		}

		public void setFloorScore(int floorScore) {
			this.floorScore = floorScore;
		}

	}
	
}
