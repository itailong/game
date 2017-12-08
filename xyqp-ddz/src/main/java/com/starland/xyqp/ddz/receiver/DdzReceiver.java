package com.starland.xyqp.ddz.receiver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
import com.starland.tools.shape.ShapeParser;
import com.starland.xyqp.common.component.GameServerComponent;
import com.starland.xyqp.common.component.SessionManager;
import com.starland.xyqp.common.exception.UselessRequestException;
import com.starland.xyqp.db.domain.FightDetail;
import com.starland.xyqp.db.domain.FightExploits;
import com.starland.xyqp.db.domain.GameRoom;
import com.starland.xyqp.db.domain.GameServer;
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.db.service.FightDetailService;
import com.starland.xyqp.db.service.FightExploitsService;
import com.starland.xyqp.db.service.GameRoomService;
import com.starland.xyqp.db.service.UserService;
import com.starland.xyqp.ddz.business.CardShapeJudger;
import com.starland.xyqp.ddz.business.HintJudger;
import com.starland.xyqp.ddz.c2s.C2SApplyDissolveRoom;
import com.starland.xyqp.ddz.c2s.C2SCallLandlord;
import com.starland.xyqp.ddz.c2s.C2SChat;
import com.starland.xyqp.ddz.c2s.C2SCreateRoom;
import com.starland.xyqp.ddz.c2s.C2SInsteadCreateRoom;
import com.starland.xyqp.ddz.c2s.C2SJoinRoom;
import com.starland.xyqp.ddz.c2s.C2SLoginByToken;
import com.starland.xyqp.ddz.c2s.C2SOutCard;
import com.starland.xyqp.ddz.c2s.C2SPassCard;
import com.starland.xyqp.ddz.c2s.C2SPassLandlord;
import com.starland.xyqp.ddz.c2s.C2SReady;
import com.starland.xyqp.ddz.consts.DdzConstant;
import com.starland.xyqp.ddz.model.BigSettleInfo;
import com.starland.xyqp.ddz.model.CardShape;
import com.starland.xyqp.ddz.model.DataMaps;
import com.starland.xyqp.ddz.model.HintParam;
import com.starland.xyqp.ddz.model.PlayWay;
import com.starland.xyqp.ddz.model.Room;
import com.starland.xyqp.ddz.model.Seat;
import com.starland.xyqp.ddz.model.SmallSettleInfo;
import com.starland.xyqp.ddz.s2c.S2CAllocateCard;
import com.starland.xyqp.ddz.s2c.S2CApplyDissolveRoom;
import com.starland.xyqp.ddz.s2c.S2CBigSettle;
import com.starland.xyqp.ddz.s2c.S2CCallLandlord;
import com.starland.xyqp.ddz.s2c.S2CChat;
import com.starland.xyqp.ddz.s2c.S2CCreateRoom;
import com.starland.xyqp.ddz.s2c.S2CDissolveRoom;
import com.starland.xyqp.ddz.s2c.S2CHintCallLandload;
import com.starland.xyqp.ddz.s2c.S2CHintOutCard;
import com.starland.xyqp.ddz.s2c.S2CInsteadCreateRoom;
import com.starland.xyqp.ddz.s2c.S2CJoinRoom;
import com.starland.xyqp.ddz.s2c.S2CJoinRoomOther;
import com.starland.xyqp.ddz.s2c.S2CLeaveRoom;
import com.starland.xyqp.ddz.s2c.S2CLoginByToken;
import com.starland.xyqp.ddz.s2c.S2CMakeLandlord;
import com.starland.xyqp.ddz.s2c.S2COutCard;
import com.starland.xyqp.ddz.s2c.S2CPassCard;
import com.starland.xyqp.ddz.s2c.S2CPassLandlord;
import com.starland.xyqp.ddz.s2c.S2CReady;
import com.starland.xyqp.ddz.s2c.S2CReconnection;
import com.starland.xyqp.ddz.s2c.S2CSmallSettle;
import com.starland.xyqp.ddz.s2c.S2CUpLine;

import io.netty.channel.ChannelHandlerContext;

@MessageReceiver
public class DdzReceiver implements DdzConstant {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Resource
	private SessionManager sessionManager;
	
	@Resource
	private UserService userService;
	
	@Resource
	private GameRoomService gameRoomService;
	
	@Resource
	private ScheduledExecutorService scheduledExecutor;
	
	@Resource
	private GameServerComponent gameServerComponent;
	
	@Resource
	private FightExploitsService fightExploitsService;
	
	@Resource
	private FightDetailService fightDetailService;
	
	/**
	 * 数据集合
	 */
	private DataMaps dataMaps = new DataMaps();
	
	/**
	 * 随机对象
	 */
	private Random random = new Random();
	
