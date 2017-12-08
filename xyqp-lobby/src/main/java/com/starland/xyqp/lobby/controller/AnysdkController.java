package com.starland.xyqp.lobby.controller;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.utils.UUIDUtils;

@Controller
@RequestMapping("anysdk/")
public class AnysdkController {

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * anysdk统一登录地址
	 */
	private String loginCheckUrl = "http://oauth.anysdk.com/api/User/LoginOauth/";

	/**
	 * connect time out
	 * 
	 * @var int
	 */
	private int connectTimeOut = 30 * 1000;

	/**
	 * time out second
	 * 
	 * @var int
	 */
	private int timeOut = 30 * 1000;

	/**
	 * user agent
	 * 
	 * @var string
	 */
	private static final String userAgent = "px v1.0";
	
	@Resource
	private UserService userService;

	@RequestMapping("login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String[]> params = request.getParameterMap();
			// 检测必要参数
			if (parametersIsset(params)) {
				sendToClient(response, "parameter not complete");
				return;
			}

			String queryString = getQueryString(request);

			LOGGER.info(queryString);

			URL url = new URL(loginCheckUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", userAgent);
			conn.setReadTimeout(timeOut);
			conn.setConnectTimeout(connectTimeOut);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(queryString);
			writer.flush();
			tryClose(writer);
			tryClose(os);
			conn.connect();

			InputStream is = conn.getInputStream();

			ObjectNode jsonNode = (ObjectNode) OBJECT_MAPPER.readTree(is);
			
			User user = getUser(jsonNode);
			jsonNode.put("ext", user.getToken());
			
			String result = jsonNode.toString();
			LOGGER.info(result);
			sendToClient(response, result);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendToClient(response, "Unknown error!");
		return;
	}
	
	private User getUser(JsonNode jsonNode) {
		JsonNode dataJson = jsonNode.get("data");
		JsonNode userJson = dataJson.get("user_info");
		String nickname = userJson.get("nickname").asText();
		int sex = userJson.get("sex").asInt();
		String headimgurl = userJson.get("headimgurl").asText();
		String unionId = userJson.get("unionid").asText();
		User user = userService.getByUnionId(unionId);
		if (null == user) {
			user = new User();
			user.setCreateTime(new Date());
			user.setDiamond(3);
			user.setGold(0L);
			user.setUnionId(unionId);
			user.setUserType(2);
			user.setHeadImg(headimgurl);
			user.setName(nickname);
			user.setSex(sex);
			String token = UUIDUtils.getUUID();
			user.setToken(token);
			Date tokenTime = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
			user.setTokenTime(tokenTime);
			userService.add(user);
		} else {
			user.setHeadImg(headimgurl);
			user.setName(nickname);
			user.setSex(sex);
			String token = UUIDUtils.getUUID();
			user.setToken(token);
			Date tokenTime = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
			user.setTokenTime(tokenTime);
			if (user.getUpperId() == null && user.getTempUpperId() != null) {
				// 将临时绑定状态修改为正式绑定状态
				user.setUpperId(user.getTempUpperId());
				user.setTempUpperId(null);
			}
			userService.update(user);
		}
		return user;
	}

	/**
	 * check needed parameters isset 检查必须的参数 channel
	 * uapi_key：渠道提供给应用的app_id或app_key（标识应用的id）
	 * uapi_secret：渠道提供给应用的app_key或app_secret（支付签名使用的密钥）
	 * 
	 * @param params
	 * @return boolean
	 */
	private boolean parametersIsset(Map<String, String[]> params) {
		return !(params.containsKey("channel") && params.containsKey("uapi_key") && params.containsKey("uapi_secret"));
	}

	/**
	 * 获取查询字符串
	 * 
	 * @param request
	 * @return
	 */
	private String getQueryString(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		String queryString = "";
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				queryString += key + "=" + value + "&";
			}
		}
		queryString = queryString.substring(0, queryString.length() - 1);
		return queryString;
	}

	/**
	 * 向客户端应答结果
	 * 
	 * @param response
	 * @param content
	 */
	private void sendToClient(HttpServletResponse response, String content) {
		response.setContentType("text/plain;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @param os
	 */
	private void tryClose(OutputStream os) {
		try {
			if (null != os) {
				os.close();
				os = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭writer
	 * 
	 * @param writer
	 */
	private void tryClose(java.io.Writer writer) {
		try {
			if (null != writer) {
				writer.close();
				writer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
