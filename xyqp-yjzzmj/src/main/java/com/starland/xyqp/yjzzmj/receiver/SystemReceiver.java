package com.starland.xyqp.yjzzmj.receiver;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.starland.tools.network.RouteSession;
import com.starland.tools.network.annotation.CloseHandler;
import com.starland.tools.network.annotation.ExceptionHandler;
import com.starland.tools.network.annotation.MessageReceiver;
import com.starland.tools.network.annotation.OpenHandler;
import com.starland.tools.network.annotation.RouteMapping;
import com.starland.xyqp.common.component.SessionManager;
import com.starland.xyqp.common.utils.ConfigUtils;
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.db.service.UserService;
import com.starland.xyqp.yjzzmj.c2s.C2SLoginByToken;
import com.starland.xyqp.yjzzmj.consts.SystemConstant;
import com.starland.xyqp.yjzzmj.s2c.S2CLoginByToken;
import com.starland.xyqp.yjzzmj.s2c.S2CVoiceToken;

@MessageReceiver
public class SystemReceiver implements SystemConstant {

	private static final Logger LOGGER = LogManager.getLogger(SystemReceiver.class);
	
	@Resource
	private SessionManager sessionManager;
	
	@Resource
	private UserService userService;
	
	@Resource
	private MajhongReceiver majhongReceiver;
	
	@Resource
	private ScheduledExecutorService scheduledExecutor;
	
	@PostConstruct
	public void init() {
		scheduledExecutor.scheduleAtFixedRate(this::sendHeartbeats, 60, 60, TimeUnit.SECONDS);
	}
	
	/**
	 * 发送心跳
	 */
	private void sendHeartbeats() {
		Map<String, Object> msg = Collections.emptyMap();
		sessionManager.values().forEach((session) -> {
			session.sendMessage(HEARTBEAT, msg);
		});
	}
	
	@OpenHandler
	public void openConnection(RouteSession session) {
		sessionManager.add(session);
		LOGGER.debug("连接打开，连接数：" + sessionManager.size());
	}
	
	@CloseHandler
	public void closeConnection(RouteSession session) {
		String id = session.getId();
		sessionManager.remove(id);
		LOGGER.debug("连接关闭，连接数：" + sessionManager.size());
	}
	
	@ExceptionHandler(Exception.class)
	public void onException(RouteSession session, Exception ex) {
		if (ex instanceof IOException) {
			return;
		}
		LOGGER.error("", ex);
	}
	
	@RouteMapping(LOGIN_BY_TOKEN)
	public void loginByToken(RouteSession session, C2SLoginByToken params) {
		S2CLoginByToken result = new S2CLoginByToken();
		String token = (String) params.getToken();
		if (null == token || "".equals(token)) {
			result.setCode(500);
			result.setMsg("token为空！");
			session.sendMessage(LOGIN_BY_TOKEN, result);
			return;
		}
		User user = userService.getByToken(token);
		if (null == user) {
			result.setCode(501);
			result.setMsg("token为无效！");
			session.sendMessage(LOGIN_BY_TOKEN, result);
			return;
		}
		Date tokenTime = user.getTokenTime();
		if (tokenTime.getTime() < System.currentTimeMillis()) {
			result.setCode(502);
			result.setMsg("token过期！");
			session.sendMessage(LOGIN_BY_TOKEN, result);
			return;
		}
//		token = UUIDUtils.getUUID();
//		user.setToken(token);
//		// 有效时间7天
//		tokenTime = new Date(System.currentTimeMillis() + TOKEN_VALID_TIME);
//		user.setTokenTime(tokenTime);
//		userService.update(user);
		result.setCode(200);
		result.setDiamond(user.getDiamond());
		result.setHeadImg(user.getHeadImg());
		result.setName(user.getName());
		result.setSex(user.getSex());
		result.setToken(user.getToken());
		result.setUserId(user.getId());
		session.attr(USER_KEY).set(user);
		session.sendMessage(LOGIN_BY_TOKEN, result);
		majhongReceiver.checkReconnetcion(session);
	}
	
	@RouteMapping(HEARTBEAT)
	public void heartbeat(RouteSession session) {
		Map<String, Object> msg = Collections.emptyMap();
		session.sendMessage(HEARTBEAT, msg);
	}
	
//	@RouteMapping(SYSTEM_NOTICE)
//	public void systemNotice(RouteSession session) {
//		NoticeQuery query = new NoticeQuery();
//		PageInfo pageInfo = new PageInfo();
//		List<Notice> notices = noticeService.list(query, pageInfo);
//		S2CSystemNotice s2cSystemNotice = new S2CSystemNotice();
//		List<String> contents = new ArrayList<>();
//		for (Notice notice : notices) {
//			contents.add(notice.getContent());
//		}
//		s2cSystemNotice.setContents(contents);
//		session.sendMessage(SYSTEM_NOTICE, s2cSystemNotice);
//	}
	
	@RouteMapping(VOICE_TOKEN)
	public void voiceToken(RouteSession session) {
		S2CVoiceToken s2cVoiceToken = new S2CVoiceToken();
		String voiceToken = ConfigUtils.getProperty("voiceToken");
		s2cVoiceToken.setVoiceToken(voiceToken);
		session.sendMessage(VOICE_TOKEN, s2cVoiceToken);
	}
}
