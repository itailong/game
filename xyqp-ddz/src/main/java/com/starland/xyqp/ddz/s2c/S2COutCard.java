package com.starland.xyqp.ddz.s2c;

import java.util.List;

public class S2COutCard {

	private int position;
	
	private boolean canOut;
	
	private List<Integer> cards;
	
	private int cardType;
	
	private int multiple;


	public int getPosition() {
		return position;
	}

	public boolean isCanOut() {
		return canOut;
	}

	public void setCanOut(boolean canOut) {
		this.canOut = canOut;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Integer> getCards() {
		return cards;
	}

	public void setCards(List<Integer> cards) {
		this.cards = cards;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
}
