package com.starland.xyqp.zjhjb.business;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.starland.tools.network.RouteSession;
import com.starland.xyqp.common.exception.UselessRequestException;
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.zjhjb.c2s.C2SAddAnte;
import com.starland.xyqp.zjhjb.c2s.C2SCompareCard;
import com.starland.xyqp.zjhjb.consts.ZjhConstant;
import com.starland.xyqp.zjhjb.model.AnteNums;
import com.starland.xyqp.zjhjb.model.CardShape;
import com.starland.xyqp.zjhjb.model.PlayWay;
import com.starland.xyqp.zjhjb.model.Room;
import com.starland.xyqp.zjhjb.model.Seat;
import com.starland.xyqp.zjhjb.s2c.S2CAddAnte;
import com.starland.xyqp.zjhjb.s2c.S2CAllocateCard;
import com.starland.xyqp.zjhjb.s2c.S2CAnte;
import com.starland.xyqp.zjhjb.s2c.S2CCancelRoomSchedule;
import com.starland.xyqp.zjhjb.s2c.S2CCompareCard;
import com.starland.xyqp.zjhjb.s2c.S2CGiveUpCard;
import com.starland.xyqp.zjhjb.s2c.S2CJoinRoom;
import com.starland.xyqp.zjhjb.s2c.S2CJoinRoomOther;
import com.starland.xyqp.zjhjb.s2c.S2CLeaveRoom;
import com.starland.xyqp.zjhjb.s2c.S2CLoginByToken;
import com.starland.xyqp.zjhjb.s2c.S2CLookCard;
import com.starland.xyqp.zjhjb.s2c.S2CLookCardOther;
import com.starland.xyqp.zjhjb.s2c.S2COpenCard;
import com.starland.xyqp.zjhjb.s2c.S2COperate;
import com.starland.xyqp.zjhjb.s2c.S2COperateOther;
import com.starland.xyqp.zjhjb.s2c.S2CReady;
import com.starland.xyqp.zjhjb.s2c.S2CReconnection;
import com.starland.xyqp.zjhjb.s2c.S2CSettleInfo;
import com.starland.xyqp.zjhjb.s2c.S2CStartRoomSchedule;
import com.starland.xyqp.zjhjb.s2c.S2CUpLine;

import io.netty.channel.ChannelHandlerContext;

@Component
public class ZjhLogic implements ZjhConstant {

	@Resource
	private RoomManager roomManager;

	private Random random = new Random();

	@Resource
	private ScheduledExecutorService scheduleExecutor;

	/**
	 * 离线
	 * @param routeSession
	 */
	public void leaveLine(RouteSession session) {
		Seat seat = roomManager.getSeat(session);
		if(seat == null){
//			throw new UselessRequestException("用户已经离开房间");
			return;
		}
		Room room = seat.getRoom();
		if(room.isGameing()){
			if (seat.isGiveUp()) {
				leaveRoom(seat, false);
			} else {
				seat.setOnline(false);
				S2CLeaveRoom result = new S2CLeaveRoom();
				result.setPosition(seat.getPosition());
				roomManager.broadcast(room, LEAVE_LINE, result);
			}
		}else{
			leaveRoom(seat, false);
		}
	}

	/**
	 * token 登录
	 * @param session
	 * @param token
	 */
	public void loginByToken(RouteSession session, String token) {
		ChannelHandlerContext context = session.getChannelHandlerContext();
		String ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
		S2CLoginByToken result = new S2CLoginByToken();
		if (null == token || "".equals(token)) {
			result.setCode(500);
			result.setMsg("token为空！");
			session.sendMessage(LOGIN_BY_TOKEN, result);
			return;
		}
		User user = roomManager.getUser(token);
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
		result.setGold(user.getGold());
		session.attr(USER_KEY).set(user);
		result.setReconnection(checkReconnetcion(session));
		session.sendMessage(LOGIN_BY_TOKEN, result);
		// 断线重连
		if(checkReconnetcion(session)){
			Seat seat = roomManager.getSeat(session);
			doReconnection(session, seat);
		}
	}

