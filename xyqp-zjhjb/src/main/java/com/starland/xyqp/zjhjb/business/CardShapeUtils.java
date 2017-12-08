package com.starland.xyqp.zjhjb.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.starland.xyqp.zjhjb.model.CardShape;

public class CardShapeUtils {

	/**
	 * 获取牌的类型
	 * @return
	 */
	public static CardShape getType(List<Integer> cardList){
		cardList.sort((o1, o2) -> {
			return o1 % 100 - o2 % 100;
		});
		CardShape cardShape = new CardShape();
		//判断是否为特殊牌型
		if(isTeShu(cardList)){
			cardShape.setType(CardShape.TYPE_TESHU);
			cardShape.setCardList(cardList);
			return cardShape;
		}
		//判断是否豹子
		else if(isBaoZi(cardList)){
			cardShape.setType(CardShape.TYPE_BAOZI);
			cardShape.setCardList(cardList);
			return cardShape;
		}
		//判断是否为顺金
		else if(isShunJin(cardList)){
			cardShape.setType(CardShape.TYPE_SHUNJIN);
			cardShape.setCardList(cardList);
			return cardShape;
		}
		// 判断是否为金花
		else if (isTongHua(cardList)) {
			cardShape.setType(CardShape.TYPE_TONGHUA);
			cardShape.setCardList(cardList);
			return cardShape;
		}
		// 判断是否为顺子
		else if(isShunZi(cardList)){
			cardShape.setType(CardShape.TYPE_SHUNZI);
			cardShape.setCardList(cardList);
			return cardShape;
		}
		//判断是否为对子
		else if(isDuiZi(cardList)){
			cardShape.setType(CardShape.TYPE_PAIR);
			cardShape.setCardList(sortPairCards(cardList));
			return cardShape;
		}
		//散牌
		else{
			cardShape.setType(CardShape.TYPE_SCATTER);
			cardShape.setCardList(cardList);
			return cardShape;
		}
	}
	
	
	/**
	 * 将对子牌进行排序 对子在后 单牌在前
	 * @param cardList
	 * @return
	 */
	private static List<Integer> sortPairCards(List<Integer> cardList){
		if(cardList.get(1) % 100 == cardList.get(2) % 100){
			return cardList;
		}else{
			List<Integer> list = new ArrayList<>();
			list.add(cardList.get(2));
			list.add(cardList.get(1));
			list.add(cardList.get(0));
			return list;
		}
	}
	
	/**
	 * 判断是否为对子
	 * @param cardList
	 */
	public static boolean isDuiZi(List<Integer> cardList){
		List<Integer> interceptSize = interceptSize(cardList);
		return new HashSet<Integer>(interceptSize).size() == 2;
	}
	
	/**
	 * 判断是否为顺子
	 * @param cardList
	 * @return
	 */
	public static boolean isShunZi(List<Integer> cardList){
		List<Integer> list = interceptSize(cardList);
		List<Integer> lists = interceptColor(cardList);
		//判断是否花色相同
		if(new HashSet<Integer>(lists).size() == 1){
			return false;
		}
		for(int i = 0 ; i < list.size() - 1 ; i++){
			if(!(list.get(i) + 1 == list.get(i + 1))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否为金花
	 * @param cardList
	 * @return
	 */
	public static boolean isTongHua(List<Integer> cardList){
		List<Integer> list = interceptColor(cardList);
		return new HashSet<Integer>(list).size() == 1;
	}
	
	/**
	 * 判断是否为顺金
	 * @param cardList
	 * @return
	 */
	public static boolean isShunJin(List<Integer> cardList){
		List<Integer> colorList = interceptColor(cardList);
		if(!(new HashSet<Integer>(colorList).size() == 1)){
			return false;
		}
		List<Integer> sizeList = interceptSize(cardList);
		for(int i = 0 ; i < sizeList.size() - 1 ; i++){
			if(!(sizeList.get(i) + 1 == sizeList.get(i + 1))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否为豹子
	 * @param cardList
	 * @return
	 */
	public static boolean isBaoZi(List<Integer> cardList){
		List<Integer> list = interceptSize(cardList);
		return (new HashSet<Integer>(list)).size() == 1;
	}
	
	/**
	 * 判断是否为特殊牌型
	 * @param cardList
	 * @return
	 */
	public static boolean isTeShu(List<Integer> cardList){
		List<Integer> sizeList = interceptSize(cardList);
		List<Integer> cardLists = new ArrayList<>();
		cardLists.add(2);
		cardLists.add(3);
		cardLists.add(5);
		return sizeList.equals(cardLists);
	}
	
	
	/**
	 * 截取牌的大小信息
	 * @param list
	 * @return
	 */
	private static List<Integer> interceptSize(List<Integer> list){
		List<Integer> cardList = new ArrayList<Integer>();
		for(int i = 0 ; i < list.size() ; i++){
			int a = list.get(i) % 100;
			cardList.add(a);
		}
		return cardList;
	}
	
	/**
	 * 截取花色信息
	 * @param list
	 * @return
	 */
	private static List<Integer> interceptColor(List<Integer> list){
		List<Integer> cardList = new ArrayList<Integer>();
		for(int i = 0 ; i < list.size() ; i++){
			int a = list.get(i) / 100;
			cardList.add(a);
		}
		return cardList;
	}
	
}
