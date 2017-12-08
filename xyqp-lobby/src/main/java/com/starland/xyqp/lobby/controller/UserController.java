package com.starland.xyqp.lobby.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starland.xyqp.lobby.domain.Agent;
import com.starland.xyqp.lobby.domain.GameRoom;
import com.starland.xyqp.lobby.domain.GameServer;
import com.starland.xyqp.lobby.domain.Turntable;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.domain.Verification;
import com.starland.xyqp.lobby.service.AgentService;
import com.starland.xyqp.lobby.service.GameRoomService;
import com.starland.xyqp.lobby.service.GameServerService;
import com.starland.xyqp.lobby.service.TurntableService;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.service.VerificationService;
import com.starland.xyqp.lobby.utils.ConfigUtils;
import com.starland.xyqp.lobby.utils.HttpUtils;
import com.starland.xyqp.lobby.utils.ShortMessageUtils;
import com.starland.xyqp.lobby.utils.UUIDUtils;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Resource
	private UserService userService;
	
	@Resource
	private AgentService agentService;
	
	@Resource
	private VerificationService verificationService;
	
	@Resource 
	private TurntableService turntableService;
	
	@Resource
	private GameRoomService gameRoomService;
	
	@Resource
	private GameServerService gameServerService;
	
	/**
	 * 登录的controller
	 * 微信登录
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(@RequestParam("code") String code , HttpServletRequest request) {
		try {
			User user = getUserByWeixinCode(code);
			if (null == user) {
				return new AjaxResponse(500, "登录失败");
			}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("id",user.getId());
			result.put("name", user.getName());
			result.put("sex", user.getSex());
			result.put("headImg", user.getHeadImg());
			result.put("diamond", user.getDiamond());
			result.put("gold", user.getGold());
			result.put("token", user.getToken());
			result.put("userType", user.getUserType());
			if(null == user.getIntegral()){
				result.put("integral", 0);
			}else{
				result.put("integral", user.getIntegral());
			}
			result.put("userPhone", user.getUserPhone());
			result.put("invitationCode", user.getUpperId());
			String ipAddr = HttpUtils.getIpAddr(request);
			result.put("ip", ipAddr);
			result.put("voiceToken", ConfigUtils.getProperty("voice.token"));
			String roomId = user.getRoomId();
			if (null != roomId && !"".equals(roomId)) {
				GameRoom gameRoom = gameRoomService.get(roomId);
				if (null != gameRoom) {
					Integer serverId = gameRoom.getServerId();
					GameServer gameServer = gameServerService.get(serverId);
					result.put("gameServer", gameServer);
					result.put("roomId", roomId);
				}
			}
			
			Turntable turntable = turntableService.getByUserId(user.getId());
			if(null == turntable){
				result.put("share", 0);
				result.put("lottery", 0);
				result.put("sparrow", 0);
				result.put("sparrowShare", 0);
				return new AjaxResponse(result);
			}
			if(null == turntable.getShare()){
				result.put("share", 0);
			}else{
				result.put("share", turntable.getShare());
			}
			if(null == turntable.getLottery()){
				result.put("lottery", 0);
			}else{
				result.put("lottery", turntable.getLottery());
			}
			if(null == turntable.getSparrowNum()){
				result.put("sparrow", 0);
			}else{
				result.put("sparrow", turntable.getSparrowNum());
			}
			if(null == turntable.getSparrowShare()){
				result.put("sparrowShare", 0);
			}else{
				result.put("sparrowShare", turntable.getSparrowShare());
			}
			return new AjaxResponse(result);
		} catch (Exception e) {
			LOGGER.error("出现异常", e);
		}
		return null;
	}
	
	private User getUserByWeixinCode(String code) {
		String httpResult;
		try {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("appid", ConfigUtils.getProperty("weixin.appid"));
			map.put("secret", ConfigUtils.getProperty("weixin.secret"));
			map.put("code", code);
			map.put("grant_type", "authorization_code");
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
			httpResult = HttpUtils.get(url, map);
			JsonNode jsonNode = OBJECT_MAPPER.readTree(httpResult);
			if (jsonNode.has("errcode")) {
				LOGGER.error("请求接口失败" + httpResult);
				return null;
			}
			String openId = jsonNode.get("openid").asText();
			String accessToken = jsonNode.get("access_token").asText();
			
			url = "https://api.weixin.qq.com/sns/userinfo";
			map = new LinkedHashMap<>();
			map.put("access_token", accessToken);
			map.put("openid", openId);
			
			httpResult = HttpUtils.get(url, map);
			jsonNode = OBJECT_MAPPER.readTree(httpResult);
			if (jsonNode.has("errcode")) {
				LOGGER.error("获取用户信息失败" + httpResult);
				return null;
			}
			String nickname = jsonNode.get("nickname").asText();
			int sex = jsonNode.get("sex").asInt();
			String headimgurl = jsonNode.get("headimgurl").asText();
			String unionId = jsonNode.get("unionid").asText();
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
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * 游客登录 返回 user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tourists", method = RequestMethod.POST)
	public Object tourists(HttpServletRequest request) {
		User user = createTouristUser(request);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("id",user.getId());
		result.put("name", user.getName());
		result.put("sex", user.getSex());
		result.put("headImg", user.getHeadImg());
		result.put("diamond", user.getDiamond());
		result.put("gold", user.getGold());
		result.put("token", user.getToken());
		result.put("userType", user.getUserType());
		if(null == user.getIntegral()){
			result.put("integral", 0);
		}else{
			result.put("integral", user.getIntegral());
		}
		String ipAddr = HttpUtils.getIpAddr(request);
		result.put("ip", ipAddr);
		result.put("voiceToken", ConfigUtils.getProperty("voice.token"));
		String roomId = user.getRoomId();
		result.put("invitationCode", user.getUpperId());
		if (null != roomId && !"".equals(roomId)) {
			GameRoom gameRoom = gameRoomService.get(roomId);
			if (null != gameRoom) {
				Integer serverId = gameRoom.getServerId();
				GameServer gameServer = gameServerService.get(serverId);
				result.put("gameServer", gameServer);
				result.put("roomId", roomId);
			}
		}
		
		Turntable turntable = turntableService.getByUserId(user.getId());
		if(null == turntable){
			result.put("share", 0);
			result.put("lottery", 0);
			result.put("sparrow", 0);
			result.put("sparrowShare", 0);
			return new AjaxResponse(result);
		}
		if(null == turntable.getShare()){
			result.put("share", 0);
		}
		result.put("share", turntable.getShare());
		if(null == turntable.getLottery()){
			result.put("lottery", 0);
		}
		result.put("lottery", turntable.getLottery());
		if(null == turntable.getSparrowNum()){
			result.put("sparrow", 0);
		}
		result.put("sparrow", turntable.getSparrowNum());
		if(null == turntable.getSparrowShare()){
			result.put("sparrowShare", 0);
		}
		result.put("sparrowShare", turntable.getSparrowShare());
		return new AjaxResponse(result);
	}

	/**
	 * 创建游客
	 * @return
	 */
	private User createTouristUser(HttpServletRequest request) {
		User user = new User();
		user.setCreateTime(new Date());
		user.setDiamond(3);
		user.setGold(0L);
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		buf.append("游客");
		for (int i = 0; i < 3; i++) {
			buf.append(random.nextInt(10));
		}
		user.setName(buf.toString());
		user.setSex(1);
		user.setUserType(1);
		String token = UUIDUtils.getUUID();
		user.setToken(token);
		// 有效时间7天
		Date tokenTime = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
		user.setTokenTime(tokenTime);
		userService.add(user);
		return user;
	}

	/**
	 * token 登录
	 * 
	 * @param token
	 * @return user对象
	 */
	@ResponseBody
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public Object token(@RequestParam("token") String token, HttpServletRequest request) {
		if (null == token || "" == token) {
			LOGGER.error("token有误");
			return new AjaxResponse(500, "错误的token");
		}
		User user = userService.getByToken(token);
		if (null == user) {
			LOGGER.error("token为无效");
			return new AjaxResponse(500, "token为无效");
		}
		if (user.getTokenTime().getTime() < System.currentTimeMillis()) {
			LOGGER.error("token已经过期");
			return new AjaxResponse(500, "token过期");
		}
//		String uuid = UUIDUtils.getUUID();
//		user.setToken(uuid);
		if (user.getUpperId() == null && user.getTempUpperId() != null) {
			// 将临时绑定状态修改为正式绑定状态
			user.setUpperId(user.getTempUpperId());
			user.setTempUpperId(null);
		}
//		userService.update(user);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("id",user.getId());
		result.put("name", user.getName());
		result.put("sex", user.getSex());
		result.put("headImg", user.getHeadImg());
		result.put("diamond", user.getDiamond());
		result.put("gold", user.getGold());
		result.put("token", user.getToken());
		result.put("userType", user.getUserType());
		result.put("invitationCode", user.getUpperId());
		if(null == user.getIntegral()){
			result.put("integral", 0);
		}else{
			result.put("integral", user.getIntegral());
		}
		String ipAddr = HttpUtils.getIpAddr(request);
		result.put("ip", ipAddr);
		result.put("voiceToken", ConfigUtils.getProperty("voice.token"));
		String roomId = user.getRoomId();
		if (null != roomId && !"".equals(roomId)) {
			GameRoom gameRoom = gameRoomService.get(roomId);
			if (null != gameRoom) {
				Integer serverId = gameRoom.getServerId();
				GameServer gameServer = gameServerService.get(serverId);
				result.put("gameServer", gameServer);
				result.put("roomId", roomId);
			}
		}
		
		Turntable turntable = turntableService.getByUserId(user.getId());
		if(null == turntable){
			result.put("share", 0);
			result.put("lottery", 0);
			result.put("sparrow", 0);
			result.put("sparrowShare", 0);
			return new AjaxResponse(result);
		}
		if(null == turntable.getShare()){
			result.put("share", 0);
		}
		result.put("share", turntable.getShare());
		if(null == turntable.getLottery()){
			result.put("lottery", 0);
		}
		result.put("lottery", turntable.getLottery());
		if(null == turntable.getSparrowNum()){
			result.put("sparrow", 0);
		}
		result.put("sparrow", turntable.getSparrowNum());
		if(null == turntable.getSparrowShare()){
			result.put("sparrowShare", 0);
		}
		result.put("sparrowShare", turntable.getSparrowShare());
		return new AjaxResponse(result);
	}

	/**
	 * 绑定邀请码
	 * 
	 * @param id
	 *            用户id
	 * @param code
	 *            邀请码
	 * @return 状态码
	 */
	@ResponseBody
	@RequestMapping(value = "/bind", method = RequestMethod.POST)
	public Object bind(@RequestParam("token") String token, @RequestParam("invitationCode") Integer invitationCode) {
		if (null == token || "" == token) {
			LOGGER.error("token有误");
			return new AjaxResponse(500, "错误的token");
		}
		User user = userService.getByToken(token);
		if (null == user) {
			LOGGER.error("用户信息获取失败");
			return new AjaxResponse(500, "绑定失败");
		}
		if ((user.getTokenTime().getTime()) < System.currentTimeMillis()) {
			LOGGER.error("token已过期");
			return new AjaxResponse(500, "token已过期");
		}
		Integer code = user.getUpperId();
		if (code != null) {
			LOGGER.error("已经绑定过");
			return new AjaxResponse(500, "已经绑定过");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Agent agent = agentService.get(invitationCode);
		if (null == agent) {
			return new AjaxResponse(500, "邀请码不正确！");
		}
		user.setUpperId(invitationCode);
//		user.setDiamond(user.getDiamond() + 4);
		userService.update(user);
		map.put("diamond", user.getDiamond());
		map.put("invitationCode", user.getUpperId());
		return new AjaxResponse(map);
	}

	/**
	 * 解绑邀请码
	 * 
	 * @param token
	 *              用户token
	 * @return 状态码
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	@ResponseBody
	@RequestMapping(value = "/unbind", method = RequestMethod.POST)
	public Object unband(@RequestParam("token") String token) throws JsonProcessingException, IOException {
		if(null == token || "" == token){
			LOGGER.error("传入的token错误");
			return new AjaxResponse(500,"传入token错误");
		}
		User user = userService.getByToken(token);
		if (user == null) {
			return new AjaxResponse(500, "用户不存在");
		}
		return new AjaxResponse(500, "不能解绑！");
	}

	/**
	 * 获取短信验证码
	 * @param token 登录所需的
	 * @param phone 电话号码
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	@ResponseBody
	@RequestMapping(value = "/phone", method = RequestMethod.POST)
	public Object phone(@RequestParam("token") String token,@RequestParam("phone") String phone) throws Exception{
		if(null == token || "" == token){
			LOGGER.error("token无效");
			return new AjaxResponse(501,"token为空");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取用户信息失败");
			return new AjaxResponse(502,"获取用户信息失败");
		}
		if(user.getTokenTime().getTime() < System.currentTimeMillis()){
			LOGGER.error("token已过期");
			return new AjaxResponse(503,"token已过期");
		}
		if(null != user.getUserPhone()){
			LOGGER.error("已绑定过手机号");
			return new AjaxResponse(504,"已绑定过手机号码");
		}
		//获取验证码
		String verification = ShortMessageUtils.getCode();
		//发送验证码
		String send = ShortMessageUtils.send(phone,"您的短信验证码为:"+verification);
		if(null == send){
			LOGGER.error("发送验证码错误");
			return new AjaxResponse(505,"发送验证码错误");
		}
		String[] split = send.split(":");
		if(split[0].equals("error")){
			LOGGER.error("获取验证码失败");
			return new AjaxResponse(506,"获取短信验证码失败");
		}
		Verification cation = verificationService.get(phone);
		if(null == cation){
			Verification cations = new Verification();
			cations.setPhone(phone);
			cations.setVerification(verification);
			verificationService.add(cations);
			return new AjaxResponse(200,"获取验证码成功");
		}
		cation.setVerification(verification);
		verificationService.update(cation);
		return new AjaxResponse(200,"获取验证码成功");
	}
	/**
	 * 绑定手机的方法 
	 * @param token 用户登录的token
	 * @param phone 用户手机号码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
	public Object bindPhone(@RequestParam("token") String token,@RequestParam("phone") String phone,@RequestParam("codes") String codes){
		if(null == token || "" == token){
			LOGGER.error("token无效");
			return new AjaxResponse(501,"token传输错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("无用的token");
			return new AjaxResponse(502,"无用的token数据库无相关数据");
		}
		if(user.getTokenTime().getTime() < System.currentTimeMillis()){
			LOGGER.error("token已过期");
			return new AjaxResponse(503,"token已过期");
		}
		if(null == phone){
			LOGGER.error("手机号码为空");
			return new AjaxResponse(504,"手机号码传输错误");
		}
		Verification cation = verificationService.get(phone);
		if(null == cation){
			LOGGER.error("无此号码的验证码");
			return new AjaxResponse(506,"没有此手机号码的验证码");
		}
		String code = cation.getVerification();
		if(!code.equals(codes)){
			LOGGER.error("验证码错误");
			return new AjaxResponse(505,"传入验证码错误");
		}
		user.setUserPhone(phone);
		user.setDiamond(user.getDiamond()+6);
		userService.update(user);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("phone", user.getUserPhone());
		map.put("diamond", user.getDiamond());
		return new AjaxResponse(map);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public Object getUserInfo(@RequestParam("token") String token){
		User user = userService.getByToken(token);
		return new AjaxResponse(user);
	}
	
}
