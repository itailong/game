package com.starland.xyqp.niuniujb.business;

import java.util.ArrayList;
import java.util.List;

import com.starland.tools.shape.match.CardMatcher;
import com.starland.tools.shape.match.CardPattern;
import com.starland.tools.shape.match.MatchRule;
import com.starland.xyqp.niuniujb.model.CardShape;

public class CardShapeUtils {

	/**
	 * 获取牌的类型的对象
	 * @param elementList
	 * @return
	 */
	public static CardShape getCardShape(List<Integer> cardLists) {
		List<Integer> cardList = new ArrayList<>();
		cardList.addAll(cardLists);
		List<List<Integer>> elementList = getListListCard(cardList);
		CardShape cardShape = new CardShape();
		if(elementList.size() == 0){
			cardShape.setCardList(cardList);
			cardShape.setType(CardShape.TYPE_WUNIU);
			cardShape.setMultiple(1);
			cardShape.setMaxCard(getMaxCard(cardList));
			return cardShape;
		}
		List<Integer> bigSizeList = getBigSizeList(elementList);
		cardShape = getType(bigSizeList, cardShape);
		cardShape.setMaxCard(getMaxCard(cardList));
		List<Integer> replaceList = replaceCard(bigSizeList, cardList);
		cardShape.setCardList(replaceList);
		return cardShape;
	}
	
	
	public static List<Integer> interceptCard(List<Integer> cardList){
		List<Integer> codeList = new ArrayList<>();
		for(int i = 0 ; i < cardList.size() ; i++){
			codeList.add(getCardCode(cardList.get(i)));
		}
		return codeList;
	}
	
	private static int getCardCode(Integer card){
		if(card % 100 == 11 || card % 100 == 12 || card % 100 == 13){
			return 10;
		}else{
			return card % 100;
		}
	}
	
	public static List<List<Integer>> getListListCard(List<Integer> cardList){
		List<Integer> interceptCard = interceptCard(cardList);
		CardPattern<Integer> cardPattern = new CardPattern<>();
		MatchRule<Integer> matchRule = new MatchRule<>(3);
		matchRule.setMatchMethod((cards, node) -> {
			Integer carda = cards.get(0);
			Integer cardb = cards.get(1);
			Integer cardc = cards.get(2);
			if((carda + cardb + cardc) % 10 == 0){
				return true;
			}
			return false;
		});
		cardPattern.addRule(matchRule);
		matchRule = new MatchRule<>(2);
		cardPattern.addRule(matchRule);
		CardMatcher<Integer> matcher = cardPattern.matcher(interceptCard);
		List<List<Integer>> elementList = new ArrayList<>();
		while(matcher.find()){
			elementList.add(matcher.cardList());
		}
		return elementList;
	}
	
	/**
	 * 筛选出最大的牌型
	 */
	public static List<Integer> getBigSizeList(List<List<Integer>> elementList){
		int count = 0;
		for(int i = 0 ; i < elementList.size() ; i++){
			if((elementList.get(i).get(4) + elementList.get(i).get(3)) % 10 == 0){
				return elementList.get(i);
			}
			if((elementList.get(i).get(4) + elementList.get(i).get(3)) % 10 > count){
				count = (elementList.get(i).get(4) + elementList.get(i).get(3)) % 10;
			}
		}
		for(int i = 0 ; i < elementList.size() ; i++){
			if((elementList.get(i).get(4) + elementList.get(i).get(3)) % 10 == count){
				return elementList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * 将牌j q k 进行替换
	 */
	public static List<Integer> replaceCard(List<Integer> codeList, List<Integer> cardList){
		List<Integer> list = new ArrayList<>();
		for(int i = 0 ; i < cardList.size() ; i++){
			if(cardList.get(i) % 100 == codeList.get(4) || cardList.get(i) % 100 == codeList.get(3)){
				if(codeList.get(4) == codeList.get(3)){
					list.add(cardList.get(i));
				}else{
					if(list.isEmpty()){
						list.add(cardList.get(i));
					}else{
						if(list.get(0) % 100 != cardList.get(i) % 100){
							list.add(cardList.get(i));
						}
					}
				}
			}
			if(list.size() == 2){
				break;
			}
		}
		if(list.size() != 2){
			for(int i = 0 ; i < cardList.size() ; i++){
				if(cardList.get(i) % 100 == 11 || cardList.get(i) % 100 == 12 || cardList.get(i) % 100 == 13){
					list.add(cardList.get(i));
					if(list.size() == 2){
						break;
					}
				}
			}
		}
		cardList.removeAll(list);
		cardList.addAll(list);
		return cardList;
	} 
	
	
	
 	
	/**
	 * 获取牌的类型
	 * @param codeList
	 * @return
	 */
	public static CardShape getType(List<Integer> codeList, CardShape cardShape){
		int size = codeList.get(4) + codeList.get(3);
		switch (size % 10) {
		case 1:
			cardShape.setMultiple(1);
			cardShape.setType(CardShape.TYPE_NIUYI);
			return cardShape;
		case 2:
			cardShape.setMultiple(1);
			cardShape.setType(CardShape.TYPE_NIUER);
			return cardShape;
		case 3:
			cardShape.setMultiple(1);
			cardShape.setType(CardShape.TYPE_NIUSAN);
			return cardShape;
		case 4:
			cardShape.setMultiple(1);
			cardShape.setType(CardShape.TYPE_NIUSI);
			return cardShape;
		case 5:
			cardShape.setMultiple(1);
			cardShape.setType(CardShape.TYPE_NIUWU);
			return cardShape;
		case 6:
			cardShape.setMultiple(1);
			cardShape.setType(CardShape.TYPE_NIULIU);
			return cardShape;
		case 7:
			cardShape.setMultiple(2);
			cardShape.setType(CardShape.TYPE_NIUQI);
			return cardShape;
		case 8:
			cardShape.setMultiple(2);
			cardShape.setType(CardShape.TYPE_NIUBA);
			return cardShape;
		case 9:
			cardShape.setMultiple(3);
			cardShape.setType(CardShape.TYPE_NIUJIU);
			return cardShape;
		default:
			cardShape.setMultiple(4);
			cardShape.setType(CardShape.TYPE_NIUNIU);
			return cardShape;
		}
	}
	
	public static int getMaxCard(List<Integer> cardList){
		int count = 0;
		for(int i = 0 ; i < cardList.size() ; i++){
			if(cardList.get(i) % 100 > count){
				count = cardList.get(i) % 100;
			}
		}
		for(int i = 0 ; i < cardList.size() ; i++){
			if(cardList.get(i) % 100 == count){
				return cardList.get(i);
			}
		}
		return 0;
	}
	
}