	private ShapeParser shapeParser = new ShapeParser();
	
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
		Integer userId = user.getId();
		Seat seat = dataMaps.getSeatMap().get(userId);
		if (null != seat) {
			seat.setOnline(false);
			Room room = seat.getRoom();
			S2CLeaveRoom s2cLeaveRoom = new S2CLeaveRoom();
			s2cLeaveRoom.setPosition(seat.getPosition());
			broadcast(room, LEAVE_LINE, s2cLeaveRoom);
		}
	}
	
	@ExceptionHandler(Exception.class)
	public void onException(RouteSession session, Exception ex) {
		if (ex instanceof IOException) {
			return;
		}
		if (ex instanceof UselessRequestException) {
			LOGGER.warn("", ex);
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
	
	@RouteMapping(LOGIN_BY_TOKEN)
	public void loginByToken(RouteSession session, C2SLoginByToken params) {
		ChannelHandlerContext context = session.getChannelHandlerContext();
		String ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
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
		result.setCode(200);
		result.setIp(ip);
		result.setDiamond(user.getDiamond());
		result.setHeadImg(user.getHeadImg());
		result.setName(user.getName());
		result.setSex(user.getSex());
		result.setToken(user.getToken());
		result.setUserId(user.getId());
		session.attr(USER_KEY).set(user);
		session.sendMessage(LOGIN_BY_TOKEN, result);
		checkReconnetcion(session);
	}
	
	@RouteMapping(CREATE_ROOM)
	public void createRoom(RouteSession session, C2SCreateRoom param) {
		S2CCreateRoom result = new S2CCreateRoom();
		User user = session.attr(USER_KEY).get();
		user = userService.get(user.getId());
		int consumeDiamond = getConsumeDiamond(param.getRoundCount());
		if (user.getDiamond() < consumeDiamond) {
			result.setCode(501);
			result.setMsg("星钻不够！");
			session.sendMessage(CREATE_ROOM, result);
			return;
		}
		Integer userId = user.getId();
		if (dataMaps.getSeatMap().containsKey(userId)) {
			throw new UselessRequestException("已经有用户在座位上！");
		}
		String roomId = randomRoomId();
		Room room = new Room(roomId, 3);
		PlayWay playWay = new PlayWay();
		room.setPlayWay(playWay);
		playWay.setFloorCount(param.getFloorCount());
		playWay.setMaxBomb(param.getMaxBomb());
		playWay.setRoundCount(param.getRoundCount());
		
		GameServer gameServer = gameServerComponent.getGameServer();
		GameRoom gameRoom = new GameRoom();
		gameRoom.setId(roomId);
		gameRoom.setCreateTime(new Date());
		gameRoom.setCreatorId(user.getId());
		gameRoom.setCurrentPerson(1);
		gameRoom.setGameName(gameServer.getName());
		gameRoom.setInstead(0);
		gameRoom.setMaxPerson(room.getSeatList().size());
		gameRoom.setRoundCount(param.getRoundCount());
		gameRoom.setScore(1);
		gameRoom.setServerId(gameServer.getId());
		gameRoomService.add(gameRoom);
		
		userService.updateRoomId(user.getId(), roomId);
		
		dataMaps.getRoomMap().put(roomId, room);
		Seat seat = findEmptySeat(room);
		joinSeat(session, user, seat);
		result.setCode(200);
		result.setRoomId(roomId);
		session.sendMessage(CREATE_ROOM, result);
	}
	
	@RouteMapping(INSTEAD_CREATE_ROOM)
	public void insteadCreateRoom(RouteSession session, C2SInsteadCreateRoom param) {
		S2CInsteadCreateRoom result = new S2CInsteadCreateRoom();
		User user = session.attr(USER_KEY).get();
		user = userService.get(user.getId());
		int consumeDiamond = getConsumeDiamond(param.getRoundCount());
		if (user.getDiamond() < consumeDiamond) {
			result.setCode(501);
			result.setMsg("星钻不够！");
			session.sendMessage(INSTEAD_CREATE_ROOM, result);
			return;
		}
		String roomId = randomRoomId();
		Room room = new Room(roomId, 3);
		PlayWay playWay = new PlayWay();
		room.setPlayWay(playWay);
		playWay.setFloorCount(param.getFloorCount());
		playWay.setMaxBomb(param.getMaxBomb());
		playWay.setRoundCount(param.getRoundCount());
		
		GameServer gameServer = gameServerComponent.getGameServer();
		GameRoom gameRoom = new GameRoom();
		gameRoom.setId(roomId);
		gameRoom.setCreateTime(new Date());
		gameRoom.setCreatorId(user.getId());
		gameRoom.setCurrentPerson(1);
		gameRoom.setGameName(gameServer.getName());
		gameRoom.setInstead(1);
		gameRoom.setMaxPerson(room.getSeatList().size());
		gameRoom.setRoundCount(param.getRoundCount());
		gameRoom.setScore(1);
		gameRoom.setServerId(gameServer.getId());
		gameRoomService.add(gameRoom);
		dataMaps.getRoomMap().put(roomId, room);
		result.setCode(200);
		result.setRoomId(roomId);
		session.sendMessage(INSTEAD_CREATE_ROOM, result);
	
	}
	
	@RouteMapping(JOIN_ROOM)
	public void joinRoom(RouteSession session, C2SJoinRoom param) {
		User user = session.attr(USER_KEY).get();
		S2CJoinRoom result = new S2CJoinRoom();
		String roomId =	param.getRoomId();
		Room room = dataMaps.getRoomMap().get(roomId);
		if (null == room) {
			result.setCode(400);
			result.setMsg("房间不存在！");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		Seat seat = findEmptySeat(room);
		if (null == seat) {
			result.setCode(401);
			result.setMsg("房间已满！");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		PlayWay playWay = room.getPlayWay();
		int consumeDiamond = getConsumeDiamond(playWay.getRoundCount());
		if(user.getDiamond() < consumeDiamond){
			result.setCode(402);
			result.setMsg("星钻不够！");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		joinSeat(session, user, seat);
		userService.updateRoomId(user.getId(), room.getId());
		int currentPerson = 0;
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			if (!pseat.isEmpty()) {
				currentPerson++;
			}
		}
		gameRoomService.updateCurrentPerson(room.getId(), currentPerson);
		result.setCode(200);
		result.setRoomId(room.getId());
		result.setSeatPosition(seat.getPosition());
		List<S2CJoinRoom.UserInfo> userInfoList = new ArrayList<>();
		result.setUserInfos(userInfoList);
		
		result.setFloorCount(playWay.getFloorCount());
		result.setMaxBomb(playWay.getMaxBomb());
		result.setRoundCount(playWay.getRoundCount());
		
		for (Seat pseat : seatList) {
			User puser = pseat.getUser();
			if (null == puser) {
				continue;
			}
			RouteSession routeSession = pseat.getRouteSession();
			ChannelHandlerContext pcontext = routeSession.getChannelHandlerContext();
			String pip = ((InetSocketAddress) pcontext.channel().remoteAddress()).getAddress().getHostAddress();
			S2CJoinRoom.UserInfo userInfo = new S2CJoinRoom.UserInfo();
			userInfo.setIp(pip);
			userInfo.setDiamond(puser.getDiamond());
			userInfo.setHeadImg(puser.getHeadImg());
			userInfo.setName(puser.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSex(puser.getSex());
			userInfo.setUserId(puser.getId());
			userInfoList.add(userInfo);
		}
		session.sendMessage(JOIN_ROOM, result);
		
		ChannelHandlerContext context = session.getChannelHandlerContext();
		String ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
		
		S2CJoinRoomOther s2cJoinRoomOther = new S2CJoinRoomOther();
		s2cJoinRoomOther.setIp(ip);
		s2cJoinRoomOther.setDiamond(user.getDiamond());
		s2cJoinRoomOther.setHeadImg(user.getHeadImg());
		s2cJoinRoomOther.setName(user.getName());
		s2cJoinRoomOther.setSeatPosition(seat.getPosition());
		s2cJoinRoomOther.setSex(user.getSex());
		s2cJoinRoomOther.setUserId(user.getId());
		for (Seat pseat : seatList) {
			RouteSession routeSession = pseat.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			if (routeSession.equals(session)) {
				continue;
			}
			routeSession.sendMessage(JOIN_ROOM_OTHER, s2cJoinRoomOther);
		}
		if (isFull(room)) {
			scheduledExecutor.schedule(() -> {
				startGame(room);
			}, 500, TimeUnit.MILLISECONDS);
		}
	}
	
	@RouteMapping(LEAVE_ROOM)
	public void leaveRoom(RouteSession session) {
		Seat seat = getSeat(session);
		User user = seat.getUser();
		Integer userId = user.getId();
		Room room = seat.getRoom();
		if (room.getRoundCount() != 0) {
			throw new UselessRequestException("游戏已经开始，不能离开房间！");
		}
		userService.updateRoomId(userId, null);
		dataMaps.getSeatMap().remove(userId);
		seat.setEmpty(true);
		seat.setOnline(false);
		seat.setRouteSession(null);
		seat.setUser(null);
		S2CLeaveRoom s2cLeaveRoom = new S2CLeaveRoom();
		s2cLeaveRoom.setPosition(seat.getPosition());
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			if (pseat.equals(seat)) {
				continue;
			}
			RouteSession routeSession = pseat.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			routeSession.sendMessage(LEAVE_ROOM, s2cLeaveRoom);
		}
	}
	
	@RouteMapping(OUT_CARD)
	public void outCard(RouteSession session, C2SOutCard param) {
		Seat seat = getSeat(session);
		Room room = seat.getRoom();
		List<Integer> cards = param.getCards();
		if (!containsList(seat.getCardList(), cards)) {
			throw new UselessRequestException();
		}
		if (seat.getPosition() != room.getOutCardPosition()) {
			throw new UselessRequestException();
		}
		S2COutCard s2cOutCard = new S2COutCard();
		s2cOutCard.setPosition(seat.getPosition());
		CardShape cardShape = getCardShape(cards);
		if (canOut(seat, cardShape)) {
			int multiple = getMultiple(cardShape,room);
			s2cOutCard.setCanOut(true);
			s2cOutCard.setCards(cards);
			s2cOutCard.setCardType(cardShape.getType());
			s2cOutCard.setMultiple(multiple);
			removeList(seat.getCardList(), cards);
			room.getLastCards().clear();
			room.getLastCards().addAll(cards);
			room.setLastCardShape(cardShape);
			room.setLastPosition(seat.getPosition());
			seat.getOutList().add(cards);
			broadcast(room, OUT_CARD, s2cOutCard);
			if (seat.getCardList().isEmpty()) {
				win(seat);
			} else {
				Seat nseat = nextSeat(seat);
				room.setOutCardPosition(nseat.getPosition());
				hintOutCard(room);
			}
		} else {
			s2cOutCard.setCanOut(false);
			session.sendMessage(OUT_CARD, s2cOutCard);
		}
	}
	
	/**
	 * 获取倍数的方法  并将房间的相关数据进行更新
	 * @param cardShape
	 * @param room
	 * @return
	 */
	public int getMultiple(CardShape cardShape, Room room){
		PlayWay playWay = room.getPlayWay();
		int maxBomb = playWay.getMaxBomb();
		if(cardShape.getType() == CardShape.TYPE_BOMB || cardShape.getType() == CardShape.TYPE_KING_BOMB || cardShape.getType() == CardShape.TYPE_GHOST){
			int bombCount = room.getBombCount();
			if(bombCount <= maxBomb){
				room.setBombCount(room.getBombCount() + 1);
				return (int) Math.pow(2, room.getBombCount());
			}else{
				return (int) Math.pow(2, room.getBombCount());
			}
		}
		return (int) Math.pow(2, room.getBombCount());
	}
	
	@RouteMapping(PASS_CARD)
	public void passCard(RouteSession session, C2SPassCard param) {
		Seat seat = getSeat(session);
		Room room = seat.getRoom();
		CardShape lastCardShape = room.getLastCardShape();
		int lastPosition = room.getLastPosition();
		if (lastCardShape == null || lastPosition == seat.getPosition()) {
			throw new UselessRequestException();
		}
		S2CPassCard s2cPassCard = new S2CPassCard();
		s2cPassCard.setPosition(seat.getPosition());
		broadcast(room, PASS_CARD, s2cPassCard);
		
		Seat nseat = nextSeat(seat);
		room.setOutCardPosition(nseat.getPosition());
		hintOutCard(room);
	}
	
	@RouteMapping(CALL_LANDLORD)
	public void callLandlord(RouteSession session, C2SCallLandlord param) {
		Seat seat = getSeat(session);
		Room room = seat.getRoom();
		int callScore = param.getCallScore();
		int callPosition = room.getCallPosition();
		Seat callSeat = getSeat(room, callPosition);
		if (!seat.equals(callSeat)) {
			throw new UselessRequestException();
		}
		if (callScore < 1 || callScore > 3 || callScore < room.getCallScore()) {
			throw new UselessRequestException();
		}
		room.setCallScore(callScore);
		seat.setCallScore(callScore);
		S2CCallLandlord s2cCallLandlord = new S2CCallLandlord();
		s2cCallLandlord.setCallScore(callScore);
		broadcast(room, CALL_LANDLORD, s2cCallLandlord);
		if (callScore == 3) {
			makeLandlord(seat);
			return;
		}
		List<Seat> seatList = room.getSeatList();
		int passNum = 0;
		for (Seat pseat : seatList) {
			if (pseat.getCallScore() == -1) {
				passNum++;
			}
		}
		if (passNum == seatList.size() - 1) {
			makeLandlord(seat);
			return;
		}
		Seat nseat = getNextCallSeat(seat);
		room.setCallPosition(nseat.getPosition());
		hintCallLandlord(room);
	}

	@RouteMapping(PASS_LANDLORD)
	public void passLandlord(RouteSession session, C2SPassLandlord param) {
		Seat seat = getSeat(session);
		seat.setCallScore(-1);
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		int passNum = 0;
		for (Seat pseat : seatList) {
			if (pseat.getCallScore() == -1) {
				passNum++;
			}
		}
		S2CPassLandlord s2cPassLandlord = new S2CPassLandlord();
		s2cPassLandlord.setPosition(seat.getPosition());
		broadcast(room, PASS_LANDLORD, s2cPassLandlord);
		if (passNum == seatList.size()) {
			// 全部pass，重新开始
			restartGame(room);
		} else if (passNum == seatList.size() - 1) {
			Seat nseat = getNextCallSeat(seat);
			if (nseat.getCallScore() == 0) {
				room.setCallPosition(nseat.getPosition());
				hintCallLandlord(room);
			} else {
				makeLandlord(nseat);
			}
		} else {
			Seat nseat = getNextCallSeat(seat);
			room.setCallPosition(nseat.getPosition());
			hintCallLandlord(room);
		}
	}
	
	@RouteMapping(READY)
	public void ready(RouteSession session, C2SReady param) {
		Seat seat = getSeat(session);
		seat.setReady(true);
		Room room = seat.getRoom();
		S2CReady s2cReady = new S2CReady();
		s2cReady.setPosition(seat.getPosition());
		broadcast(room, READY, s2cReady);
		if (isAllReady(room)) {
			startGame(room);
		}
	}
	
	@RouteMapping(APPLY_DISSOLVE_ROOM)
	public void applyDissolveRoom(RouteSession session, C2SApplyDissolveRoom param) {
		Seat seat = getSeat(session);
		if (null == seat) {
			return;
		}
		User user = seat.getUser();
		Room room = seat.getRoom();
		boolean agree = param.isAgree();
		if (room.getRoundCount() == 0) {
			if (seat.getPosition() == 1 && agree) {
				dissolveRoom(room);
			}
			return;
		}
		if (null == room.getDissolveScheduled()) {
			Runnable dissolveTask = new Runnable() {
				@Override
				public void run() {
					dissolveRoom(room);
				}
			};
			ScheduledFuture<?> scheduledFuture = scheduledExecutor.schedule(dissolveTask, 300, TimeUnit.SECONDS);
			room.setDissolveScheduled(scheduledFuture);
			room.setApplyDissolveId(user.getId());
		}
		if (agree) {
			seat.setAgreeDissolve(1);
		} else {
			seat.setAgreeDissolve(2);
		}
		sendDissolveMessage(room);
		List<Seat> seatList = room.getSeatList();
		int agreeNum = 0;
		int noAgreeNum = 0;
		for (Seat pseat : seatList) {
			int agreeDissolve = pseat.getAgreeDissolve();
			if (agreeDissolve == 1) {
				agreeNum++;
			} else if (agreeDissolve == 2) {
				noAgreeNum++;
			}
		}
		if (agreeNum >= 2) {
			dissolveRoom(room);
		} else if (noAgreeNum >= 2) {
			cancelDissolveRoom(room);
		}
	}
	
	@RouteMapping(CHAT)
	public void chat(RouteSession session, C2SChat param) {
		Seat seat = getSeat(session);
		String content = param.getContent();
		S2CChat s2cChat = new S2CChat();
		s2cChat.setContent(content);
		s2cChat.setPosition(seat.getPosition());
		Room room = seat.getRoom();
		broadcast(room, CHAT, s2cChat);
	}
	
	/**
	 * 消耗房间的钻石
	 * @param room
	 */
	private void consumeDiamond(Room room, Seat seat) {
		User user = seat.getUser();
		int consumeDiamond = getConsumeDiamond(room.getPlayWay().getRoundCount());
		userService.updateConsumeDiamond(user.getId(), consumeDiamond);
	}
	
	/**
	 * 产生地主
	 */
	private void makeLandlord(Seat seat) {
		Room room = seat.getRoom();
		room.setCallPosition(0);
		room.setLandlordPosition(seat.getPosition());
		seat.getCardList().addAll(room.getFloorCards());
		S2CMakeLandlord s2cMakeLandlord = new S2CMakeLandlord();
		s2cMakeLandlord.setFloorCards(room.getFloorCards());
		s2cMakeLandlord.setPosition(seat.getPosition());
		s2cMakeLandlord.setCallScore(room.getCallScore());
		broadcast(room, MAKE_LANDLORD, s2cMakeLandlord);
		room.setOutCardPosition(seat.getPosition());
		hintOutCard(room);
	}
	
	/**
	 * 提示出牌
	 * @param seat
	 */
	private void hintOutCard(Room room) {
		int outCardPosition = room.getOutCardPosition();
		int lastPosition = room.getLastPosition();
		Seat seat = getSeat(room, outCardPosition);
		S2CHintOutCard s2cHintOutCard = new S2CHintOutCard();
		RouteSession routeSession = seat.getRouteSession();
		CardShape lastCardShape = room.getLastCardShape();
		if (lastCardShape == null || lastPosition == outCardPosition) {
			s2cHintOutCard.setCanOut(true);
			s2cHintOutCard.setCanPass(false);
		} else {
			List<List<Integer>> hintCards = getHintCards(lastCardShape, seat.getCardList());
			s2cHintOutCard.setCanOut(!hintCards.isEmpty());
			s2cHintOutCard.setHintCards(hintCards);
			s2cHintOutCard.setCanPass(true);
		}
		routeSession.sendMessage(HINT_OUT_CARD, s2cHintOutCard);
	}
	
	/**
	 * 获取提示的牌的列表
	 * @param lastCards
	 * @param handCards
	 * @return
	 */
	private List<List<Integer>> getHintCards(CardShape lastCardShape, List<Integer> handCards) {
		HintParam hintParam = new HintParam();
		hintParam.setCardShape(lastCardShape);
		hintParam.setCardList(handCards);
		List<List<Integer>> result = new LinkedList<>();
		List<List<List<Integer>>> list = shapeParser.parse(new HintJudger(), hintParam);
		for (List<List<Integer>> cardList : list) {
			result.addAll(cardList);
		}
		return result;
	}
	
	/**
	 * 是否能出
	 * @param room
	 * @param cardShape
	 * @return
	 */
	private boolean canOut(Seat seat, CardShape cardShape) {
		Room room = seat.getRoom();		int lastPosition = room.getLastPosition();
		CardShape lastCardShape = room.getLastCardShape();
		if (null == cardShape) {
			return false;
		}
		if (lastCardShape == null || lastPosition == seat.getPosition()) {
			return true;
		}
		if (lastCardShape.getType() == cardShape.getType()) {
			if(cardShape.getType() == CardShape.TYPE_BOMB){
				return lastCardShape.getWeightCode() < cardShape.getWeightCode();
			}else{
				if(cardShape.getContinueSize() == lastCardShape.getContinueSize()){
					return lastCardShape.getWeightCode() < cardShape.getWeightCode();
				}else{
					return false;
				}
			}
		}
		if (lastCardShape.getType() == CardShape.TYPE_KING_BOMB) {
			return false;
		}
		if (lastCardShape.getType() == CardShape.TYPE_BOMB) {
			return cardShape.getType() == CardShape.TYPE_KING_BOMB;
		}
		if (lastCardShape.getType() == CardShape.TYPE_GHOST) {
			return cardShape.getType() == CardShape.TYPE_KING_BOMB || cardShape.getType() == CardShape.TYPE_BOMB;
		}
		return cardShape.getType() == CardShape.TYPE_KING_BOMB || cardShape.getType() == CardShape.TYPE_BOMB || cardShape.getType() == CardShape.TYPE_GHOST;
	}
	
	/**
	 * 获取牌的牌型
	 * @param cards
	 * @return
	 */
	private CardShape getCardShape(List<Integer> cards) {
		List<CardShape> shapes = shapeParser.parse(new CardShapeJudger(), cards);
		if (shapes.isEmpty()) {
			return null;
		}
		return shapes.get(0);
	}
	
	/**
	 * 赢了
	 * @param seat
	 */
	private void win(Seat seat) {
		Room room = seat.getRoom();
		room.setSettling(true);
		smallSettle(seat);
		bigSettle(room);
		saveBigSettle(room);
		PlayWay playWay = room.getPlayWay();
		int roundCount = room.getRoundCount();
		List<Seat> seatList = room.getSeatList();
		room.setBankerPosition(seat.getPosition());
		for (Seat pseat : seatList) {
			sendSmallSettle(pseat);
			if (roundCount >= playWay.getRoundCount()) {
				sendBigSettle(pseat);
			}
		}
		if(roundCount >= playWay.getRoundCount()){
			doDissolveRoom(room);
			for(Seat pseat : seatList){
				RouteSession routeSession = pseat.getRouteSession();
				if(routeSession != null){
					routeSession.close("游戏结束");
				}
			}
		}
	}
	
	/**
	 * 小结算
	 * @param room
	 */
	private void smallSettle(Seat winSeat) {
		Room room = winSeat.getRoom();
		boolean landlordWin = winSeat.getPosition() == room.getLandlordPosition();
		int score = room.getCallScore();
		boolean spring = isSpring(room);
		int bombCount = room.getBombCount();
		PlayWay playWay = room.getPlayWay();
		if (bombCount > playWay.getMaxBomb()) {
			bombCount = playWay.getMaxBomb();
		}
		int multiple = 1;
		for (int i = 0; i < bombCount; i++) {
			multiple *= 2;
		}
		if (spring) {
			multiple *= 2;
		}
		score *= multiple;
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			SmallSettleInfo smallSettleInfo = seat.getSmallSettleInfo();
			smallSettleInfo.setMultiple(multiple);
			if (seat.getPosition() == room.getLandlordPosition()) {
				if (landlordWin) {
					smallSettleInfo.setScore(score * 2);
					smallSettleInfo.setWin(true);
				} else {
					smallSettleInfo.setScore(-score * 2);
				}
			} else {
				if (landlordWin) {
					smallSettleInfo.setScore(-score);
				} else {
					smallSettleInfo.setScore(score);
					smallSettleInfo.setWin(true);
				}
			}
		}
	}
	
	/**
	 * 大结算
	 * @param room
	 */
	private void bigSettle(Room room) {
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			SmallSettleInfo smallSettleInfo = seat.getSmallSettleInfo();
			BigSettleInfo bigSettleInfo = seat.getBigSettleInfo();
			bigSettleInfo.setScore(bigSettleInfo.getScore() + smallSettleInfo.getScore());
		}
	}
	
	/**
	 * 保存大结算信息
	 * @param room
	 */
	private void saveBigSettle(Room room) {
		GameServer gameServer = gameServerComponent.getGameServer();
		FightExploits fightExploits = new FightExploits();
		fightExploits.setCreateTime(new Date());
		fightExploits.setGameName(gameServer.getName());
		fightExploits.setGameType(gameServer.getGameType());
		fightExploits.setRoomId(room.getId());
		fightExploitsService.add(fightExploits);
		
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			User user = seat.getUser();
			BigSettleInfo bigSettleInfo = seat.getBigSettleInfo();
			FightDetail fightDetail = new FightDetail();
			fightDetail.setExploitsId(fightExploits.getId());
			fightDetail.setPosition(seat.getPosition());
			fightDetail.setScore(bigSettleInfo.getScore());
			fightDetail.setUserId(user.getId());
			fightDetail.setUserName(user.getName());
			fightDetailService.add(fightDetail);
		}
	}
	
	/**
	 * 发送小结算信息
	 * @param seat
	 */
	private void sendSmallSettle(Seat seat) {
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		S2CSmallSettle s2cSmallSettle = new S2CSmallSettle();
		s2cSmallSettle.setBombCount(room.getBombCount());
		PlayWay playWay = room.getPlayWay();
		boolean hasNext = room.getRoundCount() < playWay.getRoundCount();
		s2cSmallSettle.setHasNext(hasNext);
		s2cSmallSettle.setSpring(isSpring(room));
		s2cSmallSettle.setRoundCount(room.getRoundCount());
		List<S2CSmallSettle.Detail> details = new ArrayList<>(seatList.size());
		s2cSmallSettle.setDetails(details);
		for (Seat pseat : seatList) {
			SmallSettleInfo smallSettleInfo = pseat.getSmallSettleInfo();
			BigSettleInfo bigSettleInfo = pseat.getBigSettleInfo();
			S2CSmallSettle.Detail detail = new S2CSmallSettle.Detail();
			detail.setOffsetScore(smallSettleInfo.getScore());
			detail.setMultiple(smallSettleInfo.getMultiple());
			detail.setCurrentScore(bigSettleInfo.getScore());
			detail.setFloorScore(room.getCallScore());
			detail.setLandlord(smallSettleInfo.isLandlord());
			detail.setPosition(pseat.getPosition());
			detail.setWin(smallSettleInfo.isWin());
			detail.setCards(pseat.getCardList());
			details.add(detail);
		}
		RouteSession routeSession = seat.getRouteSession();
		routeSession.sendMessage(SMALL_SETTLE, s2cSmallSettle);
	}
	
	/**
	 * 发送大结算信息
	 * @param seat
	 */
	private void sendBigSettle(Seat seat) {
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		S2CBigSettle s2cBigSettle = new S2CBigSettle();
		List<S2CBigSettle.Detail> details = new ArrayList<>(seatList.size());
		s2cBigSettle.setDetails(details);
		for (Seat pseat : seatList) {
			BigSettleInfo bigSettleInfo = pseat.getBigSettleInfo();
			S2CBigSettle.Detail detail = new S2CBigSettle.Detail();
			detail.setCurrentScore(bigSettleInfo.getScore());
			detail.setPosition(pseat.getPosition());
			details.add(detail);
		}
		RouteSession routeSession = seat.getRouteSession();
		routeSession.sendMessage(BIG_SETTLE, s2cBigSettle);
	}
	
	/**
	 * 是否是春天
	 * @param room
	 * @return
	 */
	private boolean isSpring(Room room) {
		List<Seat> seatList = room.getSeatList();
		int landlordPosition = room.getLandlordPosition();
		int count = 0;
		for (Seat seat : seatList) {
			int size = seat.getOutList().size();
			if (landlordPosition == seat.getPosition()) {
				if (size == 1) {
					return true;
				}
			} else {
				if (size == 0) {
					count++;
				}
			}
		}
		if (count >= 2) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取下一个叫地主的座位
	 * @param seat
	 * @return
	 */
	private Seat getNextCallSeat(Seat seat) {
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		Seat nseat = seat;
		for (int i = 0; i <seatList.size() - 1; i++) {
			nseat = nextSeat(nseat);
			if (nseat.getCallScore() != -1) {
				return nseat;
			}
		}
		return null;
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
	 * 根据局数判断消耗的钻石数量
	 * @param roundCount
	 * @return
	 */
	private int getConsumeDiamond(int roundCount) {
		if (roundCount == 6) {
			return 1;
		}
		if (roundCount == 12) {
			return 2;
		}
		if (roundCount == 20) {
			return 3;
		}
		throw new UselessRequestException("局数不正确！");
	}
	
	private void checkReconnetcion(RouteSession session) {
		Seat seat = getSeat(session);
		if (null == seat) {
			return;
		}
		doReconnection(session, seat);
	}
	
	private void doReconnection(RouteSession session, Seat seat) {
		RouteSession preSession = seat.getRouteSession();
		if (null != preSession) {
			preSession.attr(USER_KEY).set(null);
		}
		seat.setRouteSession(session);
		seat.setOnline(true);
		S2CReconnection result = new S2CReconnection();
		Room room = seat.getRoom();
		PlayWay playWay = room.getPlayWay();
		
		S2CReconnection.PlayWay pw = new S2CReconnection.PlayWay();
		
		pw.setFloorCount(playWay.getFloorCount());
		pw.setMaxBomb(playWay.getMaxBomb());
		pw.setRoundCount(playWay.getRoundCount());
		
		List<Seat> seatList = room.getSeatList();
		List<S2CReconnection.UserInfo> userInfos = new ArrayList<>();
		result.setUserInfos(userInfos);
		result.setRoomId(room.getId());
		result.setSeatPosition(seat.getPosition());
		result.setLastPosition(room.getLastPosition());
		result.setBankerPosition(room.getBankerPosition());
		result.setLandlordPosition(room.getLandlordPosition());
		result.setRoundCount(room.getRoundCount());
		result.setPlayWay(pw);
		result.setFloorCards(room.getFloorCards());
		result.setBombCount(room.getBombCount());
		result.setSettling(room.isSettling());
		result.setCallPosition(room.getCallPosition());
		result.setOutCardPosition(room.getOutCardPosition());
		result.setMultiple((int) Math.pow(2, room.getBombCount()));
		result.setCallScore(room.getCallScore());
		for (Seat pseat : seatList) {
			User puser = pseat.getUser();
			if (null == puser) {
				continue;
			}
			RouteSession routeSession = pseat.getRouteSession();
			ChannelHandlerContext context = routeSession.getChannelHandlerContext();
			String ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
			S2CReconnection.UserInfo userInfo = new S2CReconnection.UserInfo();
			userInfos.add(userInfo);
			userInfo.setCardList(pseat.getCardList());
			userInfo.setDiamond(puser.getDiamond());
			userInfo.setIp(ip);
			userInfo.setHeadImg(puser.getHeadImg());
			userInfo.setName(puser.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setReady(pseat.isReady());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSex(puser.getSex());
			userInfo.setUserId(puser.getId());
			userInfo.setCurrentScore(pseat.getBigSettleInfo().getScore());
		}
		result.setCode(200);
		session.sendMessage(RECONNECTION, result);
		
		for (Seat pseat : seatList) {
			RouteSession routeSession = pseat.getRouteSession();
			if (pseat.equals(seat) || null == routeSession) {
				continue;
			}
			S2CUpLine s2cUpLine = new S2CUpLine();
			s2cUpLine.setPosition(seat.getPosition());
			routeSession.sendMessage(UP_LINE, s2cUpLine);
		}
		
		if (room.isSettling() && !seat.isReady()) {
			sendSmallSettle(seat);
		}
		
		if (null != room.getDissolveScheduled()) {
			sendDissolveMessage(seat);
		}
		
		if (!room.isSettling() && room.getOutCardPosition() == seat.getPosition()) {
			hintOutCard(room);
		}
		if (room.getLandlordPosition() == 0 && room.getCallPosition() == seat.getPosition()) {
			hintCallLandlord(room);
		}
		
	}
	
	private <T> boolean containsList(List<T> src, List<T> target) {
		List<T> list = new ArrayList<>(src.size());
		list.addAll(src);
		for (T obj : target) {
			if (!list.remove(obj)) {
				return false;
			}
		}
		return true;
	}
	
	private <T> boolean removeList(List<T> src, List<T> target) {
		for (T obj : target) {
			if (!src.remove(obj)) {
				return false;
			}
		}
		return true;
	}
	
	private String randomRoomId() {
		while(true) {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int num = random.nextInt(10);
				buf.append(num);
			}
			String roomId = buf.toString();
			GameRoom gameRoom = gameRoomService.get(roomId);
			if (gameRoom == null) {
				return roomId;
			}
		}
	}
	
	/**
	 * 根据位置获取一个座位
	 * @param room
	 * @param position
	 * @return
	 */
	private Seat getSeat(Room room, int position) {
		List<Seat> seats = room.getSeatList();
		if (position <= 0 || position > seats.size()) {
			return null;
		}
		return seats.get(position - 1);
	}
	
	/**
	 * 根据session获取座位
	 * @param session
	 * @return
	 */
	private Seat getSeat(RouteSession session) {
		User user = session.attr(USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = dataMaps.getSeatMap().get(userId);
		return seat;
	}
	
	/**
	 * 查找一个空座位
	 * @param room
	 * @return
	 */
	private Seat findEmptySeat(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (seat.isEmpty()) {
				return seat;
			}
		}
		return null;
	}
	
	/**
	 * 座位是否都坐满了
	 * @param room
	 * @return
	 */
	private boolean isFull(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (seat.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 加入到座位
	 * @param session
	 * @param seat
	 */
	private void joinSeat(RouteSession session, User user, Seat seat) {
		Integer userId = user.getId();
		if (dataMaps.getSeatMap().containsKey(userId)) {
			throw new UselessRequestException("已经有用户在座位上！");
		}
		dataMaps.getSeatMap().put(userId, seat);
		seat.setEmpty(false);
		seat.setOnline(true);
		seat.setRouteSession(session);
		seat.setUser(user);
	}
	
	private void startGame(Room room) {
		room.setRoundCount(room.getRoundCount() + 1);
		room.reset();
		shuffle(room);
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			RouteSession routeSession = seat.getRouteSession();
			S2CAllocateCard s2cAllocateCard = new S2CAllocateCard();
			s2cAllocateCard.setCardList(seat.getCardList());
			routeSession.sendMessage(ALLOCATE_CARD, s2cAllocateCard);
		}
		hintCallLandlord(room);
	}
	
	private void restartGame(Room room) {
		room.reset();
		shuffle(room);
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			RouteSession routeSession = seat.getRouteSession();
			S2CAllocateCard s2cAllocateCard = new S2CAllocateCard();
			s2cAllocateCard.setCardList(seat.getCardList());
			routeSession.sendMessage(ALLOCATE_CARD, s2cAllocateCard);
		}
		hintCallLandlord(room);
	}
	
	private void shuffle(Room room) {
		PlayWay playWay = room.getPlayWay();
		List<Integer> cardList = new LinkedList<>();
		// 3代表3,15代表2
		for (int i = 3; i <= 15; i++) {
			// 1方片，2梅花，3红桃，4黑桃
			for (int j = 1; j <= 4; j++) {
				// 314红桃A, 410黑桃10
				cardList.add(j * 100 + i);
			}
		}
		// 2 小王，3大王
		cardList.add(2);
		cardList.add(3);
		if (playWay.getFloorCount() == 4) {
			// 1鬼牌
			cardList.add(1);
		}
		List<Integer> cards = new LinkedList<>();
		while(!cardList.isEmpty()) {
			int index = random.nextInt(cardList.size());
			Integer card = cardList.remove(index);
			cards.add(card);
		}
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			for (int i = 0; i < 17; i++) {
				seat.getCardList().add(cards.remove(0));
			}
			seat.getCardList().sort((o1, o2) -> {
				return o1 - o2;
			});
		}
		room.getFloorCards().addAll(cards);
	}
	
	/**
	 * 提示叫地主
	 * @param room
	 */
	private void hintCallLandlord(Room room) {
		int callPosition = room.getCallPosition();
		if (callPosition == 0) {
			return;
		}
		Seat seat = getSeat(room, callPosition);
		int callScore = room.getCallScore();
		S2CHintCallLandload s2cHintCallLandload = new S2CHintCallLandload();
		s2cHintCallLandload.setCallScore(callScore);
		s2cHintCallLandload.setPosition(seat.getPosition());
		broadcast(room, HINT_CALL_LANDLORD, s2cHintCallLandload);
	}
	
	/**
	 * 是否全部准备好了
	 * @param room
	 * @return
	 */
	private boolean isAllReady(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (!seat.isReady()) {
				return false;
			}
		}
		return true;
	}
	
	private void broadcast(Room room, String router, Object msg) {
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			RouteSession session = seat.getRouteSession();
			if (null == session) {
				continue;
			}
			session.sendMessage(router, msg);
		}
	}
	
	/**
	 * 下家的座位
	 * @param seat
	 * @return
	 */
	private Seat nextSeat(Seat seat) {
		Room room = seat.getRoom();
		int size = room.getSeatList().size();
		int position = seat.getPosition() + 1;
		if (position > size) {
			position = 1;
		}
		return getSeat(room, position);
	}
	
	private void sendDissolveMessage(Room room) {
		List<Seat> seatList = room.getSeatList();
		S2CApplyDissolveRoom s2cApplyDissolveRoom = new S2CApplyDissolveRoom();
		List<S2CApplyDissolveRoom.UserInfo> userInfos = new ArrayList<>();
		s2cApplyDissolveRoom.setUserInfos(userInfos);
		ScheduledFuture<?> dissolveScheduled = room.getDissolveScheduled();
		s2cApplyDissolveRoom.setSurplusTime(dissolveScheduled.getDelay(TimeUnit.SECONDS));
		for (Seat seat : seatList) {
			User user = seat.getUser();
			if (null == user) {
				continue;
			}
			if (user.getId().equals(room.getApplyDissolveId())) {
				s2cApplyDissolveRoom.setApplyName(user.getName());
			} else {
				S2CApplyDissolveRoom.UserInfo userInfo = new S2CApplyDissolveRoom.UserInfo();
				userInfo.setAgreeDissolve(seat.getAgreeDissolve());
				userInfo.setPosition(seat.getPosition());
				userInfo.setUserName(user.getName());
				userInfos.add(userInfo);
			}
		}
		broadcast(room, APPLY_DISSOLVE_ROOM, s2cApplyDissolveRoom);
	}
	
	private void sendDissolveMessage(Seat seat) {
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		S2CApplyDissolveRoom s2cApplyDissolveRoom = new S2CApplyDissolveRoom();
		ScheduledFuture<?> dissolveScheduled = room.getDissolveScheduled();
		s2cApplyDissolveRoom.setSurplusTime(dissolveScheduled.getDelay(TimeUnit.SECONDS));
		List<S2CApplyDissolveRoom.UserInfo> userInfos = new ArrayList<>();
		s2cApplyDissolveRoom.setUserInfos(userInfos);
		for (Seat pseat : seatList) {
			RouteSession routeSession = pseat.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			User user = pseat.getUser();
			if (room.getApplyDissolveId().equals(user.getId())) {
				s2cApplyDissolveRoom.setApplyName(user.getName());
			} else {
				S2CApplyDissolveRoom.UserInfo userInfo = new S2CApplyDissolveRoom.UserInfo();
				userInfo.setAgreeDissolve(pseat.getAgreeDissolve());
				userInfo.setPosition(pseat.getPosition());
				userInfo.setUserName(user.getName());
				userInfos.add(userInfo);
			}
		}
		RouteSession session = seat.getRouteSession();
		if (null == session) {
			return;
		}
		session.sendMessage(APPLY_DISSOLVE_ROOM, s2cApplyDissolveRoom);
	}
	
	private void cancelDissolveRoom(Room room) {
		room.setApplyDissolveId(null);
		ScheduledFuture<?> dissolveTask = room.getDissolveScheduled();
		if (null != dissolveTask) {
			dissolveTask.cancel(false);
		}
		room.setDissolveScheduled(null);
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			seat.setAgreeDissolve(0);
		}
		S2CDissolveRoom s2cDissolveRoom = new S2CDissolveRoom();
		if (room.getRoundCount() > 0) {
			s2cDissolveRoom.setStarted(true);
		}
		s2cDissolveRoom.setDissolve(false);
		broadcast(room, DISSOLVE_ROOM, s2cDissolveRoom);
	}
	
	private void dissolveRoom(Room room) {
		List<Seat> seatList = room.getSeatList();
		doDissolveRoom(room);
		S2CDissolveRoom s2cDissolveRoom = new S2CDissolveRoom();
		s2cDissolveRoom.setDissolve(true);
		if (room.getRoundCount() > 0) {
			s2cDissolveRoom.setStarted(true);
		}
		broadcast(room, DISSOLVE_ROOM, s2cDissolveRoom);
		if (room.getRoundCount() == 0) {
			return;
		}
		bigSettle(room);
		saveBigSettle(room);
		for (Seat seat : seatList) {
			sendBigSettle(seat);
			RouteSession routeSession = seat.getRouteSession();
			if (routeSession != null) {
				routeSession.close("游戏结束");
			}
		}
	}

	private void doDissolveRoom(Room room) {
		List<Seat> seatList = room.getSeatList();
		updateDiamond(seatList, room);
		for (Seat seat : seatList) {
			User user = seat.getUser();
			if (null == user) {
				continue;
			}
			Integer userId = user.getId();
			dataMaps.getSeatMap().remove(userId);
		}
		String roomId = room.getId();
		dataMaps.getRoomMap().remove(roomId);
		gameRoomService.delete(room.getId());
		for (Seat seat : seatList) {
			User user = seat.getUser();
			if (null == user) {
				continue;
			}
			userService.updateRoomId(user.getId(), null);
		}
		ScheduledFuture<?> dissolveScheduled = room.getDissolveScheduled();
		if (null != dissolveScheduled) {
			dissolveScheduled.cancel(false);
			room.setDissolveScheduled(null);
		}
	}
	
	
	/**
	 * 修改钻石
	 * @param seatList
	 * @param room
	 */
	private void updateDiamond(List<Seat> seatList, Room room){
		if(room.getRoundCount() > 1){
			int highPoint = 0;
			for(Seat seat : seatList){
				int score = seat.getBigSettleInfo().getScore();
				if(score > highPoint){
					highPoint = score;
				}
			}
			List<Seat> list = new ArrayList<>();
			for(Seat seat : seatList){
				if(seat.getBigSettleInfo().getScore() == highPoint){
					list.add(seat);
				}
			}
			if(list.size() == 1){
				consumeDiamond(room,list.get(0));
			}else{
				int size = random.nextInt(list.size());
				consumeDiamond(room,list.get(size));
			}
		}
	}
	
	
}
