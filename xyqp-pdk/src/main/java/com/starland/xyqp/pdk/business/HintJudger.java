package com.starland.xyqp.pdk.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.starland.tools.shape.JudgerHandler;
import com.starland.tools.shape.ShapeJudger;
import com.starland.tools.shape.match.CardMatcher;
import com.starland.tools.shape.match.CardPattern;
import com.starland.tools.shape.match.MatchNode;
import com.starland.tools.shape.match.MatchRule;
import com.starland.xyqp.pdk.model.CardShape;
import com.starland.xyqp.pdk.model.HintParam;

public class HintJudger implements ShapeJudger<HintParam, List<List<Integer>>> {

	/**
	 * 上家出的牌型
	 */
	private CardShape cardShape;
	
	private List<Integer> cardList;
	
	private List<Element> elements = new ArrayList<>();
	
	
	@Override
	public void init(HintParam hintParam) {
		cardShape = hintParam.getCardShape();
		cardList = new ArrayList<>(hintParam.getCardList().size());
		cardList.addAll(hintParam.getCardList());
		cardList.sort((carda, cardb) -> {
			int codea = carda % 100;
			int codeb = cardb % 100;
			if (codea != codeb) {
				return codea - codeb;
			}
			return carda / 100 - cardb / 100;
		});
		elements = parseElements(cardList);
	}
	
