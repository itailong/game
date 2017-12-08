package com.starland.xyqp.gmback.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeUtils {

	private static final Random RANDOM = new Random();
	
	private static final char[] CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789".toCharArray();
	
	public static String randomCode() {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int index = RANDOM.nextInt(CHARS.length);
			buf.append(CHARS[index]);
		}
		return buf.toString();
	}
	

	public static BufferedImage createImage(String code) {
		int width = 120;
		int height = 40;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		Font font = new Font("楷体", Font.BOLD, 26);
		graphics.setColor(randomColor(100, 255));
		graphics.fillRect(0, 0, width, height);
		
		graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		for (int i = 0; i < 10; i++) {
			int x1 = RANDOM.nextInt(width - 1);
			int y1 = RANDOM.nextInt(height - 1);
			int x2 = RANDOM.nextInt(width - 1);
			int y2 = RANDOM.nextInt(height - 1);
			graphics.setColor(randomColor(100, 200));
			graphics.drawLine(x1, y1, x2, y2);
		}
		
		graphics.setFont(font);
		for (int i = 0; i < code.length(); i++) {
			graphics.setColor(randomColor(50, 150));
			AffineTransform transform = new AffineTransform();
			int x = i * 28 + 10;
			int y = 30;
			double a = 3.14 / 180 * (RANDOM.nextInt(90) - 45);
			transform.rotate(a, x, y);
			graphics.setTransform(transform);
			graphics.drawString(code.substring(i, i + 1), x, y);
		}
		graphics.dispose();
		System.out.println(code);
		return image;
	}
	
	private static Color randomColor(int min, int max) {
		int r = RANDOM.nextInt(max - min) + min;
		int g = RANDOM.nextInt(max - min) + min;
		int b = RANDOM.nextInt(max - min) + min;
		return new Color(r, g, b);
	}
	
}
