package com.starland.xyqp.ddz.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.starland.tools.shape.JudgerHandler;
import com.starland.tools.shape.ShapeJudger;
import com.starland.xyqp.common.exception.LogicException;
import com.starland.xyqp.ddz.model.CardShape;

public class CardShapeJudger implements ShapeJudger<List<Integer>, CardShape> {

	/**
	 * 所有牌对应编号的集合，暗升序排列
	 */
	private List<Integer> codeList;
	
	/**
	 * 所有牌对应编号的数量的表
	 */
	private Map<Integer, Integer> countMap;
	
	@Override
	public void init(List<Integer> data) {
		codeList = new ArrayList<>(data.size());
		for (Integer card : data) {
			int code = getCardCode(card);
			codeList.add(code);
		}
		codeList.sort((o1, o2) -> {
			return o1 - o2;
		});
		countMap = new LinkedHashMap<>();
		for (Integer code : codeList) {
			Integer count = countMap.get(code);
			if (count == null) {
				countMap.put(code, 1);
			} else {
				countMap.put(code, count + 1);
			}
		}
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
	
	/**
	 * 判断单牌
	 * @return
	 */
	@JudgerHandler(id="TYPE_SINGLE", priority=1)
	public CardShape judgeSingle() {
		if (codeList.size() != 1) {
			return null;
		}
		Integer code = codeList.get(0);
		// 鬼牌不是单牌
		if (code.intValue() == 1) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(code);
		cardShape.setType(CardShape.TYPE_SINGLE);
		return cardShape;
	}
	
	/**
	 * 判断对子
	 * @return
	 */
	@JudgerHandler(id="TYPE_PAIR", priority=1)
	public CardShape judegPair() {
		if (codeList.size() != 2) {
			return null;
		}
		Integer codea = codeList.get(0);
		Integer codeb = codeList.get(1);
		if (!codea.equals(codeb)) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(codea);
		cardShape.setType(CardShape.TYPE_PAIR);
		return cardShape;
	}
	
	/**
	 * 判断三个
	 * @return
	 */
	@JudgerHandler(id="TYPE_THREE", priority=1)
	public CardShape judgeThree() {
		if (codeList.size() != 3) {
			return null;
		}
		Integer carda = codeList.get(0);
		Integer cardb = codeList.get(1);
		Integer cardc = codeList.get(2);
		if (!carda.equals(cardb) || !cardb.equals(cardc)) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(carda);
		cardShape.setType(CardShape.TYPE_THREE);
		return cardShape;
	}
	
	/**
	 * 判断三带一个
	 * @return
	 */
	@JudgerHandler(id="TYPE_THREE_SINGLE", priority=1)
	public CardShape judgeThreeSingle() {
		if (codeList.size() != 4) {
			return null;
		}
		boolean flag = false;
		int card = 0;
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count == 3) {
				card = entry.getKey();
				flag = true;
				break;
			}
		}
		if (!flag) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(card);
		cardShape.setType(CardShape.TYPE_THREE_SINGLE);
		return cardShape;
	}
	
	/**
	 * 判断三带一对
	 * @return
	 */
	@JudgerHandler(id="TYPE_THREE_PAIR", priority=1)
	public CardShape judgeThreePair() {
		if (codeList.size() != 5) {
			return null;
		}
		boolean three = false;
		boolean pair = false;
		int card = 0;
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count == 3) {
				card = entry.getKey();
				three = true;
			}
			if (count == 2) {
				pair = true;
			}
		}
		if (!three || !pair) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(card);
		cardShape.setType(CardShape.TYPE_THREE_PAIR);
		return cardShape;
	}
	
	/**
	 * 判断四带二
	 * @return
	 */
	@JudgerHandler(id="TYPE_FOUR_SINGLE", priority=1)
	public CardShape judgeFourSingle() {
		if (codeList.size() != 6) {
			return null;
		}
		int card = 0;
		boolean flag = false;
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count == 4) {
				card = entry.getKey();
				flag = true;
				break;
			}
		}
		if (!flag) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(card);
		cardShape.setType(CardShape.TYPE_FOUR_SINGLE);
		return cardShape;
	}
	
	/**
	 * 判断四带两对
	 * @return
	 */
	@JudgerHandler(id="TYPE_FOUR_PAIR", priority=1)
	public CardShape judgeFourPair() {
		if (codeList.size() != 8) {
			return null;
		}
		int card = 0;
		boolean flag = false;
		int pairCount = 0;
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count == 4) {
				card = entry.getKey();
				flag = true;
			}
			if (count == 2) {
				pairCount++;
			}
		}
		if (!flag || pairCount != 2) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(card);
		cardShape.setType(CardShape.TYPE_FOUR_PAIR);
		return cardShape;
	}
	
	/**
	 * 判断炸弹
	 * @return
	 */
	@JudgerHandler(id="TYPE_BOMB", priority=1)
	public CardShape judgeBomb() {
		if (codeList.size() != 4) {
			return null;
		}
		if (countMap.size() != 1) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(codeList.get(0));
		cardShape.setType(CardShape.TYPE_BOMB);
		return cardShape;
	}
	
	/**
	 * 判断王炸
	 * @return
	 */
	@JudgerHandler(id="TYPE_KING_BOMB", priority=1)
	public CardShape judgeKingBomb() {
		if (codeList.size() != 2) {
			return null;
		}
		int carda = codeList.get(0);
		int cardb = codeList.get(1);
		if (carda != 16 || cardb != 17) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(cardb);
		cardShape.setType(CardShape.TYPE_KING_BOMB);
		return cardShape;
	}
	
	/**
	 * 判断鬼牌
	 * @return
	 */
	@JudgerHandler(id="TYPE_GHOST", priority=1)
	public CardShape judgeGhost() {
		if (codeList.size() != 1) {
			return null;
		}
		int card = codeList.get(0);
		if (card != 1) {
			return null;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(card);
		cardShape.setType(CardShape.TYPE_GHOST);
		return cardShape;
	}
	
	/**
	 * 判断连对
	 * @return
	 */
	@JudgerHandler(id="TYPE_CONTINUE_PAIR", priority=1)
	public CardShape judgeContinuePair(){
		if (codeList.size() < 6) {
			return null;
		}
		if(codeList.contains(15)){
			return null;
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		Integer preCard = null;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count != 2) {
				return null;
			}
			Integer card = entry.getKey();
			if (preCard != null && card - preCard != 1) {
				return null;
			}
			preCard = card;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(preCard);
		cardShape.setContinueSize(countMap.size());
		cardShape.setType(CardShape.TYPE_CONTINUE_PAIR);
		return cardShape;
	}
	
	/**
	 * 判断飞机不带
	 * @return
	 */
	@JudgerHandler(id="TYPE_PLANE", priority=1)
	public CardShape judgePlane(){
		if (codeList.size() < 6) {
			return null;
		}
		if(codeList.contains(15)){
			return null;
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		Integer preCard = null;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count != 3) {
				return null;
			}
			Integer card = entry.getKey();
			if (preCard != null && card - preCard != 1) {
				return null;
			}
			preCard = card;
		}
		CardShape cardShape = new CardShape();
		cardShape.setWeightCode(preCard);
		cardShape.setContinueSize(countMap.size());
		cardShape.setType(CardShape.TYPE_PLANE);
		return cardShape;
	}
	
	/**
	 * 判断飞机带单牌
	 * @return
	 */
	@JudgerHandler(id="TYPE_PLANE_SINGLE", priority=1)
	public CardShape judgePlaneSingle() {
		if (codeList.size() < 8) {
			return null;
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		int size = 0;
		int maxContinueSize = 0;
		int continueSize = 0;
		Integer preCard = null;
		Integer weightCard = null;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			size += count;
			Integer card = entry.getKey();
			if (count >= 3 && card != 15) {
				if (null == preCard) {
					continueSize = 1;
				} else if (card - preCard == 1) {
					continueSize++;
				} else {
					continueSize = 1;
				}
			} else {
				continueSize = 0;
			}
			if (continueSize > maxContinueSize) {
				maxContinueSize = continueSize;
				weightCard = card;
			}
			preCard = card;
		}
		for (int i = maxContinueSize; i >= 2; i--) {
			if (i * 4 != size) {
				continue;
			}
			CardShape cardShape = new CardShape();
			cardShape.setType(CardShape.TYPE_PLANE_SINGLE);
			cardShape.setWeightCode(weightCard);
			cardShape.setContinueSize(i);
			return cardShape;
		}
		return null;
	}
	
	/**
	 * 判断飞机带对
	 * @return
	 */
	@JudgerHandler(id="TYPE_PLANE_PAIR", priority=1)
	public CardShape judgePlanPair() {
		if (codeList.size() < 10) {
			return null;
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		int size = 0;
		int maxContinueSize = 0;
		int continueSize = 0;
		Integer preCard = null;
		Integer weightCard = null;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			size += count;
			Integer card = entry.getKey();
			if (count >= 3 && card != 15) {
				if (null == preCard) {
					continueSize = 1;
				} else if (card - preCard == 1) {
					continueSize++;
				} else {
					continueSize = 1;
				}
			} else {
				continueSize = 0;
			}
			if (continueSize > maxContinueSize) {
				maxContinueSize = continueSize;
				weightCard = card;
			}
			preCard = card;
		}
		for (int i = maxContinueSize; i >= 2; i--) {
			if (i * 5 != size) {
				continue;
			}
			if (!isPlanPair(i, weightCard)) {
				continue;
			}
			CardShape cardShape = new CardShape();
			cardShape.setType(CardShape.TYPE_PLANE_PAIR);
			cardShape.setWeightCode(weightCard);
			cardShape.setContinueSize(i);
			return cardShape;
		}
		return null;
	}
	
	private boolean isPlanPair(int continueSize, Integer weightCard) {
		List<Integer> cardList = new ArrayList<>();
		cardList.addAll(codeList);
		for (int i = 0; i < continueSize; i++) {
			Integer card = weightCard - i;
			for (int j = 0; j < 3; j++) {
				cardList.remove(card);
			}
		}
		Map<Integer, Integer> map = new LinkedHashMap<>();
		for (Integer card : cardList) {
			Integer count = map.get(card);
			if (null == count) {
				map.put(card, 1);
			} else {
				map.put(card, count + 1);
			}
		}
		Iterator<Integer> iter = map.values().iterator();
		int pair = 0;
		while(iter.hasNext()) {
			int count = iter.next();
			pair += count / 2;
		}
		return pair == continueSize;
	}
	
	/**
	 * 判断顺子
	 * @return
	 */
	@JudgerHandler(id="TYPE_STRAIGHT", priority=1)
	public CardShape judgeStraight() {
		if (countMap.size() < 5) {
			return null;
		}
		for (int card : codeList) {
			if (card < 3 || card > 14) {
				return null;
			}
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		Integer preCard = null;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count != 1) {
				return null;
			}
			Integer card = entry.getKey();
			if (preCard != null && card - preCard != 1) {
				return null;
			}
			preCard = card;
		}
		CardShape cardShape = new CardShape();
		cardShape.setType(CardShape.TYPE_STRAIGHT);
		cardShape.setWeightCode(preCard);
		cardShape.setContinueSize(countMap.size());
		return cardShape;
	}
	
	@Override
	public List<CardShape> filter(Map<String, CardShape> resultMap) {
		if (resultMap.size() > 1) {
			throw new LogicException("只可能出现一种牌型！");
		}
		return ShapeJudger.super.filter(resultMap);
	}
}
