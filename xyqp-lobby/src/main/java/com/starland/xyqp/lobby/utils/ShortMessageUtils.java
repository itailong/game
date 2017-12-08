package com.starland.xyqp.lobby.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShortMessageUtils {

	
	private static final Logger LOGGER = LogManager.getLogger(ShortMessageUtils.class);
	
	
	/**
	 * 发送短信的工具方法
	 * @param phone_nums 电话号码
	 * @param content 输入的内容
	 * @return
	 */
	public static String send(String phone_num, String content) {
		try {
			if (phone_num == null) {
				LOGGER.error("手机号为空");
				return null;
			}
			String mobile = phone_num;
			StringBuilder buf = new StringBuilder();
			String getURL = ConfigUtils.getProperty("phone.url");
			Map<String, Object> obj = new LinkedHashMap<>();
			obj.put("type", ConfigUtils.getProperty("phone.type"));
			obj.put("apikey", ConfigUtils.getProperty("phone.apikey"));
			obj.put("username", ConfigUtils.getProperty("phone.username"));
			obj.put("password", ConfigUtils.getProperty("phone.password"));
			obj.put("mobile", mobile);
			obj.put("content", URLEncoder.encode(content, "UTF-8"));
			obj.put("encode", ConfigUtils.getProperty("phone.encode"));
			String param = HttpUtils.encodeParam(obj);
			URL getUrl = new URL(getURL + param);
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			try {
				if ((line = in.readLine()) != null) {
					buf.append(line);
				}
			} finally {
				in.close();
				connection.disconnect();
			}
			return buf.toString();
		} catch (Exception e) {
			LOGGER.error("抛出异常");
		}
		return null;
	}
	
	
	/**
	 * 生成四位随机数
	 * @return 返回字符串
	 */
	public static String getCode(){
		StringBuilder buf = new StringBuilder();
		Random rd = new Random();
		for(int i = 0 ; i < 4 ; i++){
			int a = rd.nextInt(10);
			buf.append(a);
		}
		return buf.toString();
	}
	
	
}
