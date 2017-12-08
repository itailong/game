package com.starland.xyqp.yjzzmj.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.starland.tools.shape.JudgerHandler;
import com.starland.tools.shape.ShapeJudger;
import com.starland.xyqp.yjzzmj.model.CardShape;
import com.starland.xyqp.yjzzmj.model.JudgerParam;
import com.starland.xyqp.yjzzmj.model.PlayWay;

public class WinJudger implements ShapeJudger<JudgerParam, CardShape> {
	
	/**
	 * 小七对
	 */
	public static final String SEVEN_PAIR = "SEVEN_PAIR";
	
	/**
	 * 平胡
	 */
	public static final String COMMON_WIN = "COMMON_WIN";
	
	/**
	 * 海底
	 */
	public static final String SEAFLOOR = "SEAFLOOR";
	
	/**
	 * 杠上花
	 */
	public static final String BRIDGE_WIN = "BRIDGE_WIN";
	
	/**
	 * 抢杠
	 */
	public static final String GRAB_BRIDGE = "GRAB_BRIDGE";
	
	/**
	 * 清一色
	 */
	public static final String UNIFORM_COLOR = "UNIFORM_COLOR";
	
	/**
	 * 门清
	 */
	public static final String DOOR_CLEAN = "DOOR_CLEAN";
	
	/**
	 * 碰碰胡
	 */
	public static final String ALL_BUMP_WIN = "ALL_BUMP_WIN";
	
	/**
	 * 将将胡
	 */
	public static final String ALL_GENERAL_WIN = "ALL_GENERAL_WIN";
	
	/**
	 * 一条龙
	 */
	public static final String ONE_DRAGON = "ONE_DRAGON";
	
	/**
	 * 一字撬
	 */
	public static final String ONE_CARD_PRY = "ONE_CARD_PRY";
	
	/**
	 * 天胡
	 */
	public static final String SKY_WIN = "SKY_WIN";
	
	/**
	 * 听牌
	 */
	public static final String HEAR = "HEAR";

	private JudgerParam judgerParam;
	
	private List<Integer> cards;
	
	private Map<Integer, Integer> countMap;
	
	private boolean canWin;
	
	@Override
	public void init(JudgerParam judgerParam) {
		this.judgerParam = judgerParam;
		cards = new ArrayList<>();
		cards.addAll(judgerParam.getCardList());
		cards.add(judgerParam.getCard());
		cards.sort((o1, o2)-> o1 - o2);
		countMap = new LinkedHashMap<>();
		for (Integer card : cards) {
			Integer count = countMap.get(card);
			if (null == count) {
				countMap.put(card, 1);
			} else {
				countMap.put(card, count + 1);
			}
		}
	}
	
	/**
	 * 判断七小对
	 * @return
	 */
	@JudgerHandler(id=SEVEN_PAIR, priority=1)
	public CardShape judgeSevenPair() {
		if (cards.size() != 14) {
			return null;
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		int luxury = 0;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (count != 2 && count != 4) {
				return null;
			}
			if (count == 4) {
				luxury++;
			}
		}
		CardShape result = new CardShape();
		switch(luxury) {
		case 0:
			result.setCount(1);
			result.setName("七小对");
			break;
		case 1:
			result.setCount(2);
			result.setName("豪华七小对");
			break;
		case 2:
			result.setCount(3);
			result.setName("双豪华七小对");
			break;
		case 3:
			result.setCount(4);
			result.setName("三豪华七小对");
			break;
		default:
			return null;
		}
		canWin = true;
		return result;
	}
	