	@JudgerHandler(id="TYPE_SINGLE", priority=1)
	public List<List<Integer>> judgeSingle() {
		if (cardShape.getType() != CardShape.TYPE_SINGLE) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode()) {
				List<Integer> list = new ArrayList<>(1);
				list.add(element.cardList.get(0));
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_PAIR", priority=1)
	public List<List<Integer>> judgePair() {
		if (cardShape.getType() != CardShape.TYPE_PAIR) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode() && element.cardList.size() >= 2) {
				List<Integer> list = new ArrayList<>(2);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_THREE", priority=1)
	public List<List<Integer>> judgeThree() {
		if (cardShape.getType() != CardShape.TYPE_THREE_TWO) {
			return null;
		}
		if (cardList.size() < 3) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		if (cardList.size() > 5) {
			cardPattern.setEstimater(this::estimateThree);
		}
		MatchRule<Integer> matchRule = new MatchRule<>(3);
		matchRule.setMatchMethod((cards, node) -> {
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			Integer cardc = cards.get(2);
			if (cardShape.getWeightCode() >= carda % 100) {
				return false;
			}
			if (carda % 100 != cardb % 100 || cardb % 100 != cardc % 100) {
				return false;
			}
			return true;
		});
		cardPattern.addRule(matchRule);
		
		if (cardList.size() > 5) {
			matchRule = new MatchRule<>(1);
			matchRule.setRepeat(2);
			cardPattern.addRule(matchRule);
		} else {
			matchRule = new MatchRule<>(cardList.size() - 3);
			cardPattern.addRule(matchRule);
		}
		
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		int count = 0;
		while(matcher.find() && count < 10) {
			List<Integer> list = matcher.cardList();
			result.add(list);
			count++;
		}
		
		return result;
	}
	
	private double estimateThree(MatchNode<Integer> node) {
		MatchNode<Integer> frontNode = node.frontNode();
		List<Integer> surplus = node.getSurplus();
		double result = 0.0;
		if (null != frontNode) {
			result += frontNode.getValue();
		}
		// 越小的牌，优先级越高
		int card = node.getCards().get(0);
		result += card % 100 * (-1.0);
		for (int ca : surplus) {
			if (ca % 100 == card % 100) {
				result += -20.0;
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_CONTINUE_PAIR", priority=1)
	public List<List<Integer>> judgeContinuePair() {
		if (cardShape.getType() != CardShape.TYPE_CONTINUE_PAIR) {
			return null;
		}
		int continueSize = cardShape.getContinueSize();
		if (cardList.size() < continueSize * 2) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		MatchRule<Integer> matchRule = new MatchRule<>(2);
		matchRule.setMatchMethod((cards, node) -> {
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			if (cardShape.getWeightCode() - continueSize + 1 >= carda % 100) {
				return false;
			}
			if (carda % 100 != cardb % 100) {
				return false;
			}
			if (node.getRepeat() > 1) {
				MatchNode<Integer> frontNode = node.frontNode();
				int preCode = frontNode.getCards().get(0) % 100;
				if (carda %100 - preCode != 1) {
					return false;
				}
			}
			return true;
		});
		matchRule.setRepeat(continueSize);
		cardPattern.addRule(matchRule);
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		int count = 0;
		while(matcher.find() && count < 10) {
			List<Integer> list = matcher.cardList();
			result.add(list);
			count++;
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_PLANE", priority=1)
	public List<List<Integer>> judgePlane() {
		if (cardShape.getType() != CardShape.TYPE_PLANE) {
			return null;
		}
		int continueSize = cardShape.getContinueSize();
		if (cardList.size() < continueSize * 3) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		CardPattern<Integer> cardPattern = new CardPattern<>();
		cardPattern.setMatchEqual((carda, cardb) -> {
			return carda % 100 == cardb % 100;
		});
		if (cardList.size() > continueSize * 5) {
			cardPattern.setEstimater(this::estimatePlane);
		}
		MatchRule<Integer> matchRule = new MatchRule<>(3);
		matchRule.setMatchMethod((cards, node) -> {
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			Integer cardc = cards.get(2);
			if (cardShape.getWeightCode() - continueSize + 1 > carda % 100) {
				return false;
			}
			if (carda % 100 != cardb % 100 || cardb % 100 != cardc % 100) {
				return false;
			}
			if (node.getRepeat() > 1) {
				MatchNode<Integer> frontNode = node.frontNode();
				int preCode = frontNode.getCards().get(0) % 100;
				if (carda %100 - preCode != 1) {
					return false;
				}
			}
			return true;
		});
		matchRule.setRepeat(continueSize);
		cardPattern.addRule(matchRule);
		
		if (cardList.size() > continueSize * 5) {
			matchRule = new MatchRule<>(1);
			matchRule.setRepeat(continueSize * 2);
			cardPattern.addRule(matchRule);
		} else {
			matchRule = new MatchRule<>(cardList.size() - continueSize * 3);
			cardPattern.addRule(matchRule);
		}
		CardMatcher<Integer> matcher = cardPattern.matcher(cardList);
		int count = 0;
		while(matcher.find() && count < 10) {
			List<Integer> list = matcher.cardList();
			result.add(list);
			count++;
		}
		return result;
	}
	
	private double estimatePlane(MatchNode<Integer> node) {
		MatchNode<Integer> frontNode = node.frontNode();
		List<Integer> surplus = node.getSurplus();
		double result = 0.0;
		if (null != frontNode) {
			result += frontNode.getValue();
		}
		// 越小的牌，优先级越高
		int card = node.getCards().get(0);
		result += card % 100 * (-1.0);
		for (int ca : surplus) {
			if (ca % 100 == card % 100) {
				result += -20.0;
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_STRAIGHT", priority=1)
	public List<List<Integer>> judgeStraight() {
		if (cardShape.getType() != CardShape.TYPE_STRAIGHT) {
			return null;
		}
		int continueSize = cardShape.getContinueSize();
		if (cardList.size() < continueSize) {
			return null;
		}
		List<Element> list = new ArrayList<>();
		for (Element element : elements) {
			if (element.code == 1 || element.code == 16 || element.code == 17 || element.code == 15) {
				break;
			}
			list.add(element);
		}
		List<List<Integer>> result = new LinkedList<>();
		for (int i = 0; i < list.size() - continueSize + 1; i++) {
			List<Integer> cards = new LinkedList<>();
			Element preElement = null;
			for (int j = 0; j < continueSize; j++) {
				Element element = list.get(i + j);
				if (preElement != null && element.code - preElement.code != 1) {
					break;
				}
				cards.add(element.cardList.get(0));
				preElement = element;
			}
			if (cards.size() < continueSize) {
				continue;
			}
			Integer card = cards.get(cards.size() - 1);
			//有问题 需确认
			int code = card % 100;
			if (cardShape.getWeightCode() < code) {
				result.add(cards);
			}
		}
		return result;
	}
	
	/**
	 * 炸弹
	 * @return
	 */
	@JudgerHandler(id="TYPE_BOMB", priority=1)
	public List<List<Integer>> judgeBomb() {
		int weightCode = cardShape.getWeightCode();
		if (cardShape.getType() != CardShape.TYPE_BOMB) {
			weightCode = 0;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > weightCode && element.cardList.size() >= 4) {
				List<Integer> list = new ArrayList<>(4);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				list.add(element.cardList.get(2));
				list.add(element.cardList.get(3));
				result.add(list);
			}
		}
		return result;
	}
	
	
	@Override
	public List<List<List<Integer>>> filter(Map<String, List<List<Integer>>> resultMap) {
		List<List<List<Integer>>> result = ShapeJudger.super.filter(resultMap);
		Iterator<List<List<Integer>>> iter = result.iterator();
		while(iter.hasNext()) {
			List<List<Integer>> list = iter.next();
			if (list.isEmpty()) {
				iter.remove();
			}
		}
		return result;
	}
	
	/**
	 * （已经排好序）将牌解析成元素
	 * @param cards
	 * @return
	 */
	private List<Element> parseElements(List<Integer> cards) {
		List<Element> result = new ArrayList<>();
		Element element = null;
		for (Integer card : cards) {
			int code = card % 100;
			if (null == element || element.code != code) {
				element = new Element();
				element.code = code;
				element.cardList.add(card);
				result.add(element);
			} else {
				element.cardList.add(card);
			}
		}
		return result;
	}
	
	private static class Element {
		
		private int code;
		
		private List<Integer> cardList = new LinkedList<>();

		@Override
		public String toString() {
			return "Element [code=" + code + ", cardList=" + cardList + "]";
		}
		
	}
	
}
