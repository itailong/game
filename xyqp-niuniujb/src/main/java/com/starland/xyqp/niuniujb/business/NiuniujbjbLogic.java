package com.starland.xyqp.niuniujb.business;
	
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
import com.starland.xyqp.niuniujb.c2s.C2SCaseAnte;
import com.starland.xyqp.niuniujb.c2s.C2SGrabBanker;
import com.starland.xyqp.niuniujb.c2s.C2SLoginByToken;
import com.starland.xyqp.niuniujb.consts.NiuniujbConstant;
import com.starland.xyqp.niuniujb.model.CardShape;
import com.starland.xyqp.niuniujb.model.PlayWay;
import com.starland.xyqp.niuniujb.model.Room;
import com.starland.xyqp.niuniujb.model.Seat;
import com.starland.xyqp.niuniujb.model.SettleInfo;
import com.starland.xyqp.niuniujb.s2c.S2CAllocateCard;
import com.starland.xyqp.niuniujb.s2c.S2CBanker;
import com.starland.xyqp.niuniujb.s2c.S2CCancelSchedule;
import com.starland.xyqp.niuniujb.s2c.S2CCaseAnte;
import com.starland.xyqp.niuniujb.s2c.S2CCaseAntes;
import com.starland.xyqp.niuniujb.s2c.S2CGrabBanker;
import com.starland.xyqp.niuniujb.s2c.S2CJoinRoom;
import com.starland.xyqp.niuniujb.s2c.S2CJoinRoomOther;
import com.starland.xyqp.niuniujb.s2c.S2CLeaveRoom;
import com.starland.xyqp.niuniujb.s2c.S2CLoginByToken;
import com.starland.xyqp.niuniujb.s2c.S2COpenCard;
import com.starland.xyqp.niuniujb.s2c.S2COpenCardView;
import com.starland.xyqp.niuniujb.s2c.S2CReady;
import com.starland.xyqp.niuniujb.s2c.S2CReconnection;
import com.starland.xyqp.niuniujb.s2c.S2CSettleInfo;
import com.starland.xyqp.niuniujb.s2c.S2CStartRoomSchedule;
import com.starland.xyqp.niuniujb.s2c.S2CUpLine;

import io.netty.channel.ChannelHandlerContext;
	
@Component
public class NiuniujbjbLogic implements NiuniujbConstant {
	
	@Resource
	private RoomManager roomManager;
	
	private Random random = new Random();
	
	@Resource
	private ScheduledExecutorService scheduledExecutor;
	
	/**
	 * 通过token登录
	 * @param session
	 * @param param
	 */
	public void loginByToken(RouteSession session, C2SLoginByToken params){
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
		session.attr(USER_KEY).set(user);
		result.setReconnection(checkReconnection(session));
		session.sendMessage(LOGIN_BY_TOKEN, result);
		if(checkReconnection(session)){
			Seat seat = roomManager.getSeat(session);
			doReconnection(session, seat);
		}
	}
	
