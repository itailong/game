package com.starland.xyqp.lobby.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
    /**
     * 此方法为通过请求微信接口返回授权信息、get请求
     * @param url  请求微信接口的url
     * @param param 封装请求接微信接口的参数
     * @return 封装了微信接口返回的字符串
     * @throws Exception
     */
	public static String get(String url, Map<String, Object> param) throws Exception{
		String paramStr = encodeParam(param);
		StringBuilder buf = new StringBuilder();
		String paramUrl = null;
		if(null == paramStr){
			paramUrl = url;
		}else{
			paramUrl = url+"?"+paramStr;
		}
		BufferedReader in = null;
		try{
			URL realUrl = new URL(paramUrl);
			URLConnection conn = realUrl.openConnection();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			if((line = in.readLine()) != null){
				buf.append(line);
			}
		}finally{
			if(null != in){
				in.close();
			}
		}
		return buf.toString();
	}
	
	 /**
     * 此方法为通过请求微信接口返回授权信息、post请求
     * @param url  请求微信接口的url
     * @param param 封装请求接微信接口的参数
     * @return 封装了微信接口返回的字符串
     * @throws Exception
     */
	public static String post(String url, Map<String,Object> param) throws Exception{
		String paramStr = encodeParam(param);
		StringBuilder buf = new StringBuilder();
		PrintWriter out = null;
		BufferedReader in = null;
		try{
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
        	conn.setRequestProperty("connection", "Keep-Alive");
        	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        	conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(paramStr);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));	
            String line  = null;
            while((line = in.readLine()) != null){
                 buf.append(line);            	
            }
		}finally{
			if(null != out){
				out.close();
			}
			if(null != in){
				in.close();
			}
		}
		return buf.toString();
	}
	
	/**
	 * 该方法的作用为将封装好的map集合转换成请求url格式的字符串
	 * @param 参数为装有请求微信接口参数的map集合
	 * @return 返回一个字符串
	 * @throws Exception
	 */
	public static String encodeParam(Map<String,Object> params) throws Exception{
		StringBuilder buf = new StringBuilder();
        Iterator<Entry<String, Object>> iter = params.entrySet().iterator();		
        while(iter.hasNext()){
        	Entry<String, Object> next = iter.next();
        	String name = next.getKey();
        	name = URLEncoder.encode(name,"utf-8");
            String value = next.getValue().toString();        	
        	value = URLEncoder.encode(value,"utf-8");
        	if(buf.length()>0){
        		buf.append("&");
        	}
        	buf.append(name);
        	buf.append("=");
        	buf.append(value);
        	
        }
		return buf.toString();
	}
	
	/**
	 * 获取客户端的ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	
}