	/**
	 * 加入房间
	 * @param session
	 * @param playWay
	 */
	public void joinRoom(RouteSession session, PlayWay playWay, String roomId) {
		Seat seat = null;
		synchronized (roomManager) {
			seat = roomManager.findEmptySeat(playWay, roomId);
			if (null == seat) {
				Room room = roomManager.createRoom(playWay);
				seat = room.getSeatList().get(0);
			}
			roomManager.joinSeat(seat, session);
		}
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		S2CJoinRoom s2cJoinRoom = new S2CJoinRoom();
		s2cJoinRoom.setCode(200);
		s2cJoinRoom.setAntePosition(room.getPosition());
		s2cJoinRoom.setBankerPosition(room.getBankerPosition());
		s2cJoinRoom.setGameing(room.isGameing());
		s2cJoinRoom.setSingleAnte(room.getSingleAnte());
		s2cJoinRoom.setTotalChips(room.getTotalChips());
		s2cJoinRoom.setRoomId(room.getId());
		s2cJoinRoom.setSeatPosition(seat.getPosition());
		s2cJoinRoom.setWinPosition(room.getWinPosition());
		if(room.getScheduled() != null && room.getScheduled().getDelay(TimeUnit.SECONDS) > 0){
			s2cJoinRoom.setRoomTime((int)room.getScheduled().getDelay(TimeUnit.SECONDS));
		}
		List<S2CJoinRoom.UserInfo> userInfos = new ArrayList<>();
		s2cJoinRoom.setUserInfos(userInfos);
		for (Seat pseat : seatList) {
			User puser = pseat.getUser();
			if (null == puser) {
				continue;
			}
			S2CJoinRoom.UserInfo userInfo = new S2CJoinRoom.UserInfo();
			userInfo.setGold(puser.getGold());
			userInfo.setHeadImg(puser.getHeadImg());
			RouteSession routeSession = pseat.getRouteSession();
			ChannelHandlerContext pcontext = routeSession.getChannelHandlerContext();
			String pip = ((InetSocketAddress) pcontext.channel().remoteAddress()).getAddress().getHostAddress();
			userInfo.setIp(pip);
			userInfo.setName(puser.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSex(puser.getSex());
			userInfo.setUserId(puser.getId());
			userInfo.setGiveUpCard(pseat.isGiveUp());
			userInfo.setReady(pseat.isReady());
			userInfo.setTotalAnte(0);
			userInfo.setSeeCard(pseat.isSeeCard());
			if(pseat.getSchedule() != null && pseat.getSchedule().getDelay(TimeUnit.SECONDS) > 0){
				userInfo.setSeatTime((int)pseat.getSchedule().getDelay(TimeUnit.SECONDS));
			}
			userInfos.add(userInfo);
		}
		session.sendMessage(JOIN_ROOM, s2cJoinRoom);

		S2CJoinRoomOther s2cJoinRoomOther = new S2CJoinRoomOther();
		User user = seat.getUser();
		ChannelHandlerContext context = session.getChannelHandlerContext();
		String ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
		s2cJoinRoomOther.setDiamond(user.getDiamond());
		s2cJoinRoomOther.setHeadImg(user.getHeadImg());
		s2cJoinRoomOther.setIp(ip);
		s2cJoinRoomOther.setName(user.getName());
		s2cJoinRoomOther.setSeatPosition(seat.getPosition());
		s2cJoinRoomOther.setSex(user.getSex());
		s2cJoinRoomOther.setUserId(user.getId());
		s2cJoinRoomOther.setGold(user.getGold());
		for (Seat pseat : seatList) {
			if (null == pseat.getUser()) {
				continue;
			}
			RouteSession routeSession = pseat.getRouteSession();
			if (session.equals(routeSession)) {
				continue;
			}
			routeSession.sendMessage(JOIN_ROOM_OTHER, s2cJoinRoomOther);
		}
	}

	/**
	 * 准备
	 * @param seat
	 */
	public void ready(Seat seat) {
		Room room = seat.getRoom();
		S2CReady result = new S2CReady();
		if (seat.getGold() < (room.getPlayWay().getAntes() * 10)) {
			RouteSession routeSession = seat.getRouteSession();
			result.setCode(500);
			result.setMsg("金币不足");
			routeSession.sendMessage(READY, result);
			return;
		}
		if(room.isGameing()){
			throw new UselessRequestException("已经在游戏中不能准备");
		}
		seat.setReady(true);
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, READY, result);
		if(roomManager.isAllReady(room)){
			if(!canecelSchedule(room)){
				return;
			}
			S2CCancelRoomSchedule cancelRoomSchedule = new S2CCancelRoomSchedule();
			roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, cancelRoomSchedule);
			startGame(room);
		}
		if (roomManager.readyNum(room) == 2) {
			S2CStartRoomSchedule startSchedule = new S2CStartRoomSchedule();
			startSchedule.setRoomTime(10);
			roomManager.broadcast(room, START_ROOM_SCHEDULE, startSchedule);
			startSchedule(room);
		}
	}

	/**
	 * 房间定时器
	 * @param room
	 */
	private void startSchedule(Room room) {
		canecelSchedule(room);
		ScheduledFuture<?> schedule = scheduleExecutor.schedule(() -> {
			room.setScheduled(null);
			startGame(room);
		}, 10000, TimeUnit.MILLISECONDS);
		room.setScheduled(schedule);
	}

	/**
	 * 取消房间定时器
	 * @param room
	 */
	private boolean canecelSchedule(Room room) {
		ScheduledFuture<?> scheduled = room.getScheduled();
		if (scheduled != null) {
			room.setScheduled(null);
			if (!scheduled.isCancelled()) {
				return scheduled.cancel(false);
			}
		}
		return false;
	}

	/**
	 * 开始游戏
	 * @param room
	 */
	public void startGame(Room room) { 
		clearNotReady(room);
		List<Seat> seatList = room.getSeatList();
		room.reset();
		shuffle(room);
		seatCardShape(room);
		// 当前先下注人的位置
		int position = room.getWinPosition();
		if (position == 0) {
			position = random.nextInt(5) + 1;
		}
		Seat seat = roomManager.getSeat(room, position);
		if (null == seat.getUser() || !seat.isReady()) {
			seat = roomManager.getNextSeat(seat);
		}
		// 设置初始单注
		room.setSingleAnte(room.getPlayWay().getAntes());
		// 庄家的位置
		room.setBankerPosition(seat.getPosition());
		room.setPosition(seat.getPosition());
		room.setTotalChips(roomManager.gameingSize(room) * room.getSingleAnte());
		S2CAllocateCard result = new S2CAllocateCard();
		List<S2CAllocateCard.GoldInfo> goldInfos = new ArrayList<>();
		result.setPosition(seat.getPosition());
		result.setGoldInfos(goldInfos);
		result.setSingleAnte(room.getSingleAnte());
		result.setTotalChips(room.getTotalChips());
		for (Seat pseat : seatList) {
			RouteSession routeSession = pseat.getRouteSession();
			if (null == routeSession || !pseat.isReady()) {
				continue;
			}
			S2CAllocateCard.GoldInfo goldInfo = new S2CAllocateCard.GoldInfo();
			pseat.setGold(pseat.getGold() - room.getSingleAnte());
			pseat.setTotalAnte(room.getPlayWay().getAntes());
			goldInfo.setGold(pseat.getGold());
			goldInfo.setTotalAnte(room.getSingleAnte());
			goldInfo.setSeatPosition(pseat.getPosition());
			goldInfos.add(goldInfo);
		}
		roomManager.broadcast(room, ALLOCATE_CARD, result);
		sendOperateAsNextSeat(seat);
	}

	/**
	 * 跟注
	 * @param seat
	 */
	public void ante(Seat seat) {
		if(!canecelScheduleSeat(seat)){
			return;
		}
		Room room = seat.getRoom();
		int shouldGold = openCard(seat, 2, 1);
		seat.setGold(seat.getGold() - shouldGold);
		room.setTotalChips(room.getTotalChips() + shouldGold);
		seat.setTotalAnte(seat.getTotalAnte() + shouldGold);
		S2CAnte s2cAnte = new S2CAnte();
		s2cAnte.setGoldChange(shouldGold);
		s2cAnte.setTotalChips(room.getTotalChips());
		s2cAnte.setUserGold(seat.getGold());
		s2cAnte.setTotalAnte(seat.getTotalAnte());
		s2cAnte.setPosition(seat.getPosition());
		s2cAnte.setRoundNum(room.getRoundNums());
		roomManager.broadcast(room, ANTE, s2cAnte);
		if(room.getTotalChips() >= (room.getPlayWay().getAntes() * 1000)){
			systemAutoOpenCard(room);
		}else{
			Seat nextSeat = roomManager.getNextSeat(seat);
			sendOperateAsNextSeat(nextSeat);
		}
	}

	/**
	 * 加注
	 * @param seat
	 * @param param
	 */
	public void addAnte(Seat seat, C2SAddAnte param) {
		if(!canecelScheduleSeat(seat)){
			return;
		}
		Room room = seat.getRoom();
		PlayWay playWay = room.getPlayWay();
		int ante = param.getAnte();
		if (!(ante / playWay.getAntes() == 1 || ante / playWay.getAntes() == 3 || ante / playWay.getAntes() == 5
				|| ante / playWay.getAntes() == 8 || ante / playWay.getAntes() == 10)) {
			throw new UselessRequestException("筹码数值不正确");
		}
		room.setSingleAnte(ante);
		int shouldAnte = openCard(seat, 2, 1);
		room.setTotalChips(room.getTotalChips() + shouldAnte);
		seat.setGold(seat.getGold() - shouldAnte);
		seat.setTotalAnte(seat.getTotalAnte() + shouldAnte);
		S2CAddAnte result = new S2CAddAnte();
		result.setGoldChange(shouldAnte);
		result.setSingleGold(room.getSingleAnte());
		result.setTotalChips(room.getTotalChips());
		result.setUserGold(seat.getGold());
		result.setPosition(seat.getPosition());
		result.setTotalAnte(seat.getTotalAnte());
		roomManager.broadcast(room, ADD_ANTE, result);
		if(room.getTotalChips() >= room.getPlayWay().getAntes() * 1000){
			systemAutoOpenCard(room);
		}else{
			Seat nextSeat = roomManager.getNextSeat(seat);
			sendOperateAsNextSeat(nextSeat);
		}
	}

	/**
	 * 看牌
	 * @param seat
	 */
	public void lookCard(Seat seat) {
		Room room = seat.getRoom();
		seat.setSeeCard(true);
		S2CLookCard result = new S2CLookCard();
		result.setCardList(seat.getCardList());
		result.setCardType(seat.getCardShape().getType());
		RouteSession routeSession = seat.getRouteSession();
		routeSession.sendMessage(LOOK_CARD, result);
		S2CLookCardOther s2cLookCardOther = new S2CLookCardOther();
		s2cLookCardOther.setLookCardPosition(seat.getPosition());
		roomManager.broadcast(room, LOOK_CARD_OTHER, s2cLookCardOther);
		sendOperate(seat);
	}

	/**
	 * 弃牌
	 * @param seat
	 */
	public void giveUpCard(Seat seat) {
		canecelScheduleSeat(seat);
		Room room = seat.getRoom();
		seat.setGiveUp(true);
		S2CGiveUpCard result = new S2CGiveUpCard();
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, GIVE_UP_CARD, result);
		if (getSeatPositions(seat).size() == 1) {
			Seat nextSeat = roomManager.getSeat(room, getSeatPositions(seat).get(0));
			List<Integer> list = new ArrayList<>();
			list.add(nextSeat.getPosition());
			settle(list, nextSeat);
		} else {
			Seat nextSeat = roomManager.getNextSeat(seat);
			sendOperateAsNextSeat(nextSeat);
		}
		if (!seat.isOnline()) {
			leaveRoom(seat, false);
		}
	}

	/**
	 * 比牌
	 * @param seat
	 * @param param
	 */
	public void compareCard(Seat seat, C2SCompareCard param) {
		Room room = seat.getRoom();
		int shouldAnte = openCard(seat, 2, 1);
		seat.setGold(seat.getGold() - shouldAnte * 2);
		seat.setTotalAnte(seat.getTotalAnte() + shouldAnte * 2);
		room.setTotalChips(room.getTotalChips() + shouldAnte * 2);
		int comparePosition = param.getComparePosition();
		Seat compareSeat = roomManager.getSeat(room, comparePosition);
		seat.getPositionList().add(compareSeat.getPosition());
		compareSeat.getPositionList().add(seat.getPosition());
		S2CCompareCard result = new S2CCompareCard();
		result.setChangeGold(shouldAnte * 2);
		result.setGoldPosition(seat.getPosition());
		result.setSurplusGold(seat.getGold());
		result.setTotalAnte(seat.getTotalAnte() + shouldAnte * 2);
		result.setTotalChips(room.getTotalChips());
		if (compareCardShape(seat, compareSeat)) {
			compareSeat.setGiveUp(true);
			result.setLosePositon(compareSeat.getPosition());
			result.setWinPosition(seat.getPosition());
		} else {
			seat.setGiveUp(true);
			result.setLosePositon(seat.getPosition());
			result.setWinPosition(compareSeat.getPosition());
		}
		roomManager.broadcast(room, COMPARE_CARD, result);
		if (seat.isGiveUp()) {
			Seat nextSeat = roomManager.getNextSeat(seat);
			sendOperateAsNextSeat(nextSeat);
		} else {
			sendOperate(seat);
		}
	}

	/**
	 * 判断是否大于被比座位的牌
	 * @param seatOne
	 * @param seatTwo
	 * @return
	 */
	public boolean compareCardShape(Seat seatOne, Seat seatTwo) {
		CardShape cardShapeOne = seatOne.getCardShape();
		CardShape cardShapeTwo = seatTwo.getCardShape();
		if (cardShapeOne.getType() == cardShapeTwo.getType()) {
			return compareCard(cardShapeOne.getCardList(), cardShapeTwo.getCardList());
		}
		return cardShapeOne.getType() > cardShapeTwo.getType();
	}

	/**
	 * 将玩家手中牌进行逐一比较
	 * @param cardListOne
	 * @param cardListTwo
	 * @return
	 */
	public boolean compareCard(List<Integer> cardListOne, List<Integer> cardListTwo) {
		for (int i = 0 ; i < 3 ; i++) {
			if (cardListOne.get(2 - i) % 100 == cardListTwo.get(2 - i) % 100) {
				continue;
			}
			return cardListOne.get(2 - i) % 100 > cardListTwo.get(2 - i) % 100;
		}
		return false;
	}

	/**
	 * 开牌
	 * @param seat
	 */
	public void openCards(Seat seat) {
		if(!canecelScheduleSeat(seat)){
			return;
		}
		Room room = seat.getRoom();
		int shouldAnte = openCard(seat, 2, 1);
		seat.setGold(seat.getGold() - shouldAnte * 2);
		seat.setTotalAnte(seat.getTotalAnte() + shouldAnte * 2);
		room.setTotalChips(room.getTotalChips() + shouldAnte * 2);
		S2COpenCard result = new S2COpenCard();
		result.setChangeGold(shouldAnte * 2);
		result.setGoldPosition(seat.getPosition());
		result.setSurplusGold(seat.getGold());
		result.setTotalAnte(seat.getTotalAnte() + shouldAnte * 2);
		result.setTotalChips(room.getTotalChips());
		List<Integer> seatPositions = getSeatPositions(seat);
		List<Integer> winList = new ArrayList<>();
		List<Integer> loseList = new ArrayList<>();
		List<Integer> openSeatList = new ArrayList<>();
		if (seatPositions.size() == 1) {
			Seat compareSeat = roomManager.getSeat(room, seatPositions.get(0));
			canecelScheduleSeat(compareSeat);
			openSeatList.add(seat.getPosition());
			openSeatList.add(compareSeat.getPosition());
			if (compareCardShape(seat, compareSeat)) {
				seat.setGiveUp(true);
				loseList.add(compareSeat.getPosition());
				winList.add(seat.getPosition());
				result.setLosePositon(loseList);
				result.setWinPosition(winList);
				roomManager.broadcast(room, OPEN_CARD, result);
				// 进行结算
				settle(openSeatList, seat);
			} else {
				compareSeat.setGiveUp(true);
				loseList.add(seat.getPosition());
				winList.add(compareSeat.getPosition());
				result.setLosePositon(loseList);
				result.setWinPosition(winList);
				roomManager.broadcast(room, OPEN_CARD, result);
				// 进行结算
				settle(openSeatList, compareSeat);
			}
		} else {
			if (seatHasWin(room, seat, seatPositions)) {
				setGiveUp(room, seatPositions);
				loseList.addAll(seatPositions);
				winList.add(seat.getPosition());
				result.setLosePositon(loseList);
				result.setWinPosition(winList);
				roomManager.broadcast(room, OPEN_CARD, result);
				openSeatList.addAll(seatPositions);
				openSeatList.add(seat.getPosition());
				// 进行结算
				cancelSeatSchedule(room, seatPositions);
				settle(openSeatList, seat);
			} else {
				seat.setGiveUp(true);
				loseList.add(seat.getPosition());
				winList.addAll(seatPositions);
				result.setLosePositon(loseList);
				result.setWinPosition(winList);
				roomManager.broadcast(room, OPEN_CARD, result);
				Seat nextSeat = roomManager.getNextSeat(seat);
				// 向下一家响应操作面板
				sendOperateAsNextSeat(nextSeat);
			}
		}
	}

	/**
	 * 进行结算
	 * @param seat
	 */
	public void settle(List<Integer> seatLists, Seat seat) {
		Room room = seat.getRoom();
		room.setGameing(false);
		room.setWinPosition(seat.getPosition());
		int winGold = (int) Math.round(room.getTotalChips() * 0.9);
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			canecelScheduleSeat(pseat);
			if (!pseat.isReady()) {
				continue;
			}
			if (pseat.getPosition() == seat.getPosition()) {
				pseat.setGold(pseat.getGold() + winGold);
				pseat.setTotalAnte(winGold - pseat.getTotalAnte());
			} else {
				pseat.setTotalAnte(0 - pseat.getTotalAnte());
			}
		}
		roomManager.updateUserGold(room);
		roomManager.saveSettle(room);
		sendSettleInfo(seat, seatLists);
		List<Integer> leaveLineSize = getLeaveLineSize(room);
		if (leaveLineSize.size() != 0) {
			clearLeaveLine(room, leaveLineSize);
		}
	}

	/**
	 * 发送结算信息
	 * @param seat
	 */
	public void sendSettleInfo(Seat seat, List<Integer> openSeatList) {
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		S2CSettleInfo result = new S2CSettleInfo();
		result.setWinSeatPosition(seat.getPosition());
		for (Seat seats : seatList) {
			RouteSession routeSession = seats.getRouteSession();
			if (null == routeSession) {
				continue;
			}
			result.setCardList(seats.getCardList());
			if(seats.isReady()){
				result.setType(seats.getCardShape().getType());
			}
			List<Integer> positionList = seats.getPositionList();
			List<S2CSettleInfo.SeatGoldInfo> seatGoldInfos = new ArrayList<>();
			result.setSeatGoldInfo(seatGoldInfos);
			for (Seat pseat : seatList) {
				if (!pseat.isReady()) {
					continue;
				}
				S2CSettleInfo.SeatGoldInfo seatGoldInfo = new S2CSettleInfo.SeatGoldInfo();
				if (seatEquals(openSeatList, pseat)) {
					seatGoldInfo.setCardList(pseat.getCardList());
					seatGoldInfo.setType(pseat.getCardShape().getType());
				}
				if (seatEquals(positionList, pseat)) {
					seatGoldInfo.setCardList(pseat.getCardList());
					seatGoldInfo.setType(pseat.getCardShape().getType());
				}
				seatGoldInfo.setGold(pseat.getGold());
				seatGoldInfo.setSeatPosition(pseat.getPosition());
				seatGoldInfo.setTotalAnte(pseat.getTotalAnte());
				seatGoldInfos.add(seatGoldInfo);
			}
			routeSession.sendMessage(SETTLE, result);
		}
		for (Seat pseat : seatList) {
			pseat.setReady(false);
		}
	}

	/**
	 * 判断座位号是否相同
	 * @return
	 */
	public boolean seatEquals(List<Integer> seatList, Seat seat) {
		if (seatList.size() == 0) {
			return false;
		}
		for (Integer position : seatList) {
			if (seat.getPosition() == position) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将其他玩家的设置为弃牌
	 * @param room
	 * @param seatPositions
	 */
	public void setGiveUp(Room room, List<Integer> seatPositions) {
		for (int i = 0; i < seatPositions.size(); i++) {
			Seat seat = roomManager.getSeat(room, seatPositions.get(i));
			seat.setGiveUp(true);
		}
	}

	/**
	 * 循环比较判断是否赢了
	 * @param seat
	 * @param seatPositions
	 * @return
	 */
	public boolean seatHasWin(Room room, Seat seat, List<Integer> seatPositions) {
		for (int i = 0; i < seatPositions.size(); i++) {
			if (!compareCardShape(seat, roomManager.getSeat(room, seatPositions.get(i)))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取房间游戏中且没有弃牌的人数
	 * @param seat
	 * @return
	 */
	public List<Integer> getSeatPositions(Seat seat) {
		Room room = seat.getRoom();
		List<Integer> list = new ArrayList<>();
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			if (pseat.getUser() != null && pseat.isReady() == true && !pseat.isGiveUp()
					&& pseat.getPosition() != seat.getPosition()) {
				list.add(pseat.getPosition());
			}
		}
		return list;
	}

	/**
	 * 座位定时器
	 * 
	 * @param seat
	 */
	public void startScheduleSeat(Seat seat) {
		canecelScheduleSeat(seat);
		ScheduledFuture<?> schedule = scheduleExecutor.schedule(() -> {
			seat.setSchedule(null);
			giveUpCard(seat);
		}, 20000, TimeUnit.MILLISECONDS);
		seat.setSchedule(schedule);
	}

	/**
	 * 向下一家响应操作面板并启动定时器
	 * @param seat
	 */
	public void sendOperateAsNextSeat(Seat seat) {
		canecelScheduleSeat(seat);
		Room room = seat.getRoom();
		room.setPosition(seat.getPosition());
		S2COperate result = new S2COperate();
		if (room.getSingleAnte() / room.getPlayWay().getAntes() != 10 && getAddAnte(seat)) {
			result.setAddAnte(true);
			result.setAddAnteNums(getAnteNums(seat));
		}
		if (seat.getGold() - openCard(seat, 2, 1) >= openCard(seat, 4, 2)) {
			result.setAnte(true);
		}
		if (room.getRoundNums() >= 2) {
			if (roomManager.gameingSize(room) == 2 || seat.getGold() - openCard(seat, 2, 1) < openCard(seat, 4, 2)) {
				result.setOpenCard(true);
			} else {
				result.setCompareCard(true);
				result.setPositionList(getPositionList(room, seat));
			}
		}
		result.setLookCard(!seat.isSeeCard());
		RouteSession routeSession = seat.getRouteSession();
		routeSession.sendMessage(SHOW_OPERATE_VIEW, result);
		S2COperateOther s2cOperateOther = new S2COperateOther();
		s2cOperateOther.setPosition(seat.getPosition());
		s2cOperateOther.setSeatTime(20);
		roomManager.broadcast(room, OPERATE_VIEW_OTHER, s2cOperateOther);
		startScheduleSeat(seat);
	}

	/**
	 * 获取房间里可以比牌的人的位置
	 * @param room
	 * @return
	 */
	public List<Integer> getPositionList(Room room, Seat seat){
		List<Seat> seatList = room.getSeatList();
		List<Integer> list = new ArrayList<>();
		for(Seat pseat : seatList){
			if(null == pseat.getUser() || pseat.getPosition() == seat.getPosition()){
				continue;
			}
			if(pseat.isReady() && !pseat.isGiveUp()){
				list.add(pseat.getPosition());
			}
		}
		return list;
	}
	
	/**
	 * 判断玩家手中的金币能否加注
	 * @param seat
	 * @return
	 */
	public boolean getAddAnte(Seat seat) {
		Room room = seat.getRoom();
		int multiple = room.getSingleAnte() / room.getPlayWay().getAntes();
		if (multiple == 1) {
			return (seat.getGold() - openCardMaxAnte(seat, 6, 3)) >= openCardMaxAnte(seat, 12, 6);
		} else if (multiple == 3) {
			return (seat.getGold() - openCardMaxAnte(seat, 10, 5)) >= openCardMaxAnte(seat, 20, 10);
		} else if (multiple == 5) {
			return (seat.getGold() - openCardMaxAnte(seat, 16, 8)) >= openCardMaxAnte(seat, 32, 16);
		} else if (multiple == 8) {
			return (seat.getGold() - openCardMaxAnte(seat, 20, 10)) >= openCardMaxAnte(seat, 40, 20);
		} else {
			return false;
		}
	}

	/**
	 * 获取可以加的筹码数
	 * @param seat
	 * @return
	 */
	public List<AnteNums> getAnteNums(Seat seat) {
		Room room = seat.getRoom();
		List<AnteNums> anteNums = setAnteNums(room);
		if ((seat.getGold() - openCardMaxAnte(seat, 20, 10)) >= openCardMaxAnte(seat, 40, 20)) {
			return setCanShow(anteNums, room, 5);
		} else if ((seat.getGold() - openCardMaxAnte(seat, 16, 8)) >= openCardMaxAnte(seat, 32, 16)) {
			return setCanShow(anteNums, room, 4);
		} else if ((seat.getGold() - openCardMaxAnte(seat, 10, 5)) >= openCardMaxAnte(seat, 20, 10)) {
			return setCanShow(anteNums, room, 3);
		} else if ((seat.getGold() - openCardMaxAnte(seat, 10, 5)) >= openCardMaxAnte(seat, 20, 10)) {
			return setCanShow(anteNums, room, 2);
		}
		return anteNums;
	}

	/**
	 * 设置是否能看牌
	 * @param anteNums
	 * @return
	 */
	public List<AnteNums> setCanShow(List<AnteNums> anteNums, Room room, int size) {
		int multiple = room.getSingleAnte() / room.getPlayWay().getAntes();
		for (int i = 0; i < size; i++) {
			if (multiple * room.getPlayWay().getAntes() <= anteNums.get(i).getAnte()) {
				anteNums.get(i).setCanShow(true);
			}
		}
		return anteNums;
	}

	/**
	 * 返回筹码数值
	 * @param room
	 * @return
	 */
	public List<AnteNums> setAnteNums(Room room) {
		int antes = room.getPlayWay().getAntes();
		List<Integer> asList = Arrays.asList(1, 3, 5, 8, 10);
		List<AnteNums> list = new ArrayList<>();
		for (int i = 0; i < asList.size(); i++) {
			AnteNums anteNum = new AnteNums();
			anteNum.setAnte(antes * asList.get(i));
			list.add(anteNum);
		}
		return list;
	}

	/**
	 * 开牌时需要消耗的金币或应该下注的金币 根据参数做判断
	 * @param seat
	 * @return
	 */
	public int openCard(Seat seat, Integer numOne, Integer numTwo) {
		Room room = seat.getRoom();
		if (seat.isSeeCard()) {
			return room.getSingleAnte() * numOne;
		} else {
			return room.getSingleAnte() * numTwo;
		}
	}

	/**
	 * 判断最大单注时开牌时需要消耗的金币 或者 应该下注的金币
	 * @param seat
	 * @return
	 */
	public int openCardMaxAnte(Seat seat, Integer numOne, Integer numTwo) {
		Room room = seat.getRoom();
		if (seat.isSeeCard()) {
			return room.getPlayWay().getAntes() * numOne;
		} else {
			return room.getPlayWay().getAntes() * numTwo;
		}
	}

	/**
	 * 取消座位定时器
	 * @param room
	 * @return
	 */
	private boolean canecelScheduleSeat(Seat seat) {
		ScheduledFuture<?> scheduled = seat.getSchedule();
		if (scheduled != null) {
			seat.setSchedule(null);
			if (!scheduled.isCancelled()) {
				return scheduled.cancel(false);
			}
		}
		return false;
	}

	/**
	 * 洗牌
	 * @param room
	 */
	private void shuffle(Room room) {
		List<Integer> cardList = new LinkedList<Integer>();
		cardList.clear();
		for (int i = 2; i < 15; i++) {
			for (int j = 1; j < 5; j++) {
				cardList.add(j * 100 + i);
			}
		}
		List<Integer> cards = new LinkedList<Integer>();
		while (!cardList.isEmpty()) {
			int index = random.nextInt(cardList.size());
			Integer card = cardList.remove(index);
			cards.add(card);
		}
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			if (seat.isReady()) {
				for (int i = 0; i < 3; i++) {
					seat.getCardList().add(cards.remove(0));
				}
			}
		}
	}

	/**
	 * 将玩家手中的牌排序并分析牌型
	 * @param room
	 */
	public void seatCardShape(Room room) {
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			if (seat.getRouteSession() == null) {
				continue;
			}
			if (!seat.isReady()) {
				continue;
			}
			List<Integer> cardList = seat.getCardList();
			cardList.sort((o1, o2) -> {
				return o1 % 100 - o2 % 100;
			});
			seat.setCardList(cardList);
			CardShape cardShape = CardShapeUtils.getType(cardList);
			seat.setCardShape(cardShape);
		}
	}

	/**
	 * 离开房间
	 * @param seat
	 */
	public void leaveRoom(Seat seat, boolean isChangeTable) {
		canecelScheduleSeat(seat);
		Room room = seat.getRoom();
		if (seat.isReady()){
			if(room.isGameing() && !seat.isGiveUp()){
				giveUpCard(seat);
			}else{
				if(!colseRoomSchedule(seat)){
					return;
				}
			}
		}
		if(room.isGameing()){
			roomManager.updateSeatGold(seat);
		}
		roomManager.updateSeatInfo(seat);
		if(!isChangeTable){
			seat.getRouteSession().colse();
		}
		seat.setGold(0);
		seat.reset();
		seat.setReady(false);
		seat.setOnline(false);
		seat.setRouteSession(null);
		seat.setUser(null);
		seat.setCardShape(null);
		S2CLeaveRoom result = new S2CLeaveRoom();
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, LEAVE_ROOM, result);
		if (getUserSize(room) == 0) {
			// 进行解散房间
			doDissolveRoom(room);
		}
	}

	/**
	 * 关闭房间定时器
	 * @param seat
	 */
	private boolean colseRoomSchedule(Seat seat){
		Room room = seat.getRoom();
		if(getReadySize(seat) == 1){
			if(room.getScheduled() != null && !canecelSchedule(room)){
				seat.getRouteSession().colse();
				return false;
			}
			S2CCancelRoomSchedule result = new S2CCancelRoomSchedule();
			roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, result);
		}
		return true;
	}
	
	
	/**
	 * 解散房间
	 * @param room
	 */
	public void doDissolveRoom(Room room) {
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			if (seat.getUser() != null) {
				roomManager.updateSeatInfo(seat);
			}
		}
		roomManager.dissolveRoom(room);
	}

	/**
	 * 判断房间人数
	 * @return
	 */
	public int getUserSize(Room room) {
		List<Seat> seatList = room.getSeatList();
		int count = 0;
		for (Seat seat : seatList) {
			if (seat.getUser() != null && seat.isOnline()) {
				count = count + 1;
			}
		}
		return count;
	}

	/**
	 * 清除房间中离线的人
	 * @param leaveLines
	 */
	public void clearLeaveLine(Room room, List<Integer> leaveLines) {
		if (leaveLines.size() == 0) {
			return;
		}
		for (int i = 0; i < leaveLines.size(); i++) {
			Seat seat = roomManager.getSeat(room, leaveLines.get(i));
			leaveRoom(seat,false);
		}
	}

	/**
	 * 获取房间离线的人数
	 * @param room
	 * @return
	 */
	public List<Integer> getLeaveLineSize(Room room) {
		List<Seat> seatList = room.getSeatList();
		List<Integer> list = new ArrayList<>();
		for (Seat seat : seatList) {
			if (seat.getUser() != null && !seat.isOnline()) {
				list.add(seat.getPosition());
			}
		}
		return list;
	}

	/**
	 * 判断能否断线重连
	 * 
	 * @param session
	 */
	private boolean checkReconnetcion(RouteSession session) {
		Seat seat = roomManager.getSeat(session);
		if (null == seat) {
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 断线重连
	 * 
	 * @param session
	 * @param seat
	 */
	private void doReconnection(RouteSession session, Seat seat) {
		RouteSession routeSession = seat.getRouteSession();
		if (routeSession != null) {
			routeSession.attr(USER_KEY).set(null);
		}
		seat.setRouteSession(session);
		seat.setOnline(true);
		S2CReconnection result = new S2CReconnection();
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		List<S2CReconnection.UserInfos> userInfos = new ArrayList<>();
		result.setUserInfos(userInfos);
		result.setOutAntePosition(room.getPosition());
		result.setTotalAnte(room.getSingleAnte());
		result.setTotalChips(room.getTotalChips());
		result.setWinPosition(room.getWinPosition());
		result.setGameing(room.isGameing());
		for (Seat pseat : seatList) {
			User user = pseat.getUser();
			if (null == user) {
				continue;
			}
			RouteSession psession = pseat.getRouteSession();
			ChannelHandlerContext context = psession.getChannelHandlerContext();
			String ip = ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
			S2CReconnection.UserInfos userInfo = new S2CReconnection.UserInfos();
			if(pseat.getPosition() == seat.getPosition() && seat.isSeeCard()){
				userInfo.setCardList(seat.getCardList());
			}
			userInfo.setAnte(pseat.getTotalAnte());
			userInfo.setGiveUpCard(pseat.isGiveUp());
			userInfo.setGold(pseat.getGold());
			userInfo.setHeadImg(user.getHeadImg());
			userInfo.setName(user.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setIp(ip);
			userInfo.setReady(pseat.isReady());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSeeCard(pseat.isSeeCard());
			userInfo.setSex(user.getSex());
			userInfo.setUserId(user.getId());
			if(pseat.getSchedule() != null && pseat.getSchedule().getDelay(TimeUnit.SECONDS) > 0){
				userInfo.setSeatTime((int)pseat.getSchedule().getDelay(TimeUnit.SECONDS));
			}
			userInfos.add(userInfo);
		}
		result.setCode(200);
		session.sendMessage(RECONNECTION, result);
		for (Seat pseat : seatList) {
			RouteSession routes = pseat.getRouteSession();
			if (pseat.equals(seat) || null == routes) {
				continue;
			}
			S2CUpLine s2cUpLine = new S2CUpLine();
			s2cUpLine.setPosition(seat.getPosition());
			routes.sendMessage(UP_LINE, s2cUpLine);
		}
		if (seat.getPosition() == room.getPosition()) {
			sendOperate(seat);
		}
	}
	

	/**
	 * 向座位发送响应面板遇上方法相同（取消了定时器的设置）
	 * @param seat
	 */
	public void sendOperate(Seat seat){
		Room room = seat.getRoom();
		S2COperate result = new S2COperate();
		if (room.getSingleAnte() / room.getPlayWay().getAntes() != 10 && getAddAnte(seat)) {
			result.setAddAnte(true);
			result.setAddAnteNums(getAnteNums(seat));
		}
		if (seat.getGold() - openCard(seat, 2, 1) >= openCard(seat, 4, 2)) {
			result.setAnte(true);
		}
		if (room.getRoundNums() >= 2) {
			if (roomManager.gameingSize(room) == 2 || seat.getGold() - openCard(seat, 2, 1) < openCard(seat, 4, 2)) {
				result.setOpenCard(true);
			} else {
				result.setCompareCard(true);
				result.setPositionList(getPositionList(room, seat));
			}
		}
		result.setLookCard(!seat.isSeeCard());
		RouteSession routeSession = seat.getRouteSession();
		routeSession.sendMessage(SHOW_OPERATE_VIEW, result);
	}
	
	/**
	 * 换桌
	 * @param seat
	 */
	public void changeTable(Seat seat) {
		Room room = seat.getRoom();
		RouteSession routeSession = seat.getRouteSession();
		if (null == seat.getUser()) {
			return;
		}
		leaveRoom(seat, true);
		joinRoom(routeSession, room.getPlayWay(), room.getId());
	}
	
	
	/**
	 * 判断房间已经准备的人数
	 * @param seat
	 */
	private int getReadySize(Seat seat){
		Room room = seat.getRoom();
		int count = 0;
		List<Seat> seatList = room.getSeatList();
		for(Seat pseat : seatList){
			if(pseat.getUser() == null || seat.getPosition() == pseat.getPosition()){
				continue;
			}
			if(pseat.isReady()){
				count += 1;
			}
		}
		return count;
	}
	
	/**
	 * 将其他座位的定时器取消
	 * @param room
	 * @param positionList
	 */
	public void cancelSeatSchedule(Room room, List<Integer> positionList){
		for(int i = 0 ; i < positionList.size() ; i++){
			Seat seat = roomManager.getSeat(room, positionList.get(i));
			canecelScheduleSeat(seat);
		}
	}
	
	/**
	 * 请出房间没有准备的人
	 * @param room
	 */
	public void clearNotReady(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(seat.getUser() == null || seat.getRouteSession() == null){
				continue;
			}
			if(!seat.isReady()){
				S2CLeaveRoom result = new S2CLeaveRoom();
				result.setPosition(seat.getPosition());
				result.setCode(500);
				result.setMsg("游戏中没有准备，已离开房间");
				seat.getRouteSession().sendMessage(LEAVE_ROOM, result);
				leaveRoom(seat, false);
			}
		}
	}
	
	/**
	 * 系统自动比牌
	 * @param room
	 */
	public void systemAutoOpenCard(Room room){
		List<Integer> seatPositions = canComparePositions(room);
		seatPositions.sort((o1, o2) -> {
			if(compareCardShape(roomManager.getSeat(room, o1), roomManager.getSeat(room, o2))){
				return 1;
			}else{
				return -1;
			}
		});
		Integer winPosition = seatPositions.remove(seatPositions.size() - 1);
		S2COpenCard result = new S2COpenCard();
		List<Integer> winList = new ArrayList<>();
		winList.add(winPosition);
		result.setLosePositon(seatPositions);
		result.setTotalChips(room.getTotalChips());
		result.setWinPosition(winList);
		roomManager.broadcast(room, OPEN_CARD, result);
		seatPositions.add(winPosition);
		settle(seatPositions, roomManager.getSeat(room, winPosition));
	}
	
	/**
	 * 获取房间中所有可以比牌的人数
	 * @return
	 */
	private List<Integer> canComparePositions(Room room){
		List<Integer> positionList = new ArrayList<>();
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(seat.getUser() == null){
				continue;
			}
			if(seat.isReady() && !seat.isGiveUp()){
				positionList.add(seat.getPosition());
			}
		}
		return positionList;
	}
	
	
	
}
 