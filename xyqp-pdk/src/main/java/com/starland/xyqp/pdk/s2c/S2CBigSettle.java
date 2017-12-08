package com.starland.xyqp.pdk.s2c;

import java.util.Date;
import java.util.List;

public class S2CBigSettle {
	
	private List<Detail> details;
	
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
		 * 当前分数
		 */
		private int currentScore;
		
		/**
		 * 总炸弹数
		 */
		private int bombNums;
		
		/**
		 * 时间
		 */
		private Date settleTime;
		
		/**
		 * 单局最高得分
		 */
		private int highPoint;
		
		/**
		 * 胜局数
		 */
		private int winRound;
		
		/**
		 * 负局数
		 */
		private int loseRound;
		
		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public int getCurrentScore() {
			return currentScore;
		}

		public void setCurrentScore(int currentScore) {
			this.currentScore = currentScore;
		}

		public int getBombNums() {
			return bombNums;
		}

		public void setBombNums(int bombNums) {
			this.bombNums = bombNums;
		}

		public Date getSettleTime() {
			return settleTime;
		}

		public void setSettleTime(Date settleTime) {
			this.settleTime = settleTime;
		}

		public int getHighPoint() {
			return highPoint;
		}

		public void setHighPoint(int highPoint) {
			this.highPoint = highPoint;
		}

		public int getWinRound() {
			return winRound;
		}

		public void setWinRound(int winRound) {
			this.winRound = winRound;
		}

		public int getLoseRound() {
			return loseRound;
		}

		public void setLoseRound(int loseRound) {
			this.loseRound = loseRound;
		}
		
	}
	
}
