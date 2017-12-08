package com.starland.xyqp.pdk.receiver;

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
import com.starland.xyqp.pdk.business.CardShapeJudger;
import com.starland.xyqp.pdk.business.HintJudger;
import com.starland.xyqp.pdk.c2s.C2SApplyDissolveRoom;
import com.starland.xyqp.pdk.c2s.C2SChat;
import com.starland.xyqp.pdk.c2s.C2SCreateRoom;
import com.starland.xyqp.pdk.c2s.C2SJoinRoom;
import com.starland.xyqp.pdk.c2s.C2SLoginByToken;
import com.starland.xyqp.pdk.c2s.C2SOutCard;
import com.starland.xyqp.pdk.c2s.C2SPassCard;
import com.starland.xyqp.pdk.c2s.C2SReady;
import com.starland.xyqp.pdk.consts.PdkConstant;
import com.starland.xyqp.pdk.model.BigSettleInfo;
import com.starland.xyqp.pdk.model.CardShape;
import com.starland.xyqp.pdk.model.DataMaps;
import com.starland.xyqp.pdk.model.HintParam;
import com.starland.xyqp.pdk.model.PlayWay;
import com.starland.xyqp.pdk.model.Room;
import com.starland.xyqp.pdk.model.Seat;
import com.starland.xyqp.pdk.model.SmallSettleInfo;
import com.starland.xyqp.pdk.s2c.S2CAllocateCard;
import com.starland.xyqp.pdk.s2c.S2CApplyDissolveRoom;
import com.starland.xyqp.pdk.s2c.S2CBigSettle;
import com.starland.xyqp.pdk.s2c.S2CChat;
import com.starland.xyqp.pdk.s2c.S2CCreateRoom;
import com.starland.xyqp.pdk.s2c.S2CDissolveRoom;
import com.starland.xyqp.pdk.s2c.S2CHintOutCard;
import com.starland.xyqp.pdk.s2c.S2CJoinRoom;
import com.starland.xyqp.pdk.s2c.S2CJoinRoomOther;
import com.starland.xyqp.pdk.s2c.S2CLeaveRoom;
import com.starland.xyqp.pdk.s2c.S2CLoginByToken;
import com.starland.xyqp.pdk.s2c.S2COutCard;
import com.starland.xyqp.pdk.s2c.S2CPassCard;
import com.starland.xyqp.pdk.s2c.S2CReady;
import com.starland.xyqp.pdk.s2c.S2CReconnection;
import com.starland.xyqp.pdk.s2c.S2CSmallSettle;
import com.starland.xyqp.pdk.s2c.S2CUpLine;

import io.netty.channel.ChannelHandlerContext;

@MessageReceiver
public class PdkReceiver implements PdkConstant {
	
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
		if (param.getCardCount() != 15 && param.getCardCount() != 16) {
			result.setCode(501);
			result.setMsg("参数错误！");
			session.sendMessage(CREATE_ROOM, result);
			return;
		}
		if (param.getPersonCount() != 2 && param.getPersonCount() != 3) {
			result.setCode(501);
			result.setMsg("参数错误！");
			session.sendMessage(CREATE_ROOM, result);
			return;
		}
		if (param.getRoundCount() != 10 && param.getRoundCount() != 20) {
			result.setCode(501);
			result.setMsg("参数错误！");
			session.sendMessage(CREATE_ROOM, result);
			return;
		}
		int consumeDiamond = getConsumeDiamond(param.getRoundCount());
		if (user.getDiamond() < consumeDiamond) {
			result.setCode(501);
			result.setMsg("星钻不够！");
			session.sendMessage(CREATE_ROOM, result);
			return;
		}
		String roomId = randomRoomId();
		Room room = new Room(roomId, param.getPersonCount());
		PlayWay playWay = new PlayWay();
		room.setPlayWay(playWay);
		playWay.setCardCount(param.getCardCount());
		playWay.setPersonCount(param.getPersonCount());
		playWay.setRoundCount(param.getRoundCount());
		playWay.setZhaniao(param.isZhaniao());
		
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
		dataMaps.getRoomMap().put(roomId, room);
		userService.updateRoomId(user.getId(), room.getId());
		Seat seat = findEmptySeat(room);
		joinSeat(session, user, seat);
		result.setCode(200);
		result.setRoomId(roomId);
		session.sendMessage(CREATE_ROOM, result);
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
		
		result.setCardCount(playWay.getCardCount());
		result.setPersonCount(playWay.getPersonCount());
		result.setZhaniao(playWay.isZhaniao());
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
	
