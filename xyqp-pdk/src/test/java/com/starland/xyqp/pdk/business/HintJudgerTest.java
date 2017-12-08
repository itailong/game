package com.starland.xyqp.pdk.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.starland.tools.shape.ShapeParser;
import com.starland.xyqp.pdk.model.CardShape;
import com.starland.xyqp.pdk.model.HintParam;

public class HintJudgerTest {

	@Test
	public void judgeSingle() {
		ShapeParser shapeParser = new ShapeParser();
		HintJudger hintJudger = new HintJudger();
		HintParam data = new HintParam();
		CardShape cardShape = new CardShape();
		cardShape.setType(CardShape.TYPE_SINGLE);
		cardShape.setWeightCode(3);
		data.setCardShape(cardShape);
		data.setCardList(Arrays.asList(103, 104, 105, 205, 106, 108));
		List<List<List<Integer>>> threeList = shapeParser.parse(hintJudger, data);
		List<List<Integer>> twoList = new ArrayList<>();
		for (List<List<Integer>> list : threeList) {
			twoList.addAll(list);
		}
		System.out.println(twoList);
	}
	
	@Test
	public void judgePair() {
		ShapeParser shapeParser = new ShapeParser();
		HintJudger hintJudger = new HintJudger();
		HintParam data = new HintParam();
		CardShape cardShape = new CardShape();
		cardShape.setType(CardShape.TYPE_PAIR);
		cardShape.setWeightCode(3);
		data.setCardShape(cardShape);
		data.setCardList(Arrays.asList(103, 104, 105, 205, 106, 108, 108, 108, 109));
		List<List<List<Integer>>> threeList = shapeParser.parse(hintJudger, data);
		List<List<Integer>> twoList = new ArrayList<>();
		for (List<List<Integer>> list : threeList) {
			twoList.addAll(list);
		}
		System.out.println(twoList);
	}
	
	@Test
	public void judgeThree() {
		ShapeParser shapeParser = new ShapeParser();
		HintJudger hintJudger = new HintJudger();
		HintParam data = new HintParam();
		CardShape cardShape = new CardShape();
		cardShape.setType(CardShape.TYPE_THREE);
		cardShape.setWeightCode(4);
		data.setCardShape(cardShape);
		data.setCardList(Arrays.asList(103, 103, 103, 104, 105, 205, 106, 206, 306, 108, 108, 108, 109));
//		data.setCardList(Arrays.asList(103, 203, 303, 104, 105, 205, 106, 108, 108, 108, 109));
		data.setCardList(Arrays.asList(108, 108, 108, 109, 110));
//		data.setCardList(Arrays.asList(108, 108, 108, 109));
//		data.setCardList(Arrays.asList(108, 108, 108));
		List<List<List<Integer>>> threeList = shapeParser.parse(hintJudger, data);
		List<List<Integer>> twoList = new ArrayList<>();
		for (List<List<Integer>> list : threeList) {
			twoList.addAll(list);
		}
		for (List<Integer> list : twoList) {
			System.out.println(list);
		}
	}
	
	@Test
	public void judgeContinuePair() {
		ShapeParser shapeParser = new ShapeParser();
		HintJudger hintJudger = new HintJudger();
		HintParam data = new HintParam();
		CardShape cardShape = new CardShape();
		cardShape.setType(CardShape.TYPE_CONTINUE_PAIR);
		cardShape.setWeightCode(4);
		cardShape.setContinueSize(2);
		data.setCardShape(cardShape);
		data.setCardList(Arrays.asList(103, 203, 303, 104, 204, 105, 205, 106, 206, 107, 207, 307, 108, 208, 109, 209, 309));
		List<List<List<Integer>>> threeList = shapeParser.parse(hintJudger, data);
		List<List<Integer>> twoList = new ArrayList<>();
		for (List<List<Integer>> list : threeList) {
			twoList.addAll(list);
		}
		for (List<Integer> list : twoList) {
			System.out.println(list);
		}
	}
	
	@Test
	public void judgePlane() {
		ShapeParser shapeParser = new ShapeParser();
		HintJudger hintJudger = new HintJudger();
		HintParam data = new HintParam();
		CardShape cardShape = new CardShape();
		cardShape.setType(CardShape.TYPE_PLANE);
		cardShape.setWeightCode(4);
		cardShape.setContinueSize(2);
		data.setCardShape(cardShape);
		data.setCardList(Arrays.asList(103, 203, 303, 104, 204, 105, 205, 305, 106, 206, 306, 107, 207, 307, 108, 208, 109, 209, 309));
		
//		data.setCardList(Arrays.asList(105, 205, 305, 106, 206, 306, 309));
//		
//		data.setCardList(Arrays.asList(105, 205, 305, 106, 206, 306));
		List<List<List<Integer>>> threeList = shapeParser.parse(hintJudger, data);
		List<List<Integer>> twoList = new ArrayList<>();
		for (List<List<Integer>> list : threeList) {
			twoList.addAll(list);
		}
		for (List<Integer> list : twoList) {
			System.out.println(list);
		}
	}
}
