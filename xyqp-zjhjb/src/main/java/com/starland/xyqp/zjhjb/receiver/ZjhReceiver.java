package com.starland.xyqp.zjhjb.receiver;

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
import com.starland.xyqp.zjhjb.business.RoomManager;
import com.starland.xyqp.zjhjb.business.ZjhLogic;
import com.starland.xyqp.zjhjb.c2s.C2SAddAnte;
import com.starland.xyqp.zjhjb.c2s.C2SAnte;
import com.starland.xyqp.zjhjb.c2s.C2SChat;
import com.starland.xyqp.zjhjb.c2s.C2SCompareCard;
import com.starland.xyqp.zjhjb.c2s.C2SGiveUpCard;
import com.starland.xyqp.zjhjb.c2s.C2SJoinRoom;
import com.starland.xyqp.zjhjb.c2s.C2SLeaveRoom;
import com.starland.xyqp.zjhjb.c2s.C2SLoginByToken;
import com.starland.xyqp.zjhjb.c2s.C2SLookCard;
import com.starland.xyqp.zjhjb.c2s.C2SReady;
import com.starland.xyqp.zjhjb.consts.ZjhConstant;
import com.starland.xyqp.zjhjb.model.PlayWay;
import com.starland.xyqp.zjhjb.model.Room;
import com.starland.xyqp.zjhjb.model.Seat;
import com.starland.xyqp.zjhjb.s2c.S2CChat;
import com.starland.xyqp.zjhjb.s2c.S2CJoinRoom;

@MessageReceiver
public class ZjhReceiver implements ZjhConstant {

	private static final Logger LOGGER = LogManager.getLogger();

	@Resource
	private SessionManager sessionManager;

	@Resource
	private ZjhLogic zjhLogic;

	@Resource
	private RoomManager roomManager;

	@Resource
	private ScheduledExecutorService scheduledExecutor;

	@PostConstruct
	public void init() {
		scheduledExecutor.scheduleAtFixedRate(this::sendHeartbeats, 60, 60, TimeUnit.SECONDS);
	}

	@OpenHandler
	public void openConnection(RouteSession routeSession) {
		sessionManager.add(routeSession);
		LOGGER.debug("连接打开,连接数 " + sessionManager.size());
	}

	@CloseHandler
	public void closeConnection(RouteSession routeSession) {
		String id = routeSession.getId();
		sessionManager.remove(id);
		LOGGER.debug("连接关闭，连接数 " + sessionManager.size());
		User user = routeSession.attr(USER_KEY).get();
		if (null == user) {
			return;
		}
		zjhLogic.leaveLine(routeSession);
	}

	@ExceptionHandler(Exception.class)
	public void onException(RouteSession routeSession, Exception ex) {
		if (ex instanceof IOException) {
			return;
		}
		LOGGER.error("", ex);
		routeSession.close("系统错误");
	}

	/**
	 * token登录
	 * @param session
	 * @param params
	 */
	@RouteMapping(LOGIN_BY_TOKEN)
	public void loginByToken(RouteSession session, C2SLoginByToken params) {
		String token = (String) params.getToken();
		zjhLogic.loginByToken(session, token);
	}

	/**
	 * 发送心跳
	 * 
	 * @param routeSession
	 */
	@RouteMapping(HEARTBEAT)
	public void heartbeat(RouteSession routeSession) {
		Map<String, Object> map = Collections.emptyMap();
		routeSession.sendMessage(HEARTBEAT, map);
	}

	/**
	 * 发送心跳
	 */
	public void sendHeartbeats() {
		Map<String, Object> map = Collections.emptyMap();
		sessionManager.values().forEach((session) -> {
			session.sendMessage(HEARTBEAT, map);
		});
	}

	/**
	 * 加入房间
	 * 
	 * @param session
	 * @param param
	 */
	@RouteMapping(JOIN_ROOM)
	public void joinRoom(RouteSession session, C2SJoinRoom param) {
		User pusers = session.attr(ZjhConstant.USER_KEY).get();
		if(!roomManager.access(param)){
			S2CJoinRoom result = new S2CJoinRoom();
			result.setCode(500);
			result.setMsg("参数错误");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		if (pusers.getGold() < param.getLowGold()) {
			S2CJoinRoom result = new S2CJoinRoom();
			result.setCode(500);
			result.setMsg("金币不足");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		PlayWay playWay = new PlayWay();
		playWay.setAntes(param.getAntes());
		playWay.setType(param.getType());
		int antes = roomManager.hasInRoom(pusers, playWay);
		if(antes != 0){
			S2CJoinRoom result = new S2CJoinRoom();
			result.setCode(501);
			result.setMsg("用户已在底分为"+ antes +"的房间中");
		}
		zjhLogic.joinRoom(session, playWay, "");
	}
	

	/**
	 * 准备
	 * @param session
	 * @param param
	 */
	@RouteMapping(READY)
	public void ready(RouteSession session, C2SReady param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.ready(seat);
	}

	/**
	 * 跟注
	 * @param session
	 * @param c2sAnte
	 */
	@RouteMapping(ANTE)
	public void ante(RouteSession session, C2SAnte c2sAnte) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.ante(seat);
	}

	/**
	 * 加注
	 * @param session
	 * @param c2sAddAnte
	 */
	@RouteMapping(ADD_ANTE)
	public void addAnte(RouteSession session, C2SAddAnte param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.addAnte(seat, param);
	}

	/**
	 * 看牌
	 * @param session
	 * @param param
	 */
	@RouteMapping(LOOK_CARD)
	public void lookCard(RouteSession session, C2SLookCard param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.lookCard(seat);
	}

	/**
	 * 弃牌
	 * @param session
	 * @param param
	 */
	@RouteMapping(GIVE_UP_CARD)
	public void giveUpCard(RouteSession session, C2SGiveUpCard param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.giveUpCard(seat);
	}

	/**
	 * 比牌
	 */
	@RouteMapping(COMPARE_CARD)
	public void compareCard(RouteSession session, C2SCompareCard param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.compareCard(seat, param);
	}

	/**
	 * 开牌
	 * @param session
	 */
	@RouteMapping(OPEN_CARD)
	public void openCard(RouteSession session, C2SCompareCard param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.openCards(seat);
	}

	/**
	 * 离开房间
	 * @param session
	 * @param param
	 */
	@RouteMapping(LEAVE_ROOM)
	public void leaveRoom(RouteSession session, C2SLeaveRoom param) {
		Seat seat = roomManager.getSeat(session);
		zjhLogic.leaveRoom(seat, false);
	}
	
	/**
	 * 换桌
	 * @param session
	 */
	@RouteMapping(CHANGE_TABLE)
	public void changeTable(RouteSession session){
		Seat seat = roomManager.getSeat(session);
		zjhLogic.changeTable(seat);
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
