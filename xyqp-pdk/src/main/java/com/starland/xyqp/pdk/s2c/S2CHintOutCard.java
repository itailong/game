package com.starland.xyqp.pdk.s2c;

import java.util.List;

public class S2CHintOutCard {

	private boolean canOut;
	
	private boolean canPass;
	
	private List<List<Integer>> hintCards;

	public boolean isCanOut() {
		return canOut;
	}

	public void setCanOut(boolean canOut) {
		this.canOut = canOut;
	}

	public boolean isCanPass() {
		return canPass;
	}

	public void setCanPass(boolean canPass) {
		this.canPass = canPass;
	}

	public List<List<Integer>> getHintCards() {
		return hintCards;
	}

	public void setHintCards(List<List<Integer>> hintCards) {
		this.hintCards = hintCards;
	}
	
}