	/**
	 * 判断是否需要断线重连
	 * @param session
	 * @return
	 */
	public boolean checkReconnection(RouteSession session){
		Seat seat = roomManager.getSeat(session);
		if(null == seat){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 加入房间
	 * @param session
	 * @param roomId
	 */
	public void joinRoom(RouteSession session, PlayWay playWay) {
		Seat seat = null;
		synchronized (roomManager) {
			seat = roomManager.findEmptySeat(playWay);
			if (null == seat) {
				Room room = roomManager.createRoom(playWay);
				seat = room.getSeatList().get(0);
			}
			roomManager.joinSeat(seat, session);
		}
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		S2CJoinRoom result = new S2CJoinRoom();
		result.setCode(200);
		result.setRoomId(room.getId());
		result.setSeatPosition(seat.getPosition());
		List<S2CJoinRoom.UserInfo> userInfoList = new ArrayList<>();
		result.setUserInfos(userInfoList);
		if(room.getScheduled() != null && room.getScheduled().getDelay(TimeUnit.SECONDS) > 0){
			result.setTime((int)room.getScheduled().getDelay(TimeUnit.SECONDS));
		}
		for (Seat pseat : seatList) {
			User puser = pseat.getUser();
			if (null == puser) {
				continue;
			}
			RouteSession routeSession = pseat.getRouteSession();
			ChannelHandlerContext pcontext = routeSession.getChannelHandlerContext();
			String pip = ((InetSocketAddress) pcontext.channel().remoteAddress()).getAddress().getHostAddress();
			S2CJoinRoom.UserInfo userInfo = new S2CJoinRoom.UserInfo();
			userInfo.setOnline(true);
			userInfo.setReady(pseat.isReady());
			userInfo.setIp(pip);
			userInfo.setGold(puser.getGold());
			userInfo.setHeadImg(puser.getHeadImg());
			userInfo.setName(puser.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSex(puser.getSex());
			userInfo.setUserId(puser.getId());
			userInfoList.add(userInfo);
		}
		session.sendMessage(JOIN_ROOM, result);
		User user = seat.getUser();
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
		s2cJoinRoomOther.setGold(user.getGold());
		s2cJoinRoomOther.setOnline(true);
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
		//进行准备
		ready(seat);
	}
	
	/**
	 * 离开房间
	 * @param session
	 */
	public void leaveRoom(RouteSession session) {
		Seat seat = roomManager.getSeat(session);
		Room room = seat.getRoom();
		if (room.isGameing()) {
			session.colse();
		} else {
			if (!whetherCancelSuccess(seat)) {
				return;
			}
			session.colse();
			User user = seat.getUser();
			roomManager.leaveRoom(user);
			seat.setCardShape(null);
			seat.setCaseAnte(0);
			seat.setGold(0);
			seat.setGrabBanker(0);
			seat.setOnline(false);
			seat.setOpenCard(false);
			seat.setRouteSession(null);
			seat.setUser(null);
			seat.setReady(false);
			S2CLeaveRoom result = new S2CLeaveRoom();
			result.setPosition(seat.getPosition());
			roomManager.broadcast(room, LEAVE_ROOM, result);
			if (getOnlineSize(room) == 0) {
				doDissolveRoom(room);
			}
		}
	}
	
	/**
	 * 判断是否取消成功
	 * @param seat
	 * @return
	 */
	private boolean whetherCancelSuccess(Seat seat){
		Room room = seat.getRoom();
		if(seat.isReady()){
			if(getReadySize(seat) == 1){
				if(!cancelSchedule(room)){
					return false;
				}
				S2CCancelSchedule result = new S2CCancelSchedule();
				roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, result);
			}
		}
		return true;
	}
	
	/**
	 * 判断房间已经准备的人数
	 * @param seat
	 */
	public int getReadySize(Seat seat){
		Room room = seat.getRoom();
		int count = 0;
		List<Seat> seatList = room.getSeatList();
		for(Seat pseat : seatList){
			if(pseat.getUser() == null || pseat.getPosition() == seat.getPosition()){
				continue;
			}
			if(pseat.isReady()){
				count += 1;
			}
		}
		return count;
	}
	
	/**
	 * 单纯的离开房间
	 * @param session
	 */
	public void leaveRooms(RouteSession session){
		Seat seat = roomManager.getSeat(session);
		Room room = seat.getRoom();
		User user = seat.getUser();
		roomManager.leaveRoom(user);
		seat.setCardShape(null);
		seat.setCaseAnte(0);
		seat.setGold(0);
		seat.setGrabBanker(0);
		seat.setOnline(false);
		seat.setOpenCard(false);
		seat.setRouteSession(null);
		seat.setUser(null);
		S2CLeaveRoom result = new S2CLeaveRoom();
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, LEAVE_ROOM, result);
		if(getOnlineSize(room) == 0){
			doDissolveRoom(room);
		}
	}
	
	/**
	 * 离线
	 * @param session
	 */
	public void leaveLine(RouteSession session) {
		Seat seat = roomManager.getSeat(session);
		seat.setOnline(false);
		Room room = seat.getRoom();
		if(!room.isGameing()){
			leaveRoom(session);
			return;
		}
		S2CLeaveRoom s2cLeaveRoom = new S2CLeaveRoom();
		s2cLeaveRoom.setPosition(seat.getPosition());
		roomManager.broadcast(room, LEAVE_LINE, s2cLeaveRoom);
	}
	
	/**
	 * 上线
	 * @param session
	 */
	public void upLine(RouteSession session) {
		Seat seat = roomManager.getSeat(session);
		seat.setRouteSession(session);
		seat.setOnline(true);
		Room room = seat.getRoom();
		S2CUpLine s2cUpLine = new S2CUpLine();
		s2cUpLine.setPosition(seat.getPosition());
		roomManager.broadcast(room, LEAVE_LINE, s2cUpLine);
	}
	
	/**
	 * 准备
	 * @param seat
	 */
	public void ready(Seat seat) {
		Room room = seat.getRoom();
		if(seat.getGold() < room.getPlayWay().getAccessPoint()){
			RouteSession routeSession = seat.getRouteSession();
			S2CReady result = new S2CReady();
			result.setCode(500);
			result.setMsg("金币不足请充值");
			routeSession.sendMessage(READY, result);
			leaveRoom(routeSession);
			return;
		}
		seat.setReady(true);
		S2CReady s2cReady = new S2CReady();
		s2cReady.setPosition(seat.getPosition());
		roomManager.broadcast(room, READY, s2cReady);
		if(roomManager.getReadySize(room) == 5){
			if(!cancelSchedule(room)){
				return;
			}
			S2CCancelSchedule result = new S2CCancelSchedule();
			roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, result);
			startGame(room);
		}
		if(roomManager.getReadySize(room) == 2){
			startSchedule(room);
			S2CStartRoomSchedule result = new S2CStartRoomSchedule();
			result.setTime(10);
			roomManager.broadcast(room, START_ROOM_SCHEDULE, result);
		}
	}
	
