package com.starland.xyqp.yjzzmj.s2c;

import java.util.List;

public class S2CShowOperate {
	
	/**
	 * 是否能胡
	 */
	private boolean canWin;
	
	/**
	 * 是否能碰
	 */
	private boolean canBump;
	
	/**
	 * 是否能杠
	 */
	private boolean canBridge;
	
	/**
	 * 可以杠牌的列表
	 */
	private List<Integer> bridgeList;
	
	/**
	 * 是否能听
	 */
	private boolean canHear;
	
	/**
	 * 听列表
	 */
	private List<HearDetail> hearDetailList;

	public boolean isCanWin() {
		return canWin;
	}

	public void setCanWin(boolean canWin) {
		this.canWin = canWin;
	}

	public boolean isCanBump() {
		return canBump;
	}

	public void setCanBump(boolean canBump) {
		this.canBump = canBump;
	}

	public boolean isCanBridge() {
		return canBridge;
	}

	public void setCanBridge(boolean canBridge) {
		this.canBridge = canBridge;
	}

	public List<Integer> getBridgeList() {
		return bridgeList;
	}

	public void setBridgeList(List<Integer> bridgeList) {
		this.bridgeList = bridgeList;
	}
	
	public boolean isCanHear() {
		return canHear;
	}

	public void setCanHear(boolean canHear) {
		this.canHear = canHear;
	}

	public List<HearDetail> getHearDetailList() {
		return hearDetailList;
	}

	public void setHearDetailList(List<HearDetail> hearDetailList) {
		this.hearDetailList = hearDetailList;
	}

	public static class HearDetail {

		/**
		 * 听牌时要打出去的牌
		 */
		private Integer card;
		
		/**
		 * 听牌的列表
		 */
		private List<Integer> cardList;
		
		public Integer getCard() {
			return card;
		}

		public void setCard(Integer card) {
			this.card = card;
		}

		public List<Integer> getCardList() {
			return cardList;
		}

		public void setCardList(List<Integer> cardList) {
			this.cardList = cardList;
		}

	}
}
