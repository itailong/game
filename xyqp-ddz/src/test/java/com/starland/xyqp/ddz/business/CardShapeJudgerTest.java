package com.starland.xyqp.ddz.business;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.starland.tools.shape.ShapeParser;
import com.starland.xyqp.ddz.business.CardShapeJudger;
import com.starland.xyqp.ddz.model.CardShape;

public class CardShapeJudgerTest {

	private ShapeParser shapeParser = new ShapeParser();
	
	@Test
	public void judgeSingle() {
		List<Integer> cards = Arrays.asList(103);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgePair() {
		List<Integer> cards = Arrays.asList(103, 203);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgeStraight() {
		List<Integer> cards = Arrays.asList(103, 204, 305, 106, 107);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgeThreeSingle() {
		List<Integer> cards = Arrays.asList(105, 106, 205, 305);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgeThreePair() {
		List<Integer> cards = Arrays.asList(105, 106, 205, 305, 206);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgeFourSingle() {
		List<Integer> cards = Arrays.asList(105, 106, 205, 305, 405, 207);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgeFourPair() {
		List<Integer> cards = Arrays.asList(105, 205, 305, 405, 107, 207, 309, 309);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
	
	@Test
	public void judgePlaneSingle() {
		List<Integer> cards = Arrays.asList(105, 205, 405, 106, 206, 306, 103, 103, 109, 109);
		CardShapeJudger cardShapeJudger = new CardShapeJudger();
		List<CardShape> shapes = shapeParser.parse(cardShapeJudger, cards);
		System.out.println(shapes);
	}
}