	/**
	 * 启动房间定时器
	 * @param room
	 */
	public void startSchedule(Room room){
		cancelSchedule(room);
		ScheduledFuture<?> schedule = scheduledExecutor.schedule(() -> {
			room.setScheduled(null);
			removeNotReady(room);
			startGame(room);
		}, 10000, TimeUnit.MILLISECONDS);
		room.setScheduled(schedule);
	}
	
	/**
	 * 取消房间定时器
	 * @param room
	 */
	public boolean cancelSchedule(Room room){
		ScheduledFuture<?> scheduled = room.getScheduled();
		if(scheduled != null){
			room.setScheduled(null);
			if(!scheduled.isCancelled()){
				return scheduled.cancel(false);
			}
		}
		return false;
	}
	
	/**
	 * 开始游戏
	 * @param room
	 */
	public void startGame(Room room){
		room.reset();
		S2CBanker result = new S2CBanker();
		roomManager.broadcast(room, GRAB_BANKER_VIEW, result);
		startGrabBankerScheduled(room);
		S2CStartRoomSchedule results = new S2CStartRoomSchedule();
		results.setTime(20);
		roomManager.broadcast(room, START_ROOM_SCHEDULE, results);
	}
	
	/**
	 * 抢庄计时器
	 * @param room
	 */
	private void startGrabBankerScheduled(Room room){
		cancelSchedule(room);
		ScheduledFuture<?> schedule = scheduledExecutor.schedule(() -> {
			sendBankerPosition(room);
		}, 20000, TimeUnit.MILLISECONDS);
		room.setScheduled(schedule);
	}
	
