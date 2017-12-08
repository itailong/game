package com.starland.xyqp.yjzzmj.receiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.starland.tools.network.RouteSession;
import com.starland.tools.network.annotation.CloseHandler;
import com.starland.tools.network.annotation.MessageReceiver;
import com.starland.tools.network.annotation.RouteMapping;
import com.starland.xyqp.common.component.GameServerComponent;
import com.starland.xyqp.db.domain.FightDetail;
import com.starland.xyqp.db.domain.FightExploits;
import com.starland.xyqp.db.domain.GameRoom;
import com.starland.xyqp.db.domain.GameServer;
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.db.service.FightDetailService;
import com.starland.xyqp.db.service.FightExploitsService;
import com.starland.xyqp.db.service.GameRoomService;
import com.starland.xyqp.db.service.UserService;
import com.starland.xyqp.yjzzmj.business.MahjongLogic;
import com.starland.xyqp.yjzzmj.c2s.C2SApplyDissolveRoom;
import com.starland.xyqp.yjzzmj.c2s.C2SBridgeCard;
import com.starland.xyqp.yjzzmj.c2s.C2SBumpCard;
import com.starland.xyqp.yjzzmj.c2s.C2SChat;
import com.starland.xyqp.yjzzmj.c2s.C2SCreateRoom;
import com.starland.xyqp.yjzzmj.c2s.C2SHearCard;
import com.starland.xyqp.yjzzmj.c2s.C2SInsteadCreateRoom;
import com.starland.xyqp.yjzzmj.c2s.C2SJoinRoom;
import com.starland.xyqp.yjzzmj.c2s.C2SOutCard;
import com.starland.xyqp.yjzzmj.c2s.C2SPassCard;
import com.starland.xyqp.yjzzmj.c2s.C2SPrepare;
import com.starland.xyqp.yjzzmj.c2s.C2SReconnection;
import com.starland.xyqp.yjzzmj.c2s.C2SVoice;
import com.starland.xyqp.yjzzmj.c2s.C2SWinCard;
import com.starland.xyqp.yjzzmj.consts.MajhongConstant;
import com.starland.xyqp.yjzzmj.consts.SystemConstant;
import com.starland.xyqp.yjzzmj.model.CardInfo;
import com.starland.xyqp.yjzzmj.model.HearInfo;
import com.starland.xyqp.yjzzmj.model.Operate;
import com.starland.xyqp.yjzzmj.model.PlayWay;
import com.starland.xyqp.yjzzmj.model.Room;
import com.starland.xyqp.yjzzmj.model.Seat;
import com.starland.xyqp.yjzzmj.model.SettleDetail;
import com.starland.xyqp.yjzzmj.model.SettleInfo;
import com.starland.xyqp.yjzzmj.s2c.S2CAllotCards;
import com.starland.xyqp.yjzzmj.s2c.S2CApplyDissolveRoom;
import com.starland.xyqp.yjzzmj.s2c.S2CBridgeCard;
import com.starland.xyqp.yjzzmj.s2c.S2CBumpCard;
import com.starland.xyqp.yjzzmj.s2c.S2CChat;
import com.starland.xyqp.yjzzmj.s2c.S2CCreateRoom;
import com.starland.xyqp.yjzzmj.s2c.S2CDissolveRoom;
import com.starland.xyqp.yjzzmj.s2c.S2CHearCard;
import com.starland.xyqp.yjzzmj.s2c.S2CHintOutCard;
import com.starland.xyqp.yjzzmj.s2c.S2CInsteadCreateRoom;
import com.starland.xyqp.yjzzmj.s2c.S2CJoinRoom;
import com.starland.xyqp.yjzzmj.s2c.S2CJoinRoomOther;
import com.starland.xyqp.yjzzmj.s2c.S2CLeaveRoom;
import com.starland.xyqp.yjzzmj.s2c.S2COutCard;
import com.starland.xyqp.yjzzmj.s2c.S2CPrepare;
import com.starland.xyqp.yjzzmj.s2c.S2CReconnection;
import com.starland.xyqp.yjzzmj.s2c.S2CSettleBig;
import com.starland.xyqp.yjzzmj.s2c.S2CSettleSmall;
import com.starland.xyqp.yjzzmj.s2c.S2CShowOperate;
import com.starland.xyqp.yjzzmj.s2c.S2CTackCardOther;
import com.starland.xyqp.yjzzmj.s2c.S2CTackCardSelf;
import com.starland.xyqp.yjzzmj.s2c.S2CUpLine;
import com.starland.xyqp.yjzzmj.s2c.S2CVoice;
import com.starland.xyqp.yjzzmj.s2c.S2CWinCard;

@MessageReceiver
public class MajhongReceiver implements MajhongConstant {

	@Resource
	private MahjongLogic mahjongLogic;
	
	@Resource
	private UserService userService;
	
	@Resource
	private GameServerComponent gameServerComponent;
	
	@Resource
	private FightExploitsService fightExploitsService;
	
	@Resource
	private FightDetailService fightDetailService;
	
	@Resource
	private GameRoomService gameRoomService;
	
	@Resource
	private ScheduledExecutorService scheduledExecutor;
	
	/**
	 * 随机对象
	 */
	private Random random = new Random();
	