	/**
	 * 判断将将胡
	 * @return
	 */
	@JudgerHandler(id=ALL_GENERAL_WIN, priority=2)
	public CardShape judgeAllGeneralWin() {
		if (!judgerParam.isTakeSelf() && !isDoorClean()) {
			return null;
		}
		PlayWay playWay = judgerParam.getPlayWay();
		if (!playWay.isMqjjh() && !judgerParam.isTakeSelf()) {
			return null;
		}
		Integer card = judgerParam.getCard();
		List<Integer> list = new ArrayList<>();
		list.add(card);
		list.addAll(judgerParam.getCardList());
		list.addAll(judgerParam.getBumpList());
		list.addAll(judgerParam.getHideBridgeList());
		list.addAll(judgerParam.getPassBridgeList());
		list.addAll(judgerParam.getShowBridgeList());
		for (Integer pai : list) {
			int num = pai % 10;
			if (num != 2 && num != 5 && num != 8) {
				return null;
			}
		}
		CardShape cardShape = new CardShape();
		cardShape.setName("将将胡");
		cardShape.setCount(1);
		canWin = true;
		return cardShape;
	}
	
	/**
	 * 判断平胡
	 * @return
	 */
	@JudgerHandler(id=COMMON_WIN, priority=3)
	public CardShape judgeCommonWin() {
		if (isCommonWin(cards)) {
			CardShape cardShape = new CardShape();
			cardShape.setCount(0);
			cardShape.setName("平胡");
			canWin = true;
			return cardShape;
		}
		return null;
	}
	
