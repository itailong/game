package com.starland.xyqp.yjzzmj.business;

import java.util.LinkedList;
import java.util.List;

public class CardElement {
	
	/**
	 * 对子
	 */
	public static final int TYPE_PAIR = 1;
	
	/**
	 * 顺子
	 */
	public static final int TYPE_STRAIGHT = 2;
	
	/**
	 * 刻子
	 */
	public static final int TYPE_TRIPLET = 3;

	/**
	 * 父节点
	 */
	private CardElement parent;
	
	/**
	 * 离散的牌的集合
	 */
	private List<Integer> discreteCards;
	
	/**
	 * 组合中的牌
	 */
	private List<Integer> group;
	
	/**
	 * 组合类型
	 */
	private int type;

	public CardElement(List<Integer> discreteCards) {
		this.discreteCards = discreteCards;
	}

	public CardElement(CardElement parent, List<Integer> group, int type) {
		this.parent = parent;
		this.group = group;
		this.type = type;
		this.discreteCards = new LinkedList<>();
		this.discreteCards.addAll(parent.discreteCards);
		for (Integer card : group) {
			this.discreteCards.remove(card);
		}
	}

	public CardElement getParent() {
		return parent;
	}

	public List<Integer> getDiscreteCards() {
		return discreteCards;
	}

	public List<Integer> getGroup() {
		return group;
	}

	public int getType() {
		return type;
	}
	
}
