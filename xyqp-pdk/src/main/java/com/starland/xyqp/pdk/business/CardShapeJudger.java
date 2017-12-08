package com.starland.xyqp.pdk.business;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.starland.tools.shape.JudgerHandler;
import com.starland.tools.shape.ShapeJudger;
import com.starland.tools.shape.match.CardMatcher;
import com.starland.tools.shape.match.CardPattern;
import com.starland.tools.shape.match.MatchNode;
import com.starland.tools.shape.match.MatchRule;
import com.starland.xyqp.common.exception.LogicException;
import com.starland.xyqp.pdk.model.CardShape;

public class CardShapeJudger implements ShapeJudger<List<Integer>, CardShape> {

	private List<Integer> cardList;
	
	@Override
	public void init(List<Integer> data) {
		cardList = new LinkedList<>();
		cardList.addAll(data);
		cardList.sort((o1, o2) -> {
			return o1 % 100 - o2 % 100;
		});
	}
	
	/**
	 * 判断单牌
	 * @return
	 */
	@JudgerHandler(id="TYPE_SINGLE", priority=1)
	public CardShape judgeSingle() {
		if (cardList.size() != 1) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(cardList.get(0) % 100);
		cardShape.setType(CardShape.TYPE_SINGLE);
		cardShape.setComplete(true);
		return cardShape;
	}
	
	/**
	 * 判断对子
	 * @return
	 */
	@JudgerHandler(id="TYPE_PAIR", priority=1)
	public CardShape judegPair() {
		if (cardList.size() != 2) {
			return null;
		}
		Integer carda = cardList.get(0);
		Integer cardb = cardList.get(1);
		if (carda % 100 != cardb % 100) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(carda % 100);
		cardShape.setType(CardShape.TYPE_PAIR);
		cardShape.setComplete(true);
		return cardShape;
	}
	
	/**
	 * 判断三个
	 * @return
	 */
	@JudgerHandler(id="TYPE_THREE", priority=1)
	public CardShape judgeThree() {
		if (cardList.size() < 3 || cardList.size() > 5) {
			return null;
		}
		if(cardList.size() == 4){
			if(cardList.get(0) % 100 == cardList.get(1) % 100 && cardList.get(2) % 100 == cardList.get(3) % 100 && cardList.get(1) % 100 == cardList.get(2) % 100){
				return null;
			}
		}
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		
		MatchRule<Integer> matchRule = new MatchRule<>(3);
		matchRule.setMatchMethod((cards, node) -> {
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			Integer cardc = cards.get(2);
			if (carda % 100 != cardb % 100 || cardb % 100 != cardc % 100) {
				return false;
			}
			return true;
		});
		cardPattern.addRule(matchRule);
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		while(matcher.find()) {
			List<List<Integer>> cardListList = matcher.cardListList();
			List<Integer> surplus = matcher.surplus();
			CardShape cardShape = new CardShape();
			if (surplus.size() == 2) {
				cardShape.setComplete(true);
			}
			cardShape.setWeightCode(cardListList.get(cardListList.size() - 1).get(0) % 100);
			cardShape.setType(CardShape.TYPE_THREE_TWO);
			return cardShape;
		}
		return null;
	}
	
	/**
	 * 判断炸弹
	 * @return
	 */
	@JudgerHandler(id="TYPE_BOMB", priority=1)
	public CardShape judgeBomb() {
		if (cardList.size() != 4) {
			return null;
		}
		Integer carda = cardList.get(0);
		Integer cardb = cardList.get(1);
		Integer cardc = cardList.get(2);
		Integer cardd = cardList.get(3);
		if (carda % 100 != cardb % 100 || cardb % 100 != cardc % 100 || cardc % 100 != cardd % 100) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(carda % 100);
		cardShape.setType(CardShape.TYPE_BOMB);
		cardShape.setComplete(true);
		return cardShape;
	}
	