	/**
	 * 判断海底
	 * @return
	 */
	@JudgerHandler(id=BRIDGE_WIN, priority=4)
	public CardShape judgeSeafloor() {
		if (!canWin) {
			return null;
		}
		if (judgerParam.isSeafloor()) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("海底捞月");
			return result;
		}
		return null;
	}
	
	/**
	 * 判断杠上开花
	 * @return
	 */
	@JudgerHandler(id=BRIDGE_WIN, priority=5)
	public CardShape judgeBridgeWin() {
		if (!canWin) {
			return null;
		}
		if (judgerParam.isBridgeWin()) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("杠上开花");
			return result;
		}
		return null;
	}
	
	/**
	 * 判断抢杠
	 * @return
	 */
	@JudgerHandler(id=GRAB_BRIDGE, priority=6)
	public CardShape judgeGrabBridge() {
		if (!canWin) {
			return null;
		}
		if (judgerParam.isGrabBridge()) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("抢杠");
			return result;
		}
		return null;
	}
	
	/**
	 * 判断清一色
	 * @return
	 */
	@JudgerHandler(id=UNIFORM_COLOR, priority=7)
	public CardShape judgeUniformColor() {
		if (!canWin) {
			return null;
		}
		Integer card = judgerParam.getCard();
		int color = card / 10;
		List<Integer> list = new ArrayList<>();
		list.addAll(judgerParam.getCardList());
		list.addAll(judgerParam.getBumpList());
		list.addAll(judgerParam.getHideBridgeList());
		list.addAll(judgerParam.getPassBridgeList());
		list.addAll(judgerParam.getShowBridgeList());
		for (Integer pai : list) {
			if (pai / 10 != color) {
				return null;
			}
		}
		CardShape result = new CardShape();
		result.setCount(1);
		result.setName("清一色");
		return result;
	}
	
	/**
	 * 判断门清
	 * @return
	 */
	@JudgerHandler(id=DOOR_CLEAN, priority=8)
	public CardShape judgeDoorClean() {
		if (!canWin) {
			return null;
		}
		PlayWay playWay = judgerParam.getPlayWay();
		if (!playWay.isHasMenqing()) {
			return null;
		}
		if (!isDoorClean()) {
			return null;
		}
		CardShape result = new CardShape();
		result.setCount(1);
		result.setName("门清");
		return result;
	}
	
	private boolean isDoorClean() {
		if (judgerParam.getBumpList().size() != 0) {
			return false;
		}
		if (judgerParam.getPassBridgeList().size() != 0) {
			return false;
		}
		if (judgerParam.getShowBridgeList().size() != 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断碰碰胡
	 * @return
	 */
	@JudgerHandler(id=ALL_BUMP_WIN, priority=9)
	public CardShape judgeAllBumpWin() {
		if (!canWin) {
			return null;
		}
		Iterator<Entry<Integer, Integer>> iter = countMap.entrySet().iterator();
		boolean flag = false;
		while(iter.hasNext()) {
			Entry<Integer, Integer> entry = iter.next();
			int count = entry.getValue();
			if (!flag && count == 2) {
				flag = true;
			} else if (count != 3) {
				return null;
			}
		}
		CardShape result = new CardShape();
		result.setCount(1);
		result.setName("碰碰胡");
		return result;
	}
	
	/**
	 * 判断一条龙
	 * @return
	 */
	@JudgerHandler(id=ONE_DRAGON, priority=10)
	public CardShape judgeOneDragon() {
		if (!canWin) {
			return null;
		}
		Integer preCard = null;
		boolean flag = false;
		int size = 1;
		Iterator<Integer> iter = countMap.keySet().iterator();
		while(iter.hasNext()) {
			Integer card = iter.next();
			if (null != preCard && card - preCard == 1) {
				size++;
			} else {
				size = 1;
			}
			if (size == 9) {
				flag = true;
				break;
			}
			preCard = card;
		}
		if (flag) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("一条龙");
			return result;
		}
		return null;
	}
	
	/**
	 * 判断一字撬
	 * @return
	 */
	@JudgerHandler(id=ONE_CARD_PRY, priority=11)
	public CardShape judgeOneCardPry() {
		if (!canWin) {
			return null;
		}
		if (cards.size() == 2) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("一字撬");
			return result;
		}
		return null;
	}
	
	/**
	 * 判断天胡
	 * @return
	 */
	@JudgerHandler(id=SKY_WIN, priority=12)
	public CardShape judgeSkyWin() {
		if (!canWin) {
			return null;
		}
		if (judgerParam.isSkyWin()) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("天胡");
			return result;
		}
		return null;
	}
	
	/**
	 * 判断听牌
	 * @return
	 */
	@JudgerHandler(id=HEAR, priority=13)
	public CardShape judgeHear() {
		if (!canWin) {
			return null;
		}
		if (judgerParam.isHear()) {
			CardShape result = new CardShape();
			result.setCount(1);
			result.setName("报听");
			return result;
		}
		return null;
	}
	
	/**
	 * 递归的方式判断是否为平胡
	 * @param cards
	 * @return
	 */
	private boolean isCommonWin(List<Integer> cards) {
		if (cards.size() == 2) {
			return cards.get(0).equals(cards.get(1));
		}
		List<List<Integer>> triplet = findTriplet(cards);
		List<List<Integer>> straight = findStraight(cards);
		List<List<Integer>> list = new ArrayList<>();
		list.addAll(triplet);
		list.addAll(straight);
		if (list.isEmpty()) {
			return false;
		}
		for (List<Integer> group : list) {
			List<Integer> subCards = removeList(cards, group);
			if (isCommonWin(subCards)) {
				return true;
			}
		}
		return false;
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
	
	@Override
	public boolean preJudge(Map<String, CardShape> resultMap, String id) {
		// 如果是小七对，则不用判断门清
		if (DOOR_CLEAN.equals(id) && resultMap.containsKey(SEVEN_PAIR)) {
			return false;
		}
		return true;
	}

	@Override
	public List<CardShape> filter(Map<String, CardShape> resultMap) {
		if (resultMap.containsKey(COMMON_WIN) && resultMap.size() > 1) {
			resultMap.remove(COMMON_WIN);
		}
		// 平胡只能自摸
		if (resultMap.containsKey(COMMON_WIN) && resultMap.size() == 1 && !judgerParam.isTakeSelf()) {
			resultMap.remove(COMMON_WIN);
		}
		List<CardShape> list = new ArrayList<>(resultMap.size());
		Iterator<Entry<String, CardShape>> iter = resultMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, CardShape> entry = iter.next();
			CardShape shape = entry.getValue();
			String id = entry.getKey();
			shape.setId(id);
			list.add(shape);
		}
		return list;
	}

}
