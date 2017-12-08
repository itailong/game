package com.starland.xyqp.ddz.s2c;

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

	}
	
}
