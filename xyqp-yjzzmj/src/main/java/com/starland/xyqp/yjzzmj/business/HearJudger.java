package com.starland.xyqp.yjzzmj.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.starland.tools.shape.JudgerHandler;
import com.starland.tools.shape.ShapeJudger;

public class HearJudger implements ShapeJudger<List<Integer>, List<Integer>> {

	private List<Integer> cards;
	
	private Map<Integer, Integer> countMap;
	
	@Override
	public void init(List<Integer> data) {
		cards = new ArrayList<>();
		cards.addAll(data);
		cards.sort((o1, o2) -> o1 - o2);
		countMap = new HashMap<>();
		for (Integer card : cards) {
			Integer count = countMap.get(card);
			if (null == count) {
				countMap.put(card, 1);
			} else {
				countMap.put(card, count + 1);
			}
		}
	}

	@JudgerHandler(id="seven_pair", priority=1)
	public List<Integer> judgeSevenPair() {
		if (cards.size() != 13) {
			return null;
		}
		List<Integer> list = new LinkedList<>();
		list.addAll(cards);
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			Integer card = entry.getKey();
			int count = entry.getValue();
			if (count < 2) {
				continue;
			}
			int length = count / 2 * 2;
			for (int i = 0; i < length; i++) {
				list.remove(card);
			}
		}
		if (list.size() == 1) {
			return list;
		}
		return null;
	}
	
	@JudgerHandler(id="all_general_win", priority=2)
	public List<Integer> judgeAllGeneralWin() {
		for (Integer card : cards) {
			int num = card % 10;
			if (num != 2 && num != 5 && num != 8) {
				return null;
			}
		}
		return Arrays.asList(2, 5, 8, 12, 15, 18, 22, 25, 28);
	}
	
	@JudgerHandler(id="common_win", priority=3)
	public List<Integer> judgeCommonWin() {
		List<Integer> cardList = new LinkedList<>();
		cardList.addAll(cards);
		Set<Integer> result = new HashSet<>();
		doJudgeCommonWin(cardList, result);
		if (!result.isEmpty()) {
			List<Integer> list = new ArrayList<>();
			list.addAll(result);
			return list;
		}
		return null;
	}
	
	private void doJudgeCommonWin(List<Integer> cardList, Set<Integer> result) {
		if (cardList.size() == 1) {
			result.add(cardList.get(0));
		}
		List<List<Integer>> triplet = findTriplet(cardList);
		List<List<Integer>> straight = findStraight(cardList);
		List<List<Integer>> list = new ArrayList<>();
		list.addAll(triplet);
		list.addAll(straight);
		if (list.isEmpty()) {
			if (cardList.size() == 4) {
				findFourHear(cardList, result);
			}
			return;
		}
		for (List<Integer> group : list) {
			List<Integer> subCards = removeList(cardList, group);
			doJudgeCommonWin(subCards, result);
		}
	}
	
	private void findFourHear(List<Integer> cardList, Set<Integer> result) {
		Map<Integer, Integer> map = new LinkedHashMap<>();
		for (Integer card : cardList) {
			Integer count = map.get(card);
			if (null == count) {
				map.put(card, 1);
			} else {
				map.put(card, count + 1);
			}
		}
		Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			Integer card = entry.getKey();
			int count = entry.getValue();
			if (count != 2) {
				continue;
			}
			List<Integer> list = new LinkedList<>();
			list.addAll(cardList);
			list.remove(card);
			list.remove(card);
			findTwoHear(list, result);
		}
	}
	
	private void findTwoHear(List<Integer> cardList, Set<Integer> result) {
		Integer carda = cardList.get(0);
		Integer cardb = cardList.get(1);
		if (carda.equals(cardb)) {
			result.add(carda);
		}
		if (cardb - carda == 1) {
			if (carda % 10 != 1) {
				result.add(carda - 1);
			}
			if (cardb % 10 != 9) {
				result.add(cardb + 1);
			}
		}
		if (cardb - carda == 2 && cardb % 10 != 1) {
			result.add(carda + 1);
		}
	}
	
	/**
	 * 查找所有的刻子
	 * @param cards
	 * @return
	 */
	private List<List<Integer>> findTriplet(List<Integer> cards) {
		List<List<Integer>> result = new ArrayList<>();
		Map<Integer, Integer> map = new HashMap<>();
		for (Integer card : cards) {
			Integer count = map.get(card);
			if (null == count) {
				map.put(card, 1);
			} else {
				map.put(card, count + 1);
			}
		}
		Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			Integer card = entry.getKey();
			int count = entry.getValue();
			if (count >= 3) {
				List<Integer> list = new ArrayList<>(3);
				list.add(card);
				list.add(card);
				list.add(card);
				result.add(list);
			}
		}
		return result;
	}
	
	/**
	 * 查找所有的顺子
	 * @param cards 必须是提前排好序的牌
	 * @return
	 */
	private List<List<Integer>> findStraight(List<Integer> cards) {
		List<List<Integer>> result = new ArrayList<>();
		Set<Integer> set = new LinkedHashSet<>();
		set.addAll(cards);
		List<Integer> list = new ArrayList<>();
		list.addAll(set);
		for (int i = 0; i < list.size() - 2; i++) {
			Integer carda = list.get(i);
			Integer cardb = list.get(i + 1);
			Integer cardc = list.get(i + 2);
			if (cardb - carda == 1 && cardc - cardb == 1) {
				List<Integer> pais = new ArrayList<>(3);
				pais.add(carda);
				pais.add(cardb);
				pais.add(cardc);
				result.add(pais);
			}
		}
		return result;
	}
	
	/**
	 * 从一个集合中移除另一个集合中的元素，并返回一个新集合
	 * @param cards
	 * @param list
	 * @return
	 */
	private List<Integer> removeList(List<Integer> cards, List<Integer> list) {
		List<Integer> result = new ArrayList<>(cards.size());
		result.addAll(cards);
		for (Integer card : list) {
			result.remove(card);
		}
		return result;
	}
	
}
