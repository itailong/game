package com.starland.xyqp.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {

	public static String get(String url, Map<String, Object> params) throws IOException {
		String paramsStr = encodeParams(params);
		StringBuilder buf = new StringBuilder();
		String urlParams = null;
		if (paramsStr.length() == 0) {
			urlParams = url;
		} else {
			urlParams = url + "?" + paramsStr;
		}
		BufferedReader in = null;
		try {
			URL realUrl = new URL(urlParams);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while((line = in.readLine()) != null) {
				buf.append(line);
			}
		} finally {
			if (null != in) {
				in.close();
			}
		}
		return buf.toString();
	}
	
	public static String post(String url, Map<String, Object> params)  throws IOException  {
		String paramsStr = encodeParams(params);
		StringBuilder buf = new StringBuilder();
		PrintWriter out = null;
        BufferedReader in = null;
        try {
        	URL realUrl = new URL(url);
        	// 打开和URL之间的连接
        	URLConnection conn = realUrl.openConnection();
        	// 设置通用的请求属性
        	conn.setRequestProperty("accept", "*/*");
        	conn.setRequestProperty("connection", "Keep-Alive");
        	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        	conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(paramsStr);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
			while((line = in.readLine()) != null) {
				buf.append(line);
			}
        } finally {
        	if (null != out) {
        		out.close();
        	}
        	if (in != null) {
        		in.close();
        	}
        }
        return buf.toString();
	}
	
	private static String encodeParams(Map<String, Object> params) throws IOException {
		Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
		StringBuilder buf = new StringBuilder();
		while(iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String name = entry.getKey();
			name = URLEncoder.encode(name, "UTF-8");
			String value = entry.getValue().toString();
			value = URLEncoder.encode(value, "UTF-8");
			if (buf.length() > 0) {
				buf.append("&");
			}
			buf.append(name);
			buf.append("=");
			buf.append(value);
		}
		return buf.toString();
	}
	
	
}
