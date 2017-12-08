package com.starland.xyqp.ddz.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.starland.tools.shape.JudgerHandler;
import com.starland.tools.shape.ShapeJudger;
import com.starland.xyqp.ddz.model.CardShape;
import com.starland.xyqp.ddz.model.HintParam;

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
			int codea = getCardCode(carda);
			int codeb = getCardCode(cardb);
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
		if (cardShape.getType() != CardShape.TYPE_THREE) {
			return null;
		}
		if (cardList.size() < 3) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode() && element.cardList.size() >= 3) {
				List<Integer> list = new ArrayList<>(3);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				list.add(element.cardList.get(2));
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_THREE_SINGLE", priority=1)
	public List<List<Integer>> judgeThreeSingle() {
		if (cardShape.getType() != CardShape.TYPE_THREE_SINGLE) {
			return null;
		}
		if (cardList.size() < 4) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode() && element.cardList.size() >= 3) {
				List<Integer> list = new ArrayList<>(4);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				list.add(element.cardList.get(2));
				List<Integer> single = findSingle(list, 1);
				list.addAll(single);
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_THREE_PAIR", priority=1)
	public List<List<Integer>> judgeThreePair() {
		if (cardShape.getType() != CardShape.TYPE_THREE_PAIR) {
			return null;
		}
		if (cardList.size() < 5) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode() && element.cardList.size() >= 3) {
				List<Integer> list = new ArrayList<>(5);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				list.add(element.cardList.get(2));
				List<Integer> pair = findPair(list, 1);
				if (pair.isEmpty()) {
					return null;
				}
				list.addAll(pair);
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_FOUR_SINGLE", priority=1)
	public List<List<Integer>> judgeFourSingle() {
		if (cardShape.getType() != CardShape.TYPE_FOUR_SINGLE) {
			return null;
		}
		if (cardList.size() < 6) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode() && element.cardList.size() >= 4) {
				List<Integer> list = new ArrayList<>(6);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				list.add(element.cardList.get(2));
				list.add(element.cardList.get(3));
				List<Integer> single = findSingle(list, 2);
				list.addAll(single);
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_FOUR_PAIR", priority=1)
	public List<List<Integer>> judgeFoutPair() {
		if (cardShape.getType() != CardShape.TYPE_FOUR_PAIR) {
			return null;
		}
		if (cardList.size() < 8) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (Element element : elements) {
			if (element.code > cardShape.getWeightCode() && element.cardList.size() >= 4) {
				List<Integer> list = new ArrayList<>(8);
				list.add(element.cardList.get(0));
				list.add(element.cardList.get(1));
				list.add(element.cardList.get(2));
				list.add(element.cardList.get(3));
				List<Integer> pair = findPair(list, 2);
				if (pair.size() < 2) {
					return null;
				}
				list.addAll(pair);
				result.add(list);
			}
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_BOMB", priority=3)
	public List<List<Integer>> judgeBomb() {
		// 普通炸弹大不起王炸
		if (cardShape.getType() == CardShape.TYPE_KING_BOMB) {
			return null;
		}
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
	
	@JudgerHandler(id="TYPE_KING_BOMB", priority=4)
	public List<List<Integer>> judgeKingBomb() {
		if (!cardList.contains(2) || !cardList.contains(3)) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		List<Integer> list = new ArrayList<>(2);
		list.add(2);
		list.add(3);
		result.add(list);
		return result;
	}
	
	@JudgerHandler(id="TYPE_GHOST", priority=2)
	public List<List<Integer>> judgeGhost() {
		// 鬼牌打不起王炸
		if (cardShape.getType() == CardShape.TYPE_KING_BOMB) {
			return null;
		}
		// 鬼牌打不起炸弹
		if (cardShape.getType() == CardShape.TYPE_BOMB) {
			return null;
		}
		if (cardShape.getType() == CardShape.TYPE_GHOST) {
			return null;
		}
		if (!cardList.contains(1)) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		List<Integer> list = new ArrayList<>(1);
		list.add(1);
		result.add(list);
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
		for (int i = 0; i < elements.size() - continueSize + 1; i++) {
			List<Integer> cards = new LinkedList<>();
			Element preElement = null;
			for (int j = 0; j < continueSize; j++) {
				Element element = elements.get(i + j);
				if(element.code == 15){
					break;
				}
				if (element.cardList.size() < 2) {
					break;
				}
				if (preElement != null && element.code - preElement.code != 1) {
					break;
				}
				cards.add(element.cardList.get(0));
				cards.add(element.cardList.get(1));
				preElement = element;
			}
			if (cards.size() < continueSize * 2) {
				continue;
			}
			Integer card = cards.get(cards.size() - 1);
			int code = getCardCode(card);
			if (cardShape.getWeightCode() < code) {
				result.add(cards);
			}
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
		for (int i = 0; i < elements.size() - continueSize + 1; i++) {
			List<Integer> cards = new LinkedList<>();
			Element preElement = null;
			for (int j = 0; j < continueSize; j++) {
				Element element = elements.get(i + j);
				if (element.cardList.size() < 3) {
					break;
				}
				if(element.code == 15){
					break;
				}
				if (preElement != null && element.code - preElement.code != 1) {
					break;
				}
				cards.add(element.cardList.get(0));
				cards.add(element.cardList.get(1));
				cards.add(element.cardList.get(2));
				preElement = element;
			}
			if (cards.size() < continueSize * 3) {
				continue;
			}
			result.add(cards);
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_PLANE_SINGLE", priority=1)
	public List<List<Integer>> judgePlaneSingle() {
		if (cardShape.getType() != CardShape.TYPE_PLANE_SINGLE) {
			return null;
		}
		int continueSize = cardShape.getContinueSize();
		if (cardList.size() < continueSize * 4) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (int i = 0; i < elements.size() - continueSize + 1; i++) {
			List<Integer> cards = new LinkedList<>();
			Element preElement = null;
			for (int j = 0; j < continueSize; j++) {
				Element element = elements.get(i + j);
				if(element.code == 15){
					break;
				}
				if (element.cardList.size() < 3) {
					break;
				}
				if (preElement != null && element.code - preElement.code != 1) {
					break;
				}
				cards.add(element.cardList.get(0));
				cards.add(element.cardList.get(1));
				cards.add(element.cardList.get(2));
				preElement = element;
			}
			if (cards.size() < continueSize * 3) {
				continue;
			}
			List<Integer> single = findSingle(cards, continueSize);
			cards.addAll(single);
			result.add(cards);
		}
		return result;
	}
	
	@JudgerHandler(id="TYPE_PLANE_PAIR", priority=1)
	public List<List<Integer>> judgePlanePair() {
		if (cardShape.getType() != CardShape.TYPE_PLANE_PAIR) {
			return null;
		}
		int continueSize = cardShape.getContinueSize();
		if (cardList.size() < continueSize * 5) {
			return null;
		}
		List<List<Integer>> result = new LinkedList<>();
		for (int i = 0; i < elements.size() - continueSize + 1; i++) {
			List<Integer> cards = new LinkedList<>();
			Element preElement = null;
			for (int j = 0; j < continueSize; j++) {
				Element element = elements.get(i + j);
				if(element.code == 15){
					break;
				}
				if (element.cardList.size() < 3) {
					break;
				}
				if (preElement != null && element.code - preElement.code != 1) {
					break;
				}
				cards.add(element.cardList.get(0));
				cards.add(element.cardList.get(1));
				cards.add(element.cardList.get(2));
				preElement = element;
			}
			if (cards.size() < continueSize * 3) {
				continue;
			}
			List<Integer> pair = findPair(cards, continueSize);
			if (pair.size() < continueSize) {
				continue;
			}
			cards.addAll(pair);
			result.add(cards);
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
				if(element.code == 15){
					break;
				}
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
			int code = getCardCode(card);
			if (cardShape.getWeightCode() < code) {
				result.add(cards);
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
	 * 查找几张单牌
	 * @param exclude 排除的牌
	 * @param size 数量
	 * @return
	 */
	private List<Integer> findSingle(List<Integer> exclude, int size) {
		List<Integer> result = new ArrayList<>(size);
		List<Integer> cards = new ArrayList<>(cardList.size());
		cards.addAll(cardList);
		cards.removeAll(exclude);
		List<Element> elementList = parseElements(cards);
		elementList.sort((ele1, ele2) -> {
			if (ele1.code == 1) {
				return 1;
			}
			if (ele2.code == 1) {
				return -1;
			}
			return ele1.cardList.size() - ele2.cardList.size();
		});
		for (Element element : elementList) {
			for (Integer card : element.cardList) {
				if (result.size() >= size) {
					break;
				}
				result.add(card);
			}
		}
		return result;
	}
	
	/**
	 * 查找几张对子
	 * @param exclude 排除的牌
	 * @param size 数量
	 * @return
	 */
	private List<Integer> findPair(List<Integer> exclude, int size) {
		List<Integer> result = new ArrayList<>(size);
		List<Integer> cards = new ArrayList<>(cardList.size());
		cards.addAll(cardList);
		cards.removeAll(exclude);
		List<Element> elementList = parseElements(cards);
		elementList.sort((ele1, ele2) -> {
			return ele1.cardList.size() - ele2.cardList.size();
		});
		for (Element element : elementList) {
			if (result.size() >= size) {
				break;
			}
			if (element.cardList.size() >= 2) {
				result.add(element.cardList.get(0));
				result.add(element.cardList.get(1));
			}
		}
		return result;
	}
	
	/**
	 * 将牌解析成元素
	 * @param cards
	 * @return
	 */
	private List<Element> parseElements(List<Integer> cards) {
		List<Element> result = new ArrayList<>();
		Element element = null;
		for (Integer card : cards) {
			int code = getCardCode(card);
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
	
	/**
	 * 获取单牌大小的编号
	 * @param card
	 * @return
	 */
	private int getCardCode(int card) {
		// 鬼牌为1
		if (card == 1) {
			return 1;
		}
		// 小王
		if (card == 2) {
			return 16;
		}
		// 大王
		if (card == 3) {
			return 17;
		}
		// 普通牌
		return card % 100;
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