	/**
	 * 判断连对
	 * @return
	 */
	@JudgerHandler(id="TYPE_CONTINUE_PAIR", priority=1)
	public CardShape judgeContinuePair(){
		if (cardList.size() < 4) {
			return null;
		}
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		MatchRule<Integer> matchRule = new MatchRule<>(2);
		matchRule.setMatchMethod((cards, node) -> {
			MatchNode<Integer> frontNode = node.frontNode();
			Integer preCode = null;
			if (node.getRepeat() > 1) {
				preCode = frontNode.getCards().get(0) % 100;
			}
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			if (preCode != null && carda % 100 - preCode != 1) {
				return false;
			}
			if (carda % 100 != cardb % 100) {
				return false;
			}
			return true;
		});
		matchRule.setRepeat(2, 8);
		cardPattern.addRule(matchRule);
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		if (matcher.matches()) {
			List<List<Integer>> cardListList = matcher.cardListList();
			CardShape cardShape = new CardShape();
			cardShape.setWeightCode(cardListList.get(cardListList.size() - 1).get(0) % 100);
			cardShape.setContinueSize(cardListList.size());
			cardShape.setType(CardShape.TYPE_CONTINUE_PAIR);
			cardShape.setComplete(true);
			return cardShape;
		}
		return null;
	}
	
	/**
	 * 判断飞机
	 * @return
	 */
	@JudgerHandler(id="TYPE_PLANE", priority=1)
	public CardShape judgePlane(){
		if (cardList.size() < 6) {
			return null;
		}
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		MatchRule<Integer> matchRule = new MatchRule<>(3);
		matchRule.setMatchMethod((cards, node) -> {
			MatchNode<Integer> frontNode = node.frontNode();
			Integer preCode = null;
			if (node.getRepeat() > 1) {
				preCode = frontNode.getCards().get(0) % 100;
			}
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			Integer cardc = cards.get(2);
			if (preCode != null && carda % 100 - preCode != 1) {
				return false;
			}
			if (carda % 100 != cardb % 100 || cardb % 100 != cardc % 100) {
				return false;
			}
			return true;
		});
		matchRule.setRepeat(2, 4);
		cardPattern.addRule(matchRule);
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		while(matcher.find()) {
			List<List<Integer>> cardListList = matcher.cardListList();
			List<Integer> surplus = matcher.surplus();
			CardShape cardShape = new CardShape();
			if(surplus.size() <= cardListList.size() * 2){
				if (surplus.size() != cardListList.size() * 2) {
					cardShape.setComplete(false);
				}else{
					cardShape.setComplete(true);
				}
				cardShape.setWeightCode(cardListList.get(cardListList.size() - 1).get(0) % 100);
				cardShape.setContinueSize(cardListList.size());
				cardShape.setType(CardShape.TYPE_PLANE);
				return cardShape;
			}else{
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 判断顺子
	 * @return
	 */
	@JudgerHandler(id="TYPE_STRAIGHT", priority=1)
	public CardShape judgeStraight() {
		if (cardList.size() < 5) {
			return null;
		}
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		MatchRule<Integer> matchRule = new MatchRule<>(1);
		matchRule.setMatchMethod((cards, node) -> {
			MatchNode<Integer> frontNode = node.frontNode();
			if (node.getRepeat() == 1) {
				return true;
			}
			Integer preCode = frontNode.getCards().get(0) % 100;
			Integer card = cards.get(0);
			if (card % 100 - preCode != 1) {
				return false;
			}
			return true;
		});
		matchRule.setRepeat(5, 16);
		cardPattern.addRule(matchRule);
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		if (matcher.matches()) {
			List<List<Integer>> cardListList = matcher.cardListList();
			CardShape cardShape = new CardShape();
			cardShape.setType(CardShape.TYPE_STRAIGHT);
			cardShape.setWeightCode(cardListList.get(cardListList.size() - 1).get(0) % 100);
			cardShape.setContinueSize(cardListList.size());
			cardShape.setComplete(true);
			return cardShape;
		}
		return null;
	}
	
	@Override
	public List<CardShape> filter(Map<String, CardShape> resultMap) {
		if (resultMap.size() > 1) {
			throw new LogicException("只可能出现一种牌型！");
		}
		return ShapeJudger.super.filter(resultMap);
	}
}