	@CloseHandler
	public void closeConnection(RouteSession session) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		if (null == user) {
			return;
		}
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		if (null != seat) {
			seat.setOnline(false);
			Room room = seat.getRoom();
			S2CLeaveRoom s2cLeaveRoom = new S2CLeaveRoom();
			s2cLeaveRoom.setPosition(seat.getPosition());
			broadcast(room, LEAVE_LINE, s2cLeaveRoom);
		}
	}
	
	@RouteMapping(RECONNECTION)
	public void reconnection(RouteSession session, C2SReconnection param) {
		S2CReconnection result = new S2CReconnection();
		String token = (String) param.getToken();
		if (null == token || "".equals(token)) {
			result.setCode(500);
			result.setMsg("token为空！");
			session.sendMessage(RECONNECTION, result);
			return;
		}
		User user = userService.getByToken(token);
		if (null == user) {
			result.setCode(501);
			result.setMsg("token无效！");
			session.sendMessage(RECONNECTION, result);
			return;
		}
		Date tokenTime = user.getTokenTime();
		if (tokenTime.getTime() < System.currentTimeMillis()) {
			result.setCode(502);
			result.setMsg("token过期！");
			session.sendMessage(RECONNECTION, result);
			return;
		}
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		if (null == seat) {
			result.setCode(400);
			result.setMsg("用户不在房间中！");
			session.sendMessage(RECONNECTION, result);
			return;
		}
		session.attr(SystemConstant.USER_KEY).set(user);
		doReconnection(session, seat);
	}
	
	public void checkReconnetcion(RouteSession session) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		if (null == seat) {
			return;
		}
		doReconnection(session, seat);
	}
	
	@RouteMapping(CREATE_ROOM)
	public void createRoom(RouteSession session, C2SCreateRoom param) {
		S2CCreateRoom result = new S2CCreateRoom();
		User user = session.attr(SystemConstant.USER_KEY).get();
		user = userService.get(user.getId());
		PlayWay playWay = new PlayWay();
		playWay.setHasMenqing(param.isHasMenqing());
		playWay.setHasMultipleMax(param.isHasMultipleMax());
		playWay.setMqjjh(param.isMqjjh());
		playWay.setRoundNum(param.getRoundNum());
		playWay.setYiziqiao(param.isYiziqiao());
		if (playWay.getRoundNum() == 6) {
			if (user.getDiamond() < 1) {
				result.setCode(501);
				result.setMsg("星钻不够！");
				session.sendMessage(CREATE_ROOM, result);
				return;
			}
		} else if (playWay.getRoundNum() == 12) {
			if (user.getDiamond() < 2) {
				result.setCode(501);
				result.setMsg("星钻不够！");
				session.sendMessage(CREATE_ROOM, result);
				return;
			}
		} else {
			result.setCode(500);
			result.setMsg("参数错误！");
			session.sendMessage(CREATE_ROOM, result);
			return;
		}
		String roomId = randomRoomId();
		Room room = mahjongLogic.createRoom(playWay, roomId);
		Seat seat = mahjongLogic.findEmptySeat(room);
		mahjongLogic.joinSeat(session, user, seat);
		
		GameServer gameServer = gameServerComponent.getGameServer();
		GameRoom gameRoom = new GameRoom();
		gameRoom.setCreateTime(new Date());
		gameRoom.setCreatorId(user.getId());
		gameRoom.setCurrentPerson(1);
		gameRoom.setGameName(gameServer.getName());
		gameRoom.setId(room.getId());
		gameRoom.setInstead(0);
		gameRoom.setMaxPerson(4);
		gameRoom.setRoundCount(playWay.getRoundNum());
		gameRoom.setScore(1);
		gameRoom.setServerId(gameServer.getId());
		gameRoomService.add(gameRoom);
		
		userService.updateRoomId(user.getId(), room.getId());
		result.setCode(200);
		result.setRoomId(room.getId());
		session.sendMessage(CREATE_ROOM, result);
	}
	
	@RouteMapping(INSTEAD_CREATE_ROOM)
	public void insteadCreateRoom(RouteSession session, C2SInsteadCreateRoom param) {
		S2CInsteadCreateRoom result = new S2CInsteadCreateRoom();
		User user = session.attr(SystemConstant.USER_KEY).get();
		user = userService.get(user.getId());
		PlayWay playWay = new PlayWay();
		playWay.setHasMenqing(param.isHasMenqing());
		playWay.setHasMultipleMax(param.isHasMultipleMax());
		playWay.setMqjjh(param.isMqjjh());
		playWay.setRoundNum(param.getRoundNum());
		playWay.setYiziqiao(param.isYiziqiao());
		if (playWay.getRoundNum() == 6) {
			if (user.getDiamond() < 1) {
				result.setCode(501);
				result.setMsg("星钻不够！");
				session.sendMessage(INSTEAD_CREATE_ROOM, result);
				return;
			}
		} else if (playWay.getRoundNum() == 12) {
			if (user.getDiamond() < 2) {
				result.setCode(501);
				result.setMsg("星钻不够！");
				session.sendMessage(INSTEAD_CREATE_ROOM, result);
				return;
			}
		} else {
			result.setCode(500);
			result.setMsg("参数错误！");
			session.sendMessage(INSTEAD_CREATE_ROOM, result);
			return;
		}
		String roomId = randomRoomId();
		Room room = mahjongLogic.createRoom(playWay, roomId);
		
		GameServer gameServer = gameServerComponent.getGameServer();
		GameRoom gameRoom = new GameRoom();
		gameRoom.setCreateTime(new Date());
		gameRoom.setCreatorId(user.getId());
		gameRoom.setCurrentPerson(1);
		gameRoom.setGameName(gameServer.getName());
		gameRoom.setId(room.getId());
		gameRoom.setInstead(1);
		gameRoom.setMaxPerson(4);
		gameRoom.setRoundCount(playWay.getRoundNum());
		gameRoom.setScore(1);
		gameRoom.setServerId(gameServer.getId());
		gameRoomService.add(gameRoom);
		
		result.setCode(200);
		result.setRoomId(room.getId());
		session.sendMessage(INSTEAD_CREATE_ROOM, result);
	
	}
	
	@RouteMapping(JOIN_ROOM)
	public void joinRoom(RouteSession session, C2SJoinRoom param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		S2CJoinRoom result = new S2CJoinRoom();
		String roomId =	param.getRoomId();
		Room room = mahjongLogic.getRoom(roomId);
		if (null == room) {
			result.setCode(400);
			result.setMsg("房间不存在！");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		Seat seat = mahjongLogic.findEmptySeat(room);
		if (null == seat) {
			result.setCode(401);
			result.setMsg("房间已满！");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		PlayWay playWay = room.getPlayWay();
		if (playWay.getRoundNum() == 6) {
			if (user.getDiamond() < 1) {
				result.setCode(501);
				result.setMsg("星钻不够！");
				session.sendMessage(JOIN_ROOM, result);
				return;
			}
		} else if (playWay.getRoundNum() == 12) {
			if (user.getDiamond() < 2) {
				result.setCode(501);
				result.setMsg("星钻不够！");
				session.sendMessage(JOIN_ROOM, result);
				return;
			}
		} else {
			result.setCode(500);
			result.setMsg("参数错误！");
			session.sendMessage(JOIN_ROOM, result);
			return;
		}
		mahjongLogic.joinSeat(session, user, seat);
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
		
		result.setHasMenqing(playWay.isHasMenqing());
		result.setHasMultipleMax(playWay.isHasMultipleMax());
		result.setMqjjh(playWay.isMqjjh());
		result.setYiziqiao(playWay.isYiziqiao());
		result.setRoundNum(playWay.getRoundNum());
		
		for (Seat pseat : seatList) {
//			RouteSession routeSession = pseat.getRouteSession();
//			if (null == routeSession) {
//				continue;
//			}
			User puser = pseat.getUser();
			if (null == puser) {
				continue;
			}
			S2CJoinRoom.UserInfo userInfo = new S2CJoinRoom.UserInfo();
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
		
		S2CJoinRoomOther s2cJoinRoomOther = new S2CJoinRoomOther();
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
		if (mahjongLogic.isFull(room)) {
			startGame(room);
		}
	}
	
	@RouteMapping(LEAVE_ROOM)
	public void leaveRoom(RouteSession session) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Room room = seat.getRoom();
		mahjongLogic.leaveRoom(seat, userId);
		userService.updateRoomId(userId, null);
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
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Integer card = param.getCard();
		doOutCard(seat, card);
	}

	@RouteMapping(BUMP_CARD)
	public void bumpCard(RouteSession session, C2SBumpCard param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		CardInfo cardInfo = seat.getCardInfo();
		if (null == cardInfo) {
			return;
		}
		if (!cardInfo.isCanBump()) {
			return;
		}
		if (null != seat.getOperate()) {
			return;
		}
		seat.setCardInfo(null);
		Operate operate = new Operate();
		operate.setBump(true);
		seat.setOperate(operate);
		Room room = seat.getRoom();
		collectOperate(room);
	}
	
	@RouteMapping(BRIEGE_CARD)
	public void bridgeCard(RouteSession session, C2SBridgeCard param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Integer card = param.getCard();
		CardInfo cardInfo = seat.getCardInfo();
		if (null == cardInfo) {
			return;
		}
		if (!cardInfo.isCanBridge()) {
			return;
		}
		if (seat.getCard() != null) {
			// 明杠或过路杠
			doBridgeCard(session, card);
			return;
		}
		if (null != seat.getOperate()) {
			return;
		}
		seat.setCardInfo(null);
		Operate operate = new Operate();
		operate.setBridge(true);
		operate.setBridgeCard(card);
		seat.setOperate(operate);
		Room room = seat.getRoom();
		collectOperate(room);
	}
	
	@RouteMapping(WIN_CARD)
	public void winCard(RouteSession session, C2SWinCard param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		CardInfo cardInfo = seat.getCardInfo();
		if (null == cardInfo) {
			return;
		}
		if (!cardInfo.isCanWin()) {
			return;
		}
		seat.setCardInfo(null);
		if (seat.getCard() != null) {
			// 自摸
			List<Seat> seats = new ArrayList<>();
			seats.add(seat);
			doWinCard(seats);
			return;
		}
		if (null != seat.getOperate()) {
			return;
		}
		Operate operate = new Operate();
		operate.setWin(true);
		seat.setOperate(operate);
		Room room = seat.getRoom();
		collectOperate(room);
	}
	
	@RouteMapping(PASS_CARD)
	public void passCard(RouteSession session, C2SPassCard param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		CardInfo cardInfo = seat.getCardInfo();
		if (null == cardInfo) {
			return;
		}
		seat.setCardInfo(null);
		if (seat.getCard() != null) {
			S2CHintOutCard s2cHintOutCard = new S2CHintOutCard();
			session.sendMessage(HINT_OUT_CARD, s2cHintOutCard);
			return;
		}
		Room room = seat.getRoom();
		if (cardInfo.isCanWin()) {
			Integer lastCard = room.getLastCard();
			seat.getWinPassCards().add(lastCard);
		}
		Operate operate = new Operate();
		operate.setPass(true);
		seat.setOperate(operate);
		collectOperate(room);
	}
	
	@RouteMapping(HEAR_CARD)
	public void hearCard(RouteSession session, C2SHearCard param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Room room = seat.getRoom();
		Integer card = param.getCard();
		doOutCard(seat, card);
		mahjongLogic.hearCard(seat, card);
		S2CHearCard s2cHearCard = new S2CHearCard();
		s2cHearCard.setPosition(seat.getPosition());
		broadcast(room, HEAR_CARD, s2cHearCard);
	}
	
	@RouteMapping(PREPARE)
	public void prepare(RouteSession session, C2SPrepare param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Room room = seat.getRoom();
		int roundCount = room.getRoundCount();
		PlayWay playWay = room.getPlayWay();
		if (roundCount > playWay.getRoundNum()) {
			return;
		}
		seat.setReady(true);
		S2CPrepare s2cPrepare = new S2CPrepare();
		s2cPrepare.setPosition(seat.getPosition());
		broadcast(room, PREPARE, s2cPrepare);
		if (mahjongLogic.isAllReady(room)) {
			room.reset();
			startGame(room);
		}
	}
	
	@RouteMapping(APPLY_DISSOLVE_ROOM)
	public void applyDissolveRoom(RouteSession session, C2SApplyDissolveRoom param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
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
		if (agreeNum >= 3) {
			dissolveRoom(room);
		} else if (noAgreeNum >= 2) {
			cancelDissolveRoom(room);
		}
	}
	
//	@RouteMapping(EXPLOITS)
//	public void exploits(RouteSession session, C2SExploits param) {
//		User user = session.attr(SystemConstant.USER_KEY).get();
//		List<SceneGame> sceneGames = sceneGameService.findByUserId(user.getId());
//		List<BigSettle> bigSettles = Collections.emptyList();
//		if (!sceneGames.isEmpty()) {
//			List<Integer> sceneIds = new ArrayList<>();
//			for (SceneGame sceneGame : sceneGames) {
//				sceneIds.add(sceneGame.getId());
//			}
//			bigSettles = bigSettleService.findBySceneIds(sceneIds);
//		}
//		S2CExploits s2cExploits = new S2CExploits();
//		List<S2CExploits.Element> elements = new ArrayList<>();
//		s2cExploits.setElements(elements);
//		
//		for (SceneGame sceneGame : sceneGames) {
//			S2CExploits.Element element = new S2CExploits.Element();
//			element.setCreateTime(sceneGame.getStartTime());
//			element.setRoomId(sceneGame.getRoomId());
//			List<S2CExploits.Detail> details = new ArrayList<>();
//			element.setDetails(details);
//			elements.add(element);
//			for (BigSettle bigSettle : bigSettles) {
//				if (sceneGame.getId().equals(bigSettle.getSceneId())) {
//					S2CExploits.Detail detail = new S2CExploits.Detail();
//					detail.setName(bigSettle.getUserName());
//					detail.setScore(bigSettle.getScore());
//					details.add(detail);
//				}
//			}
//		}
//		session.sendMessage(EXPLOITS, s2cExploits);
//	}
	
	@RouteMapping(CHAT)
	public void chat(RouteSession session, C2SChat param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Room room = seat.getRoom();
		S2CChat s2cChat = new S2CChat();
		s2cChat.setContent(param.getContent());
		s2cChat.setPosition(seat.getPosition());
		broadcast(room, CHAT, s2cChat);
	}
	
	@RouteMapping(VOICE)
	public void voice(RouteSession session, C2SVoice param) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Room room = seat.getRoom();
		S2CVoice s2cVoice = new S2CVoice();
		s2cVoice.setContent(param.getContent());
		s2cVoice.setPosition(seat.getPosition());
		broadcast(room, VOICE, s2cVoice);
	}
	
	/**
	 * 随机一个房间号
	 * @return
	 */
	private String randomRoomId() {
		while(true) {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int num = random.nextInt(10);
				buf.append(num);
			}
			String roomId = buf.toString();
			GameRoom gameRoom = gameRoomService.get(roomId);
			if (null == gameRoom) {
				return roomId;
			}
		}
	}
	
	private void doOutCard(Seat seat, Integer card) {
		mahjongLogic.outCard(seat, card);
		S2COutCard s2cOutCard = new S2COutCard();
		s2cOutCard.setCard(card);
		s2cOutCard.setPosition(seat.getPosition());
		Room room = seat.getRoom();
		broadcast(room, OUT_CARD, s2cOutCard);
		if (!checkOtherOperate(seat)) {
			Seat nextSeat = mahjongLogic.nextSeat(seat);
			takeCard(nextSeat);
		}
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
	
	
	/**
	 * 进行大结算时解散房间
	 * @param room
	 */
	private void dissolveRooms(Room room){
		updateDiamond(room);
		mahjongLogic.dissolveRoom(room);
		gameRoomService.delete(room.getId());
		List<Seat> seatList = room.getSeatList();
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
//		for (Seat seat : seatList) {
//			RouteSession routeSession = seat.getRouteSession();
//			if (routeSession != null) {
//				routeSession.close("游戏结束");
//			}
//		}
	}
	
	
	/**
	 * 解散房间
	 * @param room
	 */
	private void dissolveRoom(Room room) {
		updateDiamond(room);
		mahjongLogic.dissolveRoom(room);
		gameRoomService.delete(room.getId());
		List<Seat> seatList = room.getSeatList();
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
		S2CDissolveRoom s2cDissolveRoom = new S2CDissolveRoom();
		s2cDissolveRoom.setDissolve(true);
		if (room.getRoundCount() > 0) {
			s2cDissolveRoom.setStarted(true);
		}
		broadcast(room, DISSOLVE_ROOM, s2cDissolveRoom);
		if (room.getRoundCount() == 0) {
			return;
		}
		settleBig(room);
		saveBigSettle(room);
//		for (Seat seat : seatList) {
//			RouteSession routeSession = seat.getRouteSession();
//			if (routeSession != null) {
//				routeSession.close("游戏结束");
//			}
//		}
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
	
	private void doReconnection(RouteSession session, Seat seat) {
		RouteSession preSession = seat.getRouteSession();
		if (null != preSession) {
			preSession.attr(SystemConstant.USER_KEY).set(null);
		}
		seat.setRouteSession(session);
		seat.setOnline(true);
		S2CReconnection result = new S2CReconnection();
		Room room = seat.getRoom();
		PlayWay playWay = room.getPlayWay();
		
		S2CReconnection.PlayWay pw = new S2CReconnection.PlayWay();
		pw.setHasMenqing(playWay.isHasMenqing());
		pw.setHasMultipleMax(playWay.isHasMultipleMax());
		pw.setMqjjh(playWay.isMqjjh());
		pw.setRoundNum(playWay.getRoundNum());
		pw.setYiziqiao(playWay.isYiziqiao());
		
		List<Seat> seatList = room.getSeatList();
		List<S2CReconnection.UserInfo> userInfos = new ArrayList<>();
		result.setUserInfos(userInfos);
		result.setRoomId(room.getId());
		result.setSeatPosition(seat.getPosition());
		result.setLastPosition(room.getLastPosition());
		result.setPlaying(room.isPlaying());
		result.setBankerPosition(room.getBankerPosition());
		result.setRoundCount(room.getRoundCount());
		result.setPlayWay(pw);
		result.setSurplusCardNum(room.getCardList().size());
		
		for (Seat pseat : seatList) {
			User puser = pseat.getUser();
			if (null == puser) {
				continue;
			}
			SettleInfo settleInfo = pseat.getSettleInfo();
			S2CReconnection.UserInfo userInfo = new S2CReconnection.UserInfo();
			userInfos.add(userInfo);
			userInfo.setBumpList(pseat.getBumpList());
			userInfo.setCard(pseat.getCard());
			userInfo.setCardList(pseat.getCardList());
			userInfo.setDiamond(puser.getDiamond());
			userInfo.setHeadImg(puser.getHeadImg());
			userInfo.setHideBridgeList(pseat.getHideBridgeList());
			userInfo.setName(puser.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setOutList(pseat.getOutList());
			userInfo.setPassBridgeList(pseat.getPassBridgeList());
			userInfo.setReady(pseat.isReady());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSex(puser.getSex());
			userInfo.setShowBridgeList(pseat.getShowBridgeList());
			userInfo.setUserId(puser.getId());
			userInfo.setHear(!pseat.getHearList().isEmpty());
			userInfo.setCurrentScore(settleInfo.getCurrentScore());
		}
		result.setCode(200);
		session.sendMessage(RECONNECTION, result);
		CardInfo cardInfo = seat.getCardInfo();
		if (null != cardInfo) {
			S2CShowOperate s2cShowOperate = new S2CShowOperate();
			s2cShowOperate.setBridgeList(cardInfo.getBridgeList());
			s2cShowOperate.setCanBridge(cardInfo.isCanBridge());
			s2cShowOperate.setCanBump(cardInfo.isCanBump());
			s2cShowOperate.setCanWin(cardInfo.isCanWin());
			s2cShowOperate.setCanHear(cardInfo.isCanHear());
			List<HearInfo> hearInfoList = cardInfo.getHearInfoList();
			if (null != hearInfoList) {
				List<S2CShowOperate.HearDetail> hearDetailList = new ArrayList<>();
				s2cShowOperate.setHearDetailList(hearDetailList);
				for (HearInfo hearInfo : hearInfoList) {
					S2CShowOperate.HearDetail detail = new S2CShowOperate.HearDetail();
					detail.setCard(hearInfo.getCard());
					detail.setCardList(hearInfo.getCardList());
					hearDetailList.add(detail);
				}
			}
			session.sendMessage(SHOW_OPERATE, s2cShowOperate);
		} else {
			if (seat.getCard() != null) {
				S2CHintOutCard s2cHintOutCard = new S2CHintOutCard();
				session.sendMessage(HINT_OUT_CARD, s2cHintOutCard);
			}
		}
		
		if (room.isSettling() && !seat.isReady()) {
			S2CSettleSmall s2cSettleSmall = getSettleSamll(room);
			session.sendMessage(SETTLE_SMALL, s2cSettleSmall);
		}
		
		if (null != room.getDissolveScheduled()) {
			sendDissolveMessage(seat);
		}
		
		for (Seat pseat : seatList) {
			RouteSession routeSession = pseat.getRouteSession();
			if (pseat.equals(seat) || null == routeSession) {
				continue;
			}
			S2CUpLine s2cUpLine = new S2CUpLine();
			s2cUpLine.setPosition(seat.getPosition());
			routeSession.sendMessage(UP_LINE, s2cUpLine);
		}
		
	}
	
	private void collectOperate(Room room) {
		Seat targetSeat = null;
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			if (seat.getGrabBridgeCard() != null) {
				targetSeat = seat;
				break;
			}
		}
		int lastPosition = room.getLastPosition();
		if (null == targetSeat) {
			if (lastPosition == 0) {
				return;
			}
			targetSeat = mahjongLogic.getSeat(room, lastPosition);
		}
		Seat seat = targetSeat;
		for (int i = 0; i < seatList.size() - 1; i++) {
			seat = mahjongLogic.nextSeat(seat);
			if (null != seat.getCardInfo()) {
				return;
			}
		}
		seat = targetSeat;
		List<Seat> winSeats = new ArrayList<>();
		for (int i = 0; i < seatList.size() - 1; i++) {
			seat = mahjongLogic.nextSeat(seat);
			Operate operate = seat.getOperate();
			if (null == operate) {
				continue;
			}
			if (operate.isWin()) {
				winSeats.add(seat);
			}
		}
		if (!winSeats.isEmpty()) {
			doWinCard(winSeats);
			return;
		}
		seat = targetSeat;
		for (int i = 0; i < seatList.size() - 1; i++) {
			seat = mahjongLogic.nextSeat(seat);
			Operate operate = seat.getOperate();
			if (null == operate) {
				continue;
			}
			if (operate.isBridge()) {
				RouteSession session = seat.getRouteSession();
				doBridgeCard(session, operate.getBridgeCard());
				return;
			}
			if (operate.isBump()) {
				RouteSession session = seat.getRouteSession();
				doBumpCard(session);
				return;
			}
		}
		if (targetSeat.getGrabBridgeCard() != null) {
			targetSeat.setGrabBridgeCard(null);
			takeCard(targetSeat);
		} else {
			Seat nextSeat = mahjongLogic.nextSeat(targetSeat);
			takeCard(nextSeat);
		}
	}
	
	private void doBumpCard(RouteSession session) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		Room room = seat.getRoom();
		mahjongLogic.bumpCard(seat);
		S2CBumpCard s2cBumpCard = new S2CBumpCard();
		s2cBumpCard.setCard(room.getLastCard());
		s2cBumpCard.setPosition(seat.getPosition());
		broadcast(room, BUMP_CARD, s2cBumpCard);
	}
	
	private void doBridgeCard(RouteSession session, Integer card) {
		User user = session.attr(SystemConstant.USER_KEY).get();
		Integer userId = user.getId();
		Seat seat = mahjongLogic.getSeatByUserId(userId);
		String bridgeType = mahjongLogic.bridgeCard(seat, card);
		S2CBridgeCard s2cBridgeCard = new S2CBridgeCard();
		s2cBridgeCard.setBridgeType(bridgeType);
		s2cBridgeCard.setCard(card);
		s2cBridgeCard.setPosition(seat.getPosition());
		Room room = seat.getRoom();
		broadcast(room, BRIEGE_CARD, s2cBridgeCard);
		if (!"passBridge".equals(bridgeType) || !chcekGrabBridgeOperate(seat, card)) {
			takeCard(seat);
		}
	}
	
	private void doWinCard(List<Seat> seats) {
		mahjongLogic.winCard(seats);
		Room room = seats.get(0).getRoom();
		room.setPlaying(false);
		for (Seat seat : seats) {
			S2CWinCard s2cWinCard = new S2CWinCard();
			s2cWinCard.setPosition(seat.getPosition());
			broadcast(room, WIN_CARD, s2cWinCard);
		}
		settleSmall(room);
		PlayWay playWay = room.getPlayWay();
		if (room.getRoundCount() >= playWay.getRoundNum()) {
			settleBig(room);
			saveBigSettle(room);
			dissolveRooms(room);
		}
	}
	
	/**
	 * 小结算
	 * @param room
	 */
	private void settleSmall(Room room) {
		room.setSettling(true);
		S2CSettleSmall s2cSettleSmall = getSettleSamll(room);
		broadcast(room, SETTLE_SMALL, s2cSettleSmall);
	}

	private S2CSettleSmall getSettleSamll(Room room) {
		PlayWay playWay = room.getPlayWay();
		S2CSettleSmall s2cSettleSmall = new S2CSettleSmall();
		s2cSettleSmall.setZhaniaoCard(room.getZhaniaoCard());
		s2cSettleSmall.setZhaniaoPosition(room.getZhaniaoPosition());
		if (room.getRoundCount() >= playWay.getRoundNum()) {
			s2cSettleSmall.setHasNext(false);
		} else {
			s2cSettleSmall.setHasNext(true);
		}
		List<S2CSettleSmall.Detail> details = new ArrayList<>();
		s2cSettleSmall.setDetails(details);
		List<Seat> seatList = room.getSeatList();
		boolean flow = true;
		for (Seat seat : seatList) {
			S2CSettleSmall.Detail detail = new S2CSettleSmall.Detail();
			detail.setPosition(seat.getPosition());
			SettleInfo settleInfo = seat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			detail.setBridgeScore(settleDetail.getBridgeScore());
			detail.setWinScroe(settleDetail.getWinScroe());
			detail.setCurrentScore(settleDetail.getCurrentScore());
			detail.setDescription(settleDetail.getDescription());
			detail.setOffsetScore(settleDetail.getOffsetScore());
			detail.setCardList(seat.getCardList());
			detail.setWinCard(seat.getCard());
			detail.setWin(settleDetail.isWin());
			detail.setBumpList(seat.getBumpList());
			detail.setHideBridgeList(seat.getHideBridgeList());
			detail.setShowBridgeList(seat.getShowBridgeList());
			detail.setPassBridgeList(seat.getPassBridgeList());
			details.add(detail);
			if (settleDetail.isWin()) {
				flow = false;
			}
		}
		s2cSettleSmall.setFlow(flow);
		return s2cSettleSmall;
	}
	
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
			SettleInfo settleInfo = seat.getSettleInfo();
			
			FightDetail fightDetail = new FightDetail();
			fightDetail.setExploitsId(fightExploits.getId());
			fightDetail.setPosition(seat.getPosition());
			fightDetail.setScore(settleInfo.getCurrentScore());
			fightDetail.setUserId(user.getId());
			fightDetail.setUserName(user.getName());
			fightDetailService.add(fightDetail);
		}
	}
	
	private void settleBig(Room room) {
		S2CSettleBig s2cSettleBig = new S2CSettleBig();
		List<S2CSettleBig.Detail> details = new ArrayList<>();
		s2cSettleBig.setDetails(details);
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			S2CSettleBig.Detail detail = new S2CSettleBig.Detail();
			detail.setPosition(seat.getPosition());
			SettleInfo settleInfo = seat.getSettleInfo();
			detail.setCurrentScore(settleInfo.getCurrentScore());
			detail.setHideBridgeNum(settleInfo.getHideBridgeNum());
			detail.setPointBridgeNum(settleInfo.getPointBridgeNum());
			detail.setPointCannonNum(settleInfo.getPointCannonNum());
			detail.setShowBridgeNum(settleInfo.getShowBridgeNum());
			detail.setWinOtherNum(settleInfo.getWinOtherNum());
			detail.setWinSelfNum(settleInfo.getWinSelfNum());
			details.add(detail);
		}
		broadcast(room, SETTLE_BIG, s2cSettleBig);
	}
	
	private void createSceneGame(Room room, Seat seat) {
		PlayWay playWay = room.getPlayWay();
		int consumeDiamond = 0;
		if (playWay.getRoundNum() == 6) {
			consumeDiamond = 1;
		} else if (playWay.getRoundNum() == 12) {
			consumeDiamond = 2;
		} else {
			throw new IllegalArgumentException("参数异常！");
		}
		User user = seat.getUser();
		user = userService.get(user.getId());
		userService.updateConsumeDiamond(user.getId(), consumeDiamond);
	}
	
	
	/**
	 * 生成分数最大的座位
	 * @param room
	 */
	private void updateDiamond(Room room){
		List<Seat> seatList = room.getSeatList();
		if(room.getRoundCount() > 1){
			int highPoint = 0;
			for(Seat seat : seatList){
				SettleInfo settleInfo = seat.getSettleInfo();
				int currentScore = settleInfo.getCurrentScore();
				if(currentScore > highPoint){
					highPoint = currentScore;
				}
			}
			List<Seat> list = new ArrayList<>();
			for(Seat seat : seatList){
				if(seat.getSettleInfo().getCurrentScore() == highPoint){
					list.add(seat);
				}
			}
			if(list.size() == 1){
				createSceneGame(room, list.get(0));
			}else{
				int size = random.nextInt(list.size());
				createSceneGame(room, list.get(size));
			}
		}
	}
	
	private void startGame(Room room) {
		mahjongLogic.shuffle(room);
		mahjongLogic.allotCards(room);
		room.setPlaying(true);
		room.setSettling(false);
		room.setRoundCount(room.getRoundCount() + 1);
		int bankerPosition = room.getBankerPosition();
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			seat.setReady(false);
			RouteSession session = seat.getRouteSession();
			S2CAllotCards s2cAllotCards = new S2CAllotCards();
			s2cAllotCards.setRoundCount(room.getRoundCount());
			s2cAllotCards.setSurplusCardNum(room.getCardList().size());
			s2cAllotCards.setBankerPosition(bankerPosition);
			List<Integer> cards = new ArrayList<>();
			cards.addAll(seat.getCardList());
			Integer card = seat.getCard();
			if (null != card) {
				cards.add(card);
			}
			s2cAllotCards.setCards(cards);
			session.sendMessage(ALLOT_CARDS, s2cAllotCards);
		}
		
		Seat seat = mahjongLogic.getSeat(room, bankerPosition);
		checkSelfOperate(seat);
	}
	
	private void checkSelfOperate(Seat seat) {
		RouteSession session = seat.getRouteSession();
		mahjongLogic.updateCardInfo(seat);
		seat.setOperate(null);
		CardInfo cardInfo = seat.getCardInfo();
		if (null != cardInfo) {
			S2CShowOperate s2cShowOperate = new S2CShowOperate();
			s2cShowOperate.setBridgeList(cardInfo.getBridgeList());
			s2cShowOperate.setCanBridge(cardInfo.isCanBridge());
			s2cShowOperate.setCanBump(cardInfo.isCanBump());
			s2cShowOperate.setCanWin(cardInfo.isCanWin());
			s2cShowOperate.setCanHear(cardInfo.isCanHear());
			List<HearInfo> hearInfoList = cardInfo.getHearInfoList();
			if (null != hearInfoList) {
				List<S2CShowOperate.HearDetail> hearDetailList = new ArrayList<>();
				s2cShowOperate.setHearDetailList(hearDetailList);
				for (HearInfo hearInfo : hearInfoList) {
					S2CShowOperate.HearDetail detail = new S2CShowOperate.HearDetail();
					detail.setCard(hearInfo.getCard());
					detail.setCardList(hearInfo.getCardList());
					hearDetailList.add(detail);
				}
			}
			session.sendMessage(SHOW_OPERATE, s2cShowOperate);
		} else {
			S2CHintOutCard s2cHintOutCard = new S2CHintOutCard();
			session.sendMessage(HINT_OUT_CARD, s2cHintOutCard);
		}
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
	
	private void takeCard(Seat seat) {
		Room room = seat.getRoom();
		if (mahjongLogic.checkSeafloor(seat)) {
			settleSmall(room);
			PlayWay playWay = room.getPlayWay();
			if (room.getRoundCount() >= playWay.getRoundNum()) {
				settleBig(room);
				saveBigSettle(room);
				dissolveRooms(room);
			}
			return;
		}
		
		Integer card = mahjongLogic.takeCard(seat);
		seat.getWinPassCards().clear();
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			RouteSession routeSession = pseat.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			if (pseat.equals(seat)) {
				S2CTackCardSelf s2cTackCardSelf = new S2CTackCardSelf();
				s2cTackCardSelf.setCard(card);
				s2cTackCardSelf.setSurplusCardNum(room.getCardList().size());
				routeSession.sendMessage(TACK_CARD_SELF, s2cTackCardSelf);
			} else {
				S2CTackCardOther s2cTackCardOther = new S2CTackCardOther();
				s2cTackCardOther.setPosition(seat.getPosition());
				s2cTackCardOther.setSurplusCardNum(room.getCardList().size());
				routeSession.sendMessage(TACK_CARD_OTHER, s2cTackCardOther);
			}
		}
		checkSelfOperate(seat);
	}
	
	private boolean checkOtherOperate(Seat seat) {
		boolean flag = false;
		Room room = seat.getRoom();
		int length = room.getSeatList().size() - 1;
		Seat nextSeat = seat;
		for (int i = 0; i < length; i++) {
			nextSeat = mahjongLogic.nextSeat(nextSeat);
			mahjongLogic.updateCardInfo(nextSeat);
			nextSeat.setOperate(null);
			CardInfo cardInfo = nextSeat.getCardInfo();
			if (null == cardInfo) {
				continue;
			}
			flag = true;
			RouteSession routeSession = nextSeat.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			S2CShowOperate s2cShowOperate = new S2CShowOperate();
			s2cShowOperate.setCanBridge(cardInfo.isCanBridge());
			s2cShowOperate.setCanBump(cardInfo.isCanBump());
			s2cShowOperate.setCanWin(cardInfo.isCanWin());
			s2cShowOperate.setBridgeList(cardInfo.getBridgeList());
			routeSession.sendMessage(SHOW_OPERATE, s2cShowOperate);
		}
		return flag;
	}
	
	private boolean chcekGrabBridgeOperate(Seat seat, Integer card) {
		boolean flag = false;
		mahjongLogic.checkGrapBridgeWin(seat, card);
		Room room = seat.getRoom();
		int length = room.getSeatList().size() - 1;
		Seat nextSeat = seat;
		for (int i = 0; i < length; i++) {
			nextSeat = mahjongLogic.nextSeat(nextSeat);
			nextSeat.setOperate(null);
			CardInfo cardInfo = nextSeat.getCardInfo();
			if (null == cardInfo) {
				continue;
			}
			flag = true;
			RouteSession routeSession = nextSeat.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			S2CShowOperate s2cShowOperate = new S2CShowOperate();
			s2cShowOperate.setCanBridge(cardInfo.isCanBridge());
			s2cShowOperate.setCanBump(cardInfo.isCanBump());
			s2cShowOperate.setCanWin(cardInfo.isCanWin());
			s2cShowOperate.setBridgeList(cardInfo.getBridgeList());
			routeSession.sendMessage(SHOW_OPERATE, s2cShowOperate);
		}
		return flag;
	}
}
