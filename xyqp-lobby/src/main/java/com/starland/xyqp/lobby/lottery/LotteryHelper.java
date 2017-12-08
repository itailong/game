package com.starland.xyqp.lobby.lottery;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LotteryHelper<T> {

	private List<Element> elements = new LinkedList<>();
	
	private int totalWeight = 0;
	
	private Random random = new Random();
	
	public void add(T target, int weight) {
		elements.add(new Element(target, weight));
		totalWeight += weight;
	}
	
	public T take() {
		int ran = random.nextInt(totalWeight);
		int offset = 0;
		for (Element element : elements) {
			if (ran >= offset && ran < offset + element.weight) {
				return element.target;
			}
			offset += element.weight;
		}
		return null;
	}
	
	private class Element {
		
		private Element(T target, int weight) {
			this.target = target;
			this.weight = weight;
		}

		private T target;
		
		private int weight;
		
	}
	
	public static void main(String[] args) {
		LotteryHelper<Integer> lotteryHelper = new LotteryHelper<>();
		lotteryHelper.add(1, 50);
		lotteryHelper.add(2, 50);
		for (int i = 0; i < 10; i++) {
			Integer result = lotteryHelper.take();
			System.out.println(result);
		}
	}
	
}
