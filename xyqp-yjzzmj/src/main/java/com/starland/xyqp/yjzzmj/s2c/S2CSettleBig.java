package com.starland.xyqp.yjzzmj.s2c;

import java.util.List;

public class S2CSettleBig {
	
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
		 * 自摸数量
		 */
		private int winSelfNum;
		
		/**
		 * 接炮数量
		 */
		private int winOtherNum;
		
		/**
		 * 暗杠数量
		 */
		private int hideBridgeNum;
		
		/**
		 * 明杠(包括过路杠)数量
		 */
		private int showBridgeNum;
		
		/**
		 * 点杠数量
		 */
		private int pointBridgeNum;
		
		/**
		 * 点炮
		 */
		private int pointCannonNum;
		
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

		public int getWinSelfNum() {
			return winSelfNum;
		}

		public void setWinSelfNum(int winSelfNum) {
			this.winSelfNum = winSelfNum;
		}

		public int getWinOtherNum() {
			return winOtherNum;
		}

		public void setWinOtherNum(int winOtherNum) {
			this.winOtherNum = winOtherNum;
		}

		public int getHideBridgeNum() {
			return hideBridgeNum;
		}

		public void setHideBridgeNum(int hideBridgeNum) {
			this.hideBridgeNum = hideBridgeNum;
		}

		public int getShowBridgeNum() {
			return showBridgeNum;
		}

		public void setShowBridgeNum(int showBridgeNum) {
			this.showBridgeNum = showBridgeNum;
		}

		public int getPointBridgeNum() {
			return pointBridgeNum;
		}

		public void setPointBridgeNum(int pointBridgeNum) {
			this.pointBridgeNum = pointBridgeNum;
		}

		public int getPointCannonNum() {
			return pointCannonNum;
		}

		public void setPointCannonNum(int pointCannonNum) {
			this.pointCannonNum = pointCannonNum;
		}

	}
	
}
