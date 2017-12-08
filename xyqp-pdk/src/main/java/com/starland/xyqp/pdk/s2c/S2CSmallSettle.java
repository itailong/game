package com.starland.xyqp.pdk.s2c;

import java.util.List;

public class S2CSmallSettle {

	/**
	 * 是否还有下一局
	 */
	private boolean hasNext;
	
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
		 * 分数的变化量
		 */
		private int offsetScore;
		
		/**
		 * 炸弹分数
		 */
		private int bombScore;
		
		
		/**
		 * 当前分数
		 */
		private int currentScore;
		
		/**
		 * 是否赢了
		 */
		private boolean win;
		
		/**
		 * 姓名
		 */
		private String name;
		
		/**
		 * 剩余的牌数
		 */
		private int surplus;

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

		public int getBombScore() {
			return bombScore;
		}

		public void setBombScore(int bombScore) {
			this.bombScore = bombScore;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSurplus() {
			return surplus;
		}

		public void setSurplus(int surplus) {
			this.surplus = surplus;
		}
	}
	
}