	/**
	 * 抢庄
	 * @param seat
	 * @param position
	 */
	public void grabBanker(Seat seat, C2SGrabBanker param){
		Room room = seat.getRoom();
		seat.setGrabBanker(param.getGrabBanker());
		S2CGrabBanker result = new S2CGrabBanker();
		if(param.getGrabBanker() == 1){
			result.setGrabBanker(true);
		}else{
			result.setGrabBanker(false);
		}
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, GRAB_BANKER, result);
		if(allOperate(room)){
			S2CCancelSchedule results = new S2CCancelSchedule();
			roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, results);
			if(cancelSchedule(room)){
				sendBankerPosition(room);
			}
		}
	}
	
	/**
	 * 将没有进行操作的设置为不抢庄
	 * @param room
	 */
	public void setGrabBanker(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getUser()){
				continue;
			}
			if(seat.getGrabBanker() == 0){
				seat.setGrabBanker(2);
			}
		}
	}
	
	/**
	 * 获取庄家的位置
	 * @param room
	 * @return
	 */
	private int getBankerPosition(Room room) {
		List<Integer> grabBankerList = new ArrayList<>();
		List<Integer> notGrabList = new ArrayList<>();
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getUser()){
				continue;
			}
			if(seat.getGrabBanker() == 1){
				grabBankerList.add(seat.getPosition());
			}
			if(seat.getGrabBanker() == 2){
				notGrabList.add(seat.getPosition());
			}
		}
		if(grabBankerList.size() == 0){
			int index = random.nextInt(notGrabList.size());
			return notGrabList.get(index);
		}else{
			int banker = random.nextInt(grabBankerList.size());
			return grabBankerList.get(banker);
		}
	}
	
	/**
	 * 发送庄家的位置
	 * @param position
	 */
	public void sendBankerPosition(Room room){
		setGrabBanker(room);
		int position = getBankerPosition(room);
		room.setBankerPosition(position);
		if(position <= 0 || position > 5 || null == roomManager.getSeat(room, position).getUser()){
			throw new UselessRequestException("位置不正确");
		}
		S2CBanker result = new S2CBanker();
		result.setPosition(position);
		roomManager.broadcast(room, BANKER_POSITIOIN, result);
		allocateCard(room);
	}
	
	/**
	 * 向闲家响应下注面板
	 * @param room
	 */
	public void sendCaseAnte(Room room){
		List<Integer> anteList = getAnteList(room);
		if(anteList == null){
			throw new UselessRequestException("下注集合为空");
		}
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			RouteSession routeSession = seat.getRouteSession();
			if(null == routeSession || seat.getPosition() == room.getBankerPosition()){
				continue;
			}
			S2CCaseAnte result = new S2CCaseAnte();
			result.setCaseAntes(anteList);
			routeSession.sendMessage(CASE_ANTE_VIEW, result);
		}
		S2CStartRoomSchedule results = new S2CStartRoomSchedule();
		results.setTime(20);
		roomManager.broadcast(room, START_ROOM_SCHEDULE, results);
		startCaseAnteScheduled(room);
	}

	/**
	 * 获取房间人数
	 * @return
	 */
	private int moreThanTotalAnte(Room room){
		List<Seat> seatList = room.getSeatList();
		int bankerPosition = room.getBankerPosition();
		int count = 0;
		for(Seat pseat : seatList){
			if(pseat.getUser() == null || pseat.getPosition() == bankerPosition){
				continue;
			}
			count += 1;
		}
		return count;
	}
	
	/**
	 * 获玩家下注的集合
	 * @param room
	 * @return
	 */
	private List<Integer> getAnteList(Room room){
		int bankerPosition = room.getBankerPosition();
		Seat seat = roomManager.getSeat(room, bankerPosition);
		int bottomScore = room.getPlayWay().getBottomScore();
		int count = moreThanTotalAnte(room);
		int ante = bottomScore * 4 * count;
		if(seat.getGold() >= ante * 4){
			return Arrays.asList(bottomScore, bottomScore * 2, bottomScore * 3, bottomScore * 4);
		}else if(seat.getGold() >= ante * 3){
			return Arrays.asList(bottomScore, bottomScore * 2, bottomScore * 3);
		}else if(seat.getGold() >= ante * 2){
			return Arrays.asList(bottomScore, bottomScore * 2);
		}else if(seat.getGold() >= ante){
			return Arrays.asList(bottomScore);
		}else{
			return null;
		}
 	}
	
	/**
	 * 下注 
	 * @param seat
	 * @param param
	 */
	public void caseAnte(Seat seat, C2SCaseAnte param){
		Room room = seat.getRoom();
		int bottomScore = room.getPlayWay().getBottomScore();
		int multiple = param.getCaseAnte() / bottomScore;
		if(multiple != 1 && multiple != 2 && multiple != 3 && multiple != 4){
			throw new UselessRequestException("筹码数不正确");
		}
		seat.setCaseAnte(param.getCaseAnte());
		S2CCaseAntes result = new S2CCaseAntes();
		result.setGold(seat.getGold() - param.getCaseAnte());
		result.setCaseAnte(param.getCaseAnte());
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, CASE_ANTE, result);
		if(allCaseAnte(room)){
			S2CCancelSchedule results = new S2CCancelSchedule();
			roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, results);
			if(cancelSchedule(room)){
				sendOpenCardView(room);
			}
		}
	}
	
	/**
	 * 当计时器到时判断是否有没有进行下注的
	 * @param room
	 */
	private void sendNotCaseAnte(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getRouteSession() || seat.getPosition() == room.getBankerPosition() || seat.getCaseAnte() != 0){
				continue;
			}
			seat.setCaseAnte(room.getPlayWay().getBottomScore());
			S2CCaseAntes result = new S2CCaseAntes();
			result.setCaseAnte(seat.getCaseAnte());
			result.setGold(seat.getGold() - seat.getCaseAnte());
			result.setPosition(seat.getPosition());
			roomManager.broadcast(room, CASE_ANTE, result);
		}
	}
	
	/**
	 * 启动下注定时器
	 * @param room
	 */
	private void startCaseAnteScheduled(Room room){
		cancelSchedule(room);
		ScheduledFuture<?> schedule = scheduledExecutor.schedule(() -> {
			sendNotCaseAnte(room);
			sendOpenCardView(room);
		}, 20000, TimeUnit.MILLISECONDS);
		room.setScheduled(schedule);
	}
	
	/**
	 * 判断是否都已经下注
	 * @param room
	 * @return
	 */
	public boolean allCaseAnte(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getRouteSession() || seat.getPosition() == room.getBankerPosition()){
				continue;
			}
			if(seat.getCaseAnte() == 0){
				return false;
			}
		}
		return true;
	}

	/**
	 * 发送开牌面板
	 * @param room
	 */
	private void sendOpenCardView(Room room){
		S2COpenCardView result = new S2COpenCardView();
		roomManager.broadcast(room, OPEN_CARD_VIEW, result);
		S2CStartRoomSchedule results = new S2CStartRoomSchedule();
		results.setTime(20);
		roomManager.broadcast(room, START_ROOM_SCHEDULE, results);
		startOpenCardSheduled(room);
	}
	
	
	/**
	 * 判断是否都已经进行操作
	 * @param room
	 * @return
	 */
	private boolean allOperate(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getUser()){
				continue;
			}
			if(seat.getGrabBanker() == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 发牌
	 * @param room
	 */
	public void allocateCard(Room room){
		shuffle(room);
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			RouteSession session = seat.getRouteSession();
			if(null == session){
				continue;
			}
			S2CAllocateCard result = new S2CAllocateCard();
			result.setCardList(seat.getCardList());
			session.sendMessage(ALLOCATE_CARD, result);
		}
		sendCaseAnte(room);
	}
	
	/**
	 * 洗牌
	 * @param room
	 */
	private void shuffle(Room room) {
		List<Integer> cardList = new LinkedList<>();
		cardList.clear();
		for (int i = 1; i < 14; i++) {
			for (int j = 1; j < 5; j++) {
				cardList.add(j * 100 + i);
			}
		}
		List<Integer> cards = new LinkedList<>();
		while (!cardList.isEmpty()) {
			int index = random.nextInt(cardList.size());
			Integer card = cardList.remove(index);
			cards.add(card);
		}
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			if (null == seat.getUser()) {
				continue;
			}
			for (int i = 0; i < 5; i++) {
				seat.getCardList().add(cards.remove(0));
			}
		}
	}
	
	/**
	 * 启动开牌定时器
	 * @param room
	 */
	private void startOpenCardSheduled(Room room){
		cancelSchedule(room);
		ScheduledFuture<?> schedule = scheduledExecutor.schedule(() -> {
			List<Seat> seatList = room.getSeatList();
			for(Seat seat : seatList){
				if(null == seat.getUser()){
					continue;
				}
				if(!seat.isOpenCard()){
					setNotOpenCardSeat(seat);
				}
			}
			sendSettleInfo(room);
		}, 20000, TimeUnit.MILLISECONDS);
		room.setScheduled(schedule);
	}
	
	/**
	 * 将没有开牌的进行开牌
	 * @param room
	 */
	private void setNotOpenCardSeat(Seat seat){
		Room room = seat.getRoom();
		seat.setCardShape(CardShapeUtils.getCardShape(seat.getCardList()));
		seat.setOpenCard(true);
		S2COpenCard result = new S2COpenCard();
		result.setCardList(seat.getCardShape().getCardList());
		result.setType(seat.getCardShape().getType());
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, OPEN_CARD, result);
	}
	
	/**
	 * 开牌
	 * @param seat
	 */
	public void openCard(Seat seat){
		if(null == seat.getUser()){
			throw new UselessRequestException("座位为空");
		}
		seat.setCardShape(CardShapeUtils.getCardShape(seat.getCardList()));
		Room room = seat.getRoom();
		seat.setOpenCard(true);
		S2COpenCard result = new S2COpenCard();
		result.setCardList(seat.getCardShape().getCardList());
		result.setType(seat.getCardShape().getType());
		result.setPosition(seat.getPosition());
		roomManager.broadcast(room, OPEN_CARD, result);
		if(allOpenCard(room)){
			S2CCancelSchedule results = new S2CCancelSchedule();
			roomManager.broadcast(room, CANCEL_ROOM_SCHEDULE, results);
			if(cancelSchedule(room)){
				sendSettleInfo(room);
			}
		}
	}
	
	/**
	 * 判断是否已经都开牌
	 * @param room
	 * @return
	 */
	private boolean allOpenCard(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getUser()){
				continue;
			}
			if(!seat.isOpenCard()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 进行结算并发送结算消息
	 * @param room
	 */
	public void sendSettleInfo(Room room){
		room.setGameing(false);
		room.setSettling(true);
		saveSettleInfo(room);
		List<Seat> seatList = room.getSeatList();
		S2CSettleInfo result = new S2CSettleInfo();
		List<S2CSettleInfo.SettleInfo> settleInfos = new ArrayList<>();
		result.setSettleInfos(settleInfos);
		for(Seat seat : seatList){
			User user = seat.getUser();
			if(null == user){
				continue;
			}
			S2CSettleInfo.SettleInfo settleInfo = new S2CSettleInfo.SettleInfo();
			settleInfo.setChangGold(seat.getSettleInfo().getScore());
			settleInfo.setGold(seat.getGold());
			settleInfo.setName(user.getName());
			settleInfo.setType(seat.getCardShape().getType());
			settleInfo.setWin(seat.getSettleInfo().isWin());
			settleInfo.setPosition(seat.getPosition());
			if(seat.getPosition() == room.getBankerPosition()){
				settleInfo.setBanker(true);
			}
			settleInfos.add(settleInfo);
		}
		roomManager.broadcast(room, SETTLE, result);
		for(Seat pseat : seatList){
			if(null == pseat.getUser()){
				continue;
			}
			if(!pseat.isOnline()){
				leaveRoom(pseat.getRouteSession());
			}else{
				pseat.setReady(false);
			}
		}
		if(getOnlineSize(room) == 0){
			doDissolveRoom(room);
		}
	}
	
	/**
	 * 进行结算
	 * @param seat
	 */
	private void saveSettleInfo(Room room){
		List<Seat> seatList = room.getSeatList();
		int bankerPosition = room.getBankerPosition();
		Seat seat = roomManager.getSeat(room, bankerPosition);
		for(Seat pseat : seatList) {
			User user = pseat.getUser();
			if(null == user || pseat.getPosition() == seat.getPosition() || !pseat.isReady()){
				continue;
			}
			if(compareCardShape(pseat)){
				SettleInfo settleInfo = pseat.getSettleInfo();
				settleInfo.setScore(settleInfo.getScore() + pseat.getCardShape().getMultiple() * pseat.getCaseAnte());
				pseat.setGold(pseat.getGold() + settleInfo.getScore());
				roomManager.updateGold(pseat, -settleInfo.getScore());
				SettleInfo bankerInfo = seat.getSettleInfo();
				bankerInfo.setScore(bankerInfo.getScore() - settleInfo.getScore());
				bankerInfo.setWin(false);
			}else{
				SettleInfo settleInfo = pseat.getSettleInfo();
				settleInfo.setScore(settleInfo.getScore() - seat.getCardShape().getMultiple() * pseat.getCaseAnte());
				pseat.setGold(pseat.getGold() + settleInfo.getScore());
				settleInfo.setWin(false);
				roomManager.updateGold(pseat, -settleInfo.getScore());
				SettleInfo bankerInfo = seat.getSettleInfo();
				bankerInfo.setScore(bankerInfo.getScore() - settleInfo.getScore());
			}
		}
		seat.setGold(seat.getGold() + seat.getSettleInfo().getScore());
		roomManager.updateGold(seat, -seat.getSettleInfo().getScore());
		roomManager.saveSettle(room);
	}
	
	/**
	 * 对牌进行比较
	 * @param seat
	 * @param bankerSeat
	 * @return
	 */
	public boolean compareCardShape(Seat seat){
		Room room = seat.getRoom();
		Seat bankerSeat = roomManager.getSeat(room, room.getBankerPosition());
		CardShape seatShape = seat.getCardShape();
		CardShape bankerShape = bankerSeat.getCardShape();
		if(seatShape.getType() > bankerShape.getType()){
			return true;
		}else if(seatShape.getType() == bankerShape.getType()){
			if(seatShape.getMaxCard() % 100 > bankerShape.getMaxCard() % 100){
				return true;
			}else if(seatShape.getMaxCard() % 100 == bankerShape.getMaxCard() % 100){
				if(seatShape.getMaxCard() / 100 > bankerShape.getMaxCard() / 100){
					return true; 
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{ 
			return false;
		}
	}
	
	/**
	 * 断线重连
	 * @param session
	 */
	public void doReconnection(RouteSession session, Seat seat){
		RouteSession routeSession = seat.getRouteSession();
		if(routeSession != null){
			routeSession.attr(USER_KEY).set(null);
		}
		Room room = seat.getRoom();
		seat.setRouteSession(session);
		seat.setOnline(true);
		S2CReconnection result = new S2CReconnection();
		List<S2CReconnection.UserInfo> userInfos = new ArrayList<>();
		if(room.getScheduled() != null && room.getScheduled().getDelay(TimeUnit.SECONDS) > 0){
			result.setTime((int)room.getScheduled().getDelay(TimeUnit.SECONDS));
		}
		result.setGameing(room.isGameing());
		result.setUserInfos(userInfos);
		result.setBankerPosition(room.getBankerPosition());
		List<Seat> seatList = room.getSeatList();
		for(Seat pseat : seatList){
			User user = pseat.getUser();
			if(null == user){
				continue;
			}
			S2CReconnection.UserInfo userInfo = new S2CReconnection.UserInfo();
			userInfo.setReady(pseat.isReady());
			userInfo.setCardList(pseat.getCardList());
			userInfo.setCaseAnte(pseat.getCaseAnte());
			userInfo.setGold(pseat.getGold());
			userInfo.setHeadImg(user.getHeadImg());
			userInfo.setName(user.getName());
			userInfo.setOnline(pseat.isOnline());
			userInfo.setSeatPosition(pseat.getPosition());
			userInfo.setSex(user.getSex());
			if(pseat.getCardShape() != null){
				userInfo.setType(pseat.getCardShape().getType());
				userInfo.setOpenCardList(pseat.getCardShape().getCardList());
			}
			userInfo.setOpenCard(pseat.isOpenCard());
			userInfo.setUserId(user.getId());
			userInfos.add(userInfo);
		}
		result.setCode(200);
		session.sendMessage(RECONNECTION, result);
		if(room.getScheduled() != null && room.getScheduled().getDelay(TimeUnit.SECONDS) > 0){
			S2CStartRoomSchedule scheduled = new S2CStartRoomSchedule();
			scheduled.setTime((int)room.getScheduled().getDelay(TimeUnit.SECONDS));
		}
		if(seat.getGrabBanker() == 0 && room.isGameing()){
			S2CBanker results = new S2CBanker();
			session.sendMessage(GRAB_BANKER_VIEW, results);
			return;
		}
		if(seat.getPosition() != room.getBankerPosition() && seat.getCaseAnte() == 0 && room.isGameing()){
			S2CCaseAnte resultss = new S2CCaseAnte();
			resultss.setCaseAntes(getAnteList(room));
			session.sendMessage(CASE_ANTE_VIEW, resultss);
			return;
		}
		if(!seat.isOpenCard() && seat.getCardShape() != null){
			S2COpenCardView results = new S2COpenCardView();
			session.sendMessage(OPEN_CARD_VIEW, results);
			return;
		}
	}
	
	/**
	 * 解散房间
	 * @param room
	 */
	public void doDissolveRoom(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(seat.getUser() != null){
				roomManager.updateUserInfo(seat);
			}
		}
		roomManager.dissolveRoom(room);
	}
	
	
	/**
	 * 判断房间人数
	 * @param room
	 * @return
	 */
	public int getOnlineSize(Room room){
		List<Seat> seatList = room.getSeatList();
		int count = 0;
		for(Seat seat : seatList){
			if(seat.getUser() == null){
				continue;
			}
			if(seat.isOnline()){
				count += 1;
			}
		}
		return count;
	}
	
	/**
	 * 将没有准备的人移除房间
	 * 
	 */
	public void removeNotReady(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(seat.getUser() == null){
				continue;
			}
			if(!seat.isReady()){
				leaveRooms(seat.getRouteSession());
			}
		}
	}
}
