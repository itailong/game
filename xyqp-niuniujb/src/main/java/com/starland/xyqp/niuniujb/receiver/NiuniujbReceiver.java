package com.starland.xyqp.niuniujb.receiver;

import java.io.IOException;
import java.util.Collections;
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
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.db.service.UserService;
import com.starland.xyqp.niuniujb.business.NiuniujbjbLogic;
import com.starland.xyqp.niuniujb.business.RoomManager;
import com.starland.xyqp.niuniujb.c2s.C2SCaseAnte;
import com.starland.xyqp.niuniujb.c2s.C2SChat;
import com.starland.xyqp.niuniujb.c2s.C2SGrabBanker;
import com.starland.xyqp.niuniujb.c2s.C2SJoinRoom;
import com.starland.xyqp.niuniujb.c2s.C2SLoginByToken;
import com.starland.xyqp.niuniujb.consts.NiuniujbConstant;
import com.starland.xyqp.niuniujb.model.PlayWay;
import com.starland.xyqp.niuniujb.model.Room;
import com.starland.xyqp.niuniujb.model.Seat;
import com.starland.xyqp.niuniujb.s2c.S2CChat;
import com.starland.xyqp.niuniujb.s2c.S2CJoinRoom;
import com.starland.xyqp.niuniujb.s2c.S2CReady;

@MessageReceiver
public class NiuniujbReceiver implements NiuniujbConstant {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Resource
	private SessionManager sessionManager;
	
	@Resource
	private ScheduledExecutorService scheduledExecutor;
	
	@Resource
	private NiuniujbjbLogic niuniujbjbLogic;
	
	@Resource
	private RoomManager roomManager;
	
	@Resource
	private UserService userService;
	
	@PostConstruct
	public void init() {
		scheduledExecutor.scheduleAtFixedRate(this::sendHeartbeats, 60, 60, TimeUnit.SECONDS);
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
		User user = session.attr(USER_KEY).get();
		if (null == user) {
			return;
		}
		niuniujbjbLogic.leaveLine(session);
	}
	
	@ExceptionHandler(Exception.class)
	public void onException(RouteSession session, Exception ex) {
		if (ex instanceof IOException) {
			return;
		}
		LOGGER.error("", ex);
		session.close("系统错误！");
	}
	
	@RouteMapping(HEARTBEAT)
	public void heartbeat(RouteSession session) {
		Map<String, Object> msg = Collections.emptyMap();
		session.sendMessage(HEARTBEAT, msg);
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
	
	/**
	 * 登录
	 * @param session
	 * @param params
	 */
	@RouteMapping(LOGIN_BY_TOKEN)
	public void loginByToken(RouteSession session, C2SLoginByToken params) {
		niuniujbjbLogic.loginByToken(session, params);
	}
	
	/**
	 * 加入房间
	 * @param session
	 * @param param
	 */
	@RouteMapping(JOIN_ROOM)
	public void joinRoom(RouteSession session, C2SJoinRoom param){
		User user = session.attr(USER_KEY).get();
		if(user.getGold() < param.getAccessPoint()){
			S2CJoinRoom result = new S2CJoinRoom();
			result.setCode(500);
			result.setMsg("金币不足");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		if(!roomManager.access(param)){
			S2CJoinRoom result = new S2CJoinRoom();
			result.setCode(500);
			result.setMsg("参数错误");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		PlayWay playWay = new PlayWay();
		playWay.setBottomScore(param.getBottomScore());
		playWay.setAccessPoint(param.getAccessPoint());
		playWay.setType(param.getType());
		niuniujbjbLogic.joinRoom(session, playWay);
	}
	
	/**
	 * 抢庄
	 * @param session
	 * @param param
	 */
	@RouteMapping(GRAB_BANKER)
	public void grabBanker(RouteSession session, C2SGrabBanker param){
		Seat seat = roomManager.getSeat(session);
		niuniujbjbLogic.grabBanker(seat, param);
	}
	
	/**
	 * 下注
	 * @param session
	 * @param param
	 */
	@RouteMapping(CASE_ANTE)
	public void caseAnte(RouteSession session, C2SCaseAnte param){
		Seat seat = roomManager.getSeat(session);
		niuniujbjbLogic.caseAnte(seat, param);
	}
	
	/**
	 * 开牌
	 * @param session
	 */
	@RouteMapping(OPEN_CARD)
	public void openCard(RouteSession session){
		Seat seat = roomManager.getSeat(session);
		niuniujbjbLogic.openCard(seat);
	}
	
	/**
	 * 离开房间
	 * @param session
	 */
	@RouteMapping(LEAVE_ROOM)
	public void leaveRoom(RouteSession session){
		niuniujbjbLogic.leaveRoom(session);
	}
	
	
	/**
	 * 准备
	 * @param session
	 */
	@RouteMapping(READY)
	public void ready(RouteSession session){
		Seat seat = roomManager.getSeat(session);
		if(null == seat){
			S2CReady result = new S2CReady();
			result.setCode(501);
			result.setMsg("用户已不在房间中");
			session.sendMessage(READY, result);
			return;
		}
		niuniujbjbLogic.ready(seat);
	}
	
	/**
	 * 聊天
	 * @param session
	 * @param param
	 */
	@RouteMapping(CHAT)
	public void chat(RouteSession session, C2SChat param){
		Seat seat = roomManager.getSeat(session);
		String content = param.getContent();
		S2CChat result = new S2CChat();
		result.setContent(content);
		result.setPosition(seat.getPosition());
		Room room = seat.getRoom();
		roomManager.broadcast(room, CHAT, result);
	}
	
}