	/**
	 * 
	 * @param session
	 */
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
	
	/**
	 * 出牌
	 * @param session
	 * @param param
	 */
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
		if(room.getLastPosition() == seat.getPosition()){
			CardShape lastCardShape = room.getLastCardShape();
			if(lastCardShape.getType() == CardShape.TYPE_BOMB){
				seat.setBombNums(seat.getBombNums() + 1);
			}
		}
		S2COutCard s2cOutCard = new S2COutCard();
		s2cOutCard.setPosition(seat.getPosition());
		CardShape cardShape = getCardShape(cards);
		if (canOut(seat, cardShape, cards)) {
			s2cOutCard.setCanOut(true);
			s2cOutCard.setCards(cards);
			setCardType(cards.size(), s2cOutCard, cardShape);
			removeList(seat.getCardList(), cards);
			room.getLastCards().clear();
			room.getLastCards().addAll(cards);
			room.setLastCardShape(cardShape);
			room.setLastPosition(seat.getPosition());
			seat.getOutList().add(cards);
			broadcast(room, OUT_CARD, s2cOutCard);
			if (seat.getCardList().isEmpty()) {
				if(room.getLastCardShape().getType() == CardShape.TYPE_BOMB){
					seat.setBombNums(seat.getBombNums() + 1);
				}
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
		if (agreeNum >= seatList.size() - 1) {
			dissolveRoom(room);
		} else if (noAgreeNum >= seatList.size() - 1) {
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
		if (null == lastCardShape || lastPosition == outCardPosition) {
			s2cHintOutCard.setCanOut(true);
			s2cHintOutCard.setCanPass(false);
		} else {
			List<List<Integer>> hintCards = getHintCards(lastCardShape, seat.getCardList());
			s2cHintOutCard.setCanOut(!hintCards.isEmpty());
			if (nextSeat(seat).getCardList().size() == 1) {
				if (lastCardShape.getType() == CardShape.TYPE_SINGLE) {
					List<List<Integer>> oneHintCards = new LinkedList<>();
					if(hintCards.size() != 0){
						oneHintCards.add(hintCards.get(hintCards.size() - 1));
					}
					s2cHintOutCard.setHintCards(oneHintCards);
				} else {
					s2cHintOutCard.setHintCards(hintCards);
				}
			} else {
				s2cHintOutCard.setHintCards(hintCards);
			}
		}
		s2cHintOutCard.setCanPass(true);
		routeSession.sendMessage(HINT_OUT_CARD,s2cHintOutCard);
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
	private boolean canOut(Seat seat, CardShape cardShape, List<Integer> cards) {
		Room room = seat.getRoom();
		int lastPosition = room.getLastPosition();
		CardShape lastCardShape = room.getLastCardShape();
		if (null == cardShape) {
			return false;
		}
		// 判断上一家没有出过牌 上家出牌位置就是他自己
		if (lastCardShape == null || lastPosition == seat.getPosition()) {
			if (cardShape.getType() == CardShape.TYPE_SINGLE) {
				Seat nextSeat = nextSeat(seat);
				if (nextSeat.getCardList().size() == 1) {
					return isHighCard(seat, cardShape, cards);
				}
			}
			if (!cardShape.isComplete()) {
				if (seat.getCardList().size() == cards.size()) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
		// 当上一家不是自己的位置
		if (lastCardShape.getType() == cardShape.getType()) {
			if (cardShape.getType() == CardShape.TYPE_BOMB) {
				return lastCardShape.getWeightCode() < cardShape.getWeightCode();
			} else if (cardShape.getType() == CardShape.TYPE_SINGLE) {
				if (nextSeat(seat).getCardList().size() == 1) {
					return isHighCard(seat, cardShape, cards);
				}
				return lastCardShape.getWeightCode() < cardShape.getWeightCode();
			} else {
				if (lastCardShape.getContinueSize() == cardShape.getContinueSize()) {
					 if (!cardShape.isComplete()) {
						if (seat.getCardList().size() == cards.size()) {
							return lastCardShape.getWeightCode() < cardShape.getWeightCode();
						} else {
							return false;
						}
					}
					return lastCardShape.getWeightCode() < cardShape.getWeightCode();
				} else {
					return false;
				}
			}
		}
		return cardShape.getType() == CardShape.TYPE_BOMB;
	}
	
	/**
	 * 判断下家剩一张牌时能否出牌
	 * @param seat
	 * @param cardShape
	 * @param cards
	 * @return
	 */
	public boolean isHighCard(Seat seat, CardShape cardShape, List<Integer> cards) {
		Integer outCard = cards.get(0) % 100;
		List<Integer> cardList = seat.getCardList();
		cardList.sort((carda, cardb) -> {
			int codea = carda % 100;
			int codeb = cardb % 100;
			if (codea != codeb) {
				return codea - codeb;
			}
			return carda / 100 - cardb / 100;
		});
		Integer card = cardList.get(cardList.size() - 1) % 100;
		return card == outCard;
	}
	
	/**
	 * 响应客户端 牌型
	 * @param size
	 * @param s2cOutCard
	 * @param cardShape
	 */
	private void setCardType(int size, S2COutCard s2cOutCard, CardShape cardShape){
		
		if(cardShape.getType() == CardShape.TYPE_THREE_TWO){
			if(size == 3){
				s2cOutCard.setCardType(CardShape.TYPE_THREE);
			}else if(size == 4){
				s2cOutCard.setCardType(CardShape.TYPE_THREE_ONE);
			}else{
				s2cOutCard.setCardType(CardShape.TYPE_THREE_TWO);
			}
		}else{
			s2cOutCard.setCardType(cardShape.getType());
		}
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
		for (Seat pseat : seatList) {
			sendSmallSettle(pseat);
			if (roundCount >= playWay.getRoundCount()) {
				sendBigSettle(pseat);
			}
		}
		for(Seat pseats : seatList){
			pseats.reset();
		}
		if(roundCount >= playWay.getRoundCount()){
			List<Seat> seatLists = doDissolveRoom(room);
			for(Seat pseat : seatLists){
				RouteSession routeSession = pseat.getRouteSession();
				if(routeSession != null){
					routeSession.close("游戏结束");
				}
			}
		}
	}
	
	/**
	 * 小结算 
	 * @param winSeat
	 */
	public void smallSettle(Seat winSeat){
		Room room = winSeat.getRoom();
		List<Seat> seatList = room.getSeatList();
		PlayWay playWay = room.getPlayWay();
		SmallSettleInfo smallSettleInfos = winSeat.getSmallSettleInfo();
		smallSettleInfos.setWin(true);
		for (Seat seat : seatList) {
			int bombNums = seat.getBombNums();
			setOtherBombPoint(room, seat, bombNums, playWay);
			if(seat.getPosition() == winSeat.getPosition()){
				continue;
			}
			if(playWay.isZhaniao()){
				if(room.getZhaniaoPosition() != 0){
					zhaniaoSettle(winSeat, seat, room);
				} else {
					setDoublePoint(seat, smallSettleInfos, playWay, 2, 1);
				}
			}else{
				setDoublePoint(seat, smallSettleInfos, playWay, 2, 1);
			}
		}
	}
	
	/**
	 * 判断扎鸟进行结算
	 * @param winSeat
	 * @param seat
	 * @param room
	 */
	public void zhaniaoSettle(Seat winSeat, Seat seat, Room room){
		int zhaniaoPosition = room.getZhaniaoPosition();
		PlayWay playWay = room.getPlayWay();
		SmallSettleInfo smallSettleInfos = winSeat.getSmallSettleInfo();
		if(winSeat.getPosition() == zhaniaoPosition){
			setDoublePoint(seat, smallSettleInfos, playWay, 4, 2);
		}else{
			if(seat.getPosition() == zhaniaoPosition){
				setDoublePoint(seat, smallSettleInfos, playWay, 4, 2);
			}else{
				setDoublePoint(seat, smallSettleInfos, playWay, 2, 1);
			}
		}
	}
	
	/**
	 * 将通用代码分离
	 * @param seat
	 * @param smallSettleInfos
	 * @param playWay
	 * @param multiple
	 */
	public void setDoublePoint(Seat seat, SmallSettleInfo smallSettleInfos, PlayWay playWay, Integer multipleOne, Integer multipleTwo){
		SmallSettleInfo smallSettleInfo = seat.getSmallSettleInfo();
		if(seat.getCardList().size() == playWay.getCardCount()){
			smallSettleInfo.setScore(0 - seat.getCardList().size() * multipleOne);
			smallSettleInfos.setScore(smallSettleInfos.getScore() - smallSettleInfo.getScore());
		} else if(seat.getCardList().size() == 1){
			smallSettleInfo.setScore(0 - seat.getCardList().size() * 0);
			smallSettleInfos.setScore(smallSettleInfos.getScore() - 0);
		} else {
			smallSettleInfo.setScore(0 - seat.getCardList().size() * multipleTwo);
			smallSettleInfos.setScore(smallSettleInfos.getScore() - smallSettleInfo.getScore());
		}
		
	}
	
	/**
	 * 将其他两个位置炸弹分进行设置
	 * @param room
	 * @param seat
	 */
	public void setOtherBombPoint(Room room, Seat seat, Integer bombNums, PlayWay playWay){
		SmallSettleInfo smallSettleInfos = seat.getSmallSettleInfo();
		if(playWay.getPersonCount() == 2){
			smallSettleInfos.setBombScore(smallSettleInfos.getBombScore() + bombNums * 10);
		}else{
			smallSettleInfos.setBombScore(smallSettleInfos.getBombScore() + bombNums * 20);
		}
		List<Seat> seatList = room.getSeatList();
		for(Seat pseat : seatList){
			if(pseat.getPosition() == seat.getPosition()){
				continue;
			}
			SmallSettleInfo smallSettleInfo = pseat.getSmallSettleInfo();
			smallSettleInfo.setBombScore(smallSettleInfo.getBombScore() - (bombNums * 10));
		}
	}
	
	
	/**
	 * 大结算
	 * @param room
	 */
	private void bigSettle(Room room) {
		List<Seat> seatList = room.getSeatList();
		int roundCount = room.getRoundCount();
		for (Seat seat : seatList) {
			SmallSettleInfo smallSettleInfo = seat.getSmallSettleInfo();
			BigSettleInfo bigSettleInfo = seat.getBigSettleInfo();
			bigSettleInfo.setScore(bigSettleInfo.getScore() + smallSettleInfo.getScore() + smallSettleInfo.getBombScore());
			bigSettleInfo.setBombNums(bigSettleInfo.getBombNums() + seat.getBombNums());
			if(roundCount == 1){
				bigSettleInfo.setHighPoint(smallSettleInfo.getScore());
			}else{
				if(smallSettleInfo.getScore() > bigSettleInfo.getHighPoint()){
					bigSettleInfo.setHighPoint(smallSettleInfo.getScore());
				}
			}
			if(smallSettleInfo.isWin()){
				bigSettleInfo.setWinRound(bigSettleInfo.getWinRound() + 1);
			}
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
		PlayWay playWay = room.getPlayWay();
		boolean hasNext = room.getRoundCount() < playWay.getRoundCount();
		s2cSmallSettle.setHasNext(hasNext);
		s2cSmallSettle.setRoundCount(room.getRoundCount());
		List<S2CSmallSettle.Detail> details = new ArrayList<>(seatList.size());
		s2cSmallSettle.setDetails(details);
		for (Seat pseat : seatList) {
			SmallSettleInfo smallSettleInfo = pseat.getSmallSettleInfo();
			S2CSmallSettle.Detail detail = new S2CSmallSettle.Detail();
			detail.setName(pseat.getUser().getName());
			detail.setBombScore(smallSettleInfo.getBombScore());
			detail.setOffsetScore(smallSettleInfo.getScore());
			detail.setCurrentScore(smallSettleInfo.getBombScore() + smallSettleInfo.getScore());
			detail.setPosition(pseat.getPosition());
			detail.setWin(smallSettleInfo.isWin());
			detail.setSurplus(pseat.getCardList().size());
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
		Date date = new Date();
		List<S2CBigSettle.Detail> details = new ArrayList<>(seatList.size());
		s2cBigSettle.setDetails(details);
		for (Seat pseat : seatList) {
			BigSettleInfo bigSettleInfo = pseat.getBigSettleInfo();
			S2CBigSettle.Detail detail = new S2CBigSettle.Detail();
			detail.setCurrentScore(bigSettleInfo.getScore());
			detail.setPosition(pseat.getPosition());
			detail.setBombNums(bigSettleInfo.getBombNums());
			detail.setSettleTime(date);
			detail.setHighPoint(bigSettleInfo.getHighPoint());
			detail.setWinRound(bigSettleInfo.getWinRound());
			detail.setLoseRound(room.getRoundCount() - bigSettleInfo.getWinRound());
			details.add(detail);
		}
		RouteSession routeSession = seat.getRouteSession();
		routeSession.sendMessage(BIG_SETTLE, s2cBigSettle);
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
		if (roundCount == 10) {
			return 1;
		}
		if (roundCount == 20) {
			return 2;
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
		
		pw.setCardCount(playWay.getCardCount());
		pw.setPersonCount(playWay.getPersonCount());
		pw.setZhaniao(playWay.isZhaniao());
		pw.setRoundCount(playWay.getRoundCount());
		
		List<Seat> seatList = room.getSeatList();
		List<S2CReconnection.UserInfo> userInfos = new ArrayList<>();
		result.setUserInfos(userInfos);
		result.setRoomId(room.getId());
		result.setSeatPosition(seat.getPosition());  
		result.setLastPosition(room.getLastPosition());
		result.setBankerPosition(room.getBankerPosition());
		result.setRoundCount(room.getRoundCount());
		result.setPlayWay(pw);
		result.setSettling(room.isSettling());
		result.setOutCardPosition(room.getOutCardPosition());
		
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
		List<Seat> seatList = room.getSeatList();
		boolean flag = true;
		while(flag){
			room.reset();
			shuffle(room);
			for (Seat seat : seatList) {
				List<Integer> cardList = seat.getCardList();
				if (cardList.contains(403)) {
					room.setOutCardPosition(seat.getPosition());
					flag = false;
					break;
				}
			}
		}
		for (Seat seat : seatList) {
			RouteSession routeSession = seat.getRouteSession();
			S2CAllocateCard s2cAllocateCard = new S2CAllocateCard();
			s2cAllocateCard.setCardList(seat.getCardList());
			s2cAllocateCard.setPosition(room.getOutCardPosition());
			routeSession.sendMessage(ALLOCATE_CARD, s2cAllocateCard);
		}
		//设置扎鸟的位置
		setZhaniaoPosition(room);
		hintOutCard(room);
	}

	/**
	 * 设置扎鸟的位置
	 * @param room
	 */
	public void setZhaniaoPosition(Room room){
		List<Seat> seatList = room.getSeatList();
		PlayWay playWay = room.getPlayWay();
		if(playWay.isZhaniao()){
			for(Seat seat : seatList){
				List<Integer> cardList = seat.getCardList();
				if(cardList.contains(310)){
					room.setZhaniaoPosition(seat.getPosition());
					break;
				}
			}
		}
	}
	
	/**
	 * 洗牌
	 * @param room
	 */
	private void shuffle(Room room) {
		PlayWay playWay = room.getPlayWay();
		if (playWay.getCardCount() == 15) {
			shuffleFifteen(room);
		} else {
			shuffleSixteen(room);
		}
	}
	
	/**
	 * 十六张牌，洗牌
	 * @param room
	 */
	private void shuffleSixteen(Room room) {
		List<Integer> cardList = new LinkedList<>();
		while(true) {
			cardList.clear();
			// 3代表3,15代表2
			for (int i = 3; i <= 13; i++) {
				// 1方片，2梅花，3红桃，4黑桃
				for (int j = 1; j <= 4; j++) {
					// 314红桃A, 410黑桃10
					cardList.add(j * 100 + i);
				}
			}
			cardList.add(115);
			cardList.add(114);
			cardList.add(214);
			cardList.add(314);
			List<Integer> cards = new LinkedList<>();
			while(!cardList.isEmpty()) {
				int index = random.nextInt(cardList.size());
				Integer card = cardList.remove(index);
				cards.add(card);
			}
			List<Seat> seatList = room.getSeatList();
			for (Seat seat : seatList) {
				for (int i = 0; i < 16; i++) {
					seat.getCardList().add(cards.remove(0));
				}
			}
			// 保证二人玩法中有黑桃3
			if (!cardList.contains(403)) {
				break;
			}
		}
	}
	
	/**
	 * 洗牌，15张牌
	 * @param room
	 */
	private void shuffleFifteen(Room room) {
		List<Integer> cardList = new LinkedList<>();
		while(true) {
			cardList.clear();
			// 3代表3,15代表2
			for (int i = 3; i <= 12; i++) {
				// 1方片，2梅花，3红桃，4黑桃
				for (int j = 1; j <= 4; j++) {
					// 314红桃A, 410黑桃10
					cardList.add(j * 100 + i);
				}
			}
			cardList.add(113);
			cardList.add(213);
			cardList.add(313);
			cardList.add(114);
			cardList.add(115);
			List<Integer> cards = new LinkedList<>();
			while(!cardList.isEmpty()) {
				int index = random.nextInt(cardList.size());
				Integer card = cardList.remove(index);
				cards.add(card);
			}
			List<Seat> seatList = room.getSeatList();
			for (Seat seat : seatList) {
				for (int i = 0; i < 15; i++) {
					seat.getCardList().add(cards.remove(0));
				}
			}
			// 保证二人玩法中有黑桃3
			if (!cardList.contains(403)) {
				break;
			}
		}
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
		List<Seat> seatList = doDissolveRoom(room);
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

	private List<Seat> doDissolveRoom(Room room) {
		List<Seat> seatList = room.getSeatList();
		updateDiamond(seatList,room);
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
		return seatList;
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
