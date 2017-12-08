package com.starland.xyqp.ddz.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.starland.tools.shape.ShapeParser;
import com.starland.xyqp.ddz.model.CardShape;
import com.starland.xyqp.ddz.model.HintParam;

public class HintJudgerTest {

	private ShapeParser shapeParser = new ShapeParser();
	
	@Test
	public void judgeSingle() {
		List<Integer> cards = Arrays.asList(105);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		CardShape cardShape = shapes.get(0);
		HintParam hintParam = new HintParam();
		hintParam.setCardShape(cardShape);
		hintParam.setCardList(Arrays.asList(103, 104, 1, 2, 3, 109, 209, 309, 409));
		HintJudger hintJudger = new HintJudger();
		List<List<List<Integer>>> list = shapeParser.parse(hintJudger, hintParam);
		List<List<Integer>> cardList = new ArrayList<>();
		for (List<List<Integer>> li : list) {
			cardList.addAll(li);
		}
		for (List<Integer> list2 : cardList) {
			System.out.println(list2);
		}
	}
	
	@Test
	public void judgeThreePair() {
		List<Integer> cards = Arrays.asList(105, 205, 305, 112, 212);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		CardShape cardShape = shapes.get(0);
		HintParam hintParam = new HintParam();
		hintParam.setCardShape(cardShape);
		hintParam.setCardList(Arrays.asList(103, 104, 1, 2, 3, 109, 209, 309, 409, 210, 310));
		HintJudger hintJudger = new HintJudger();
		List<List<List<Integer>>> list = shapeParser.parse(hintJudger, hintParam);
		List<List<Integer>> cardList = new ArrayList<>();
		for (List<List<Integer>> li : list) {
			cardList.addAll(li);
		}
		for (List<Integer> list2 : cardList) {
			System.out.println(list2);
		}
	}
	
	@Test
	public void judgeContinuePair() {
		List<Integer> cards = Arrays.asList(105, 205, 106, 206, 107, 207);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		CardShape cardShape = shapes.get(0);
		HintParam hintParam = new HintParam();
		hintParam.setCardShape(cardShape);
		hintParam.setCardList(Arrays.asList(103, 104, 1, 2, 3, 109, 209, 309, 409, 210, 310, 211, 411));
		HintJudger hintJudger = new HintJudger();
		List<List<List<Integer>>> list = shapeParser.parse(hintJudger, hintParam);
		List<List<Integer>> cardList = new ArrayList<>();
		for (List<List<Integer>> li : list) {
			cardList.addAll(li);
		}
		for (List<Integer> list2 : cardList) {
			System.out.println(list2);
		}
	}
	
	@Test
	public void judgePlanePair() {
		List<Integer> cards = Arrays.asList(105, 205, 305, 206, 106, 306, 111, 211, 212, 312);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		CardShape cardShape = shapes.get(0);
		HintParam hintParam = new HintParam();
		hintParam.setCardShape(cardShape);
		hintParam.setCardList(Arrays.asList(103, 104, 1, 2, 3, 109, 209, 309, 409, 210, 310, 410, 211, 411, 311));
		HintJudger hintJudger = new HintJudger();
		List<List<List<Integer>>> list = shapeParser.parse(hintJudger, hintParam);
		List<List<Integer>> cardList = new ArrayList<>();
		for (List<List<Integer>> li : list) {
			cardList.addAll(li);
		}
		for (List<Integer> list2 : cardList) {
			System.out.println(list2);
		}
	
	}
	
}
