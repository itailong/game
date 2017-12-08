package com.starland.xyqp.gmback.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Md5Utils {

	private static final Logger LOGGER = LogManager.getLogger(Md5Utils.class);
	
	public static String encoding(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] buf = str.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			digest.update(buf);
			// 获得密文
			byte[] md = digest.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char cbuf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte bts = md[i];
				cbuf[k++] = hexDigits[bts >>> 4 & 0xf];
				cbuf[k++] = hexDigits[bts & 0xf];
			}
			return new String(cbuf);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("", e);
		}
		return null;
	}
	
}
