package com.starland.xyqp.niuniujb.business;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.starland.tools.network.RouteSession;
import com.starland.xyqp.common.component.GameServerComponent;
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
import com.starland.xyqp.niuniujb.c2s.C2SJoinRoom;
import com.starland.xyqp.niuniujb.consts.NiuniujbConstant;
import com.starland.xyqp.niuniujb.model.PlayWay;
import com.starland.xyqp.niuniujb.model.Room;
import com.starland.xyqp.niuniujb.model.Seat;

@Component
public class RoomManager {

	/**
	 * 所有房间的集合
	 */
	private Map<String, Room> roomMap = new ConcurrentHashMap<String, Room>();
	
	/**
	 * 用户所在座位的集合
	 */
	private Map<Integer, Seat> seatMap = new ConcurrentHashMap<Integer, Seat>();
	
	@Resource
	private UserService userService;
	
	@Resource
	private GameRoomService gameRoomService;
	
	@Resource
	private GameServerComponent gameServerComponent;
	
	@Resource
	private FightExploitsService fightExploitsService;
	
	@Resource
	private FightDetailService fightDetailService;
	
	private Random random = new Random();
	
	/**
	 * 创建房间
	 * @param playWay
	 * @return
	 */
	public Room createRoom(PlayWay playWay) {
		String id = randomRoomId();
		Room room = new Room(id, 5);
		room.setPlayWay(playWay);
		GameServer gameServer = gameServerComponent.getGameServer();
		GameRoom gameRoom = new GameRoom();
		gameRoom.setId(room.getId());
		gameRoom.setCreateTime(new Date());
		gameRoom.setCurrentPerson(0);
		gameRoom.setGameName(gameServer.getName());
		gameRoom.setInstead(0);
		gameRoom.setMaxPerson(3);
		gameRoom.setRoundCount(1);
		gameRoom.setScore(playWay.getBottomScore());
		gameRoom.setServerId(gameRoom.getServerId());
		gameRoomService.add(gameRoom);
		roomMap.put(id, room);
		return room;
	}
	
	public Seat findEmptySeat(PlayWay playWay) {
		Iterator<Room> iter = roomMap.values().iterator();
		while(iter.hasNext()) {
			Room room = iter.next();
			if (!room.getPlayWay().equals(playWay) || room.isGameing()) {
				continue;
			}
			List<Seat> seatList = room.getSeatList();
			for (Seat seat : seatList) {
				if (seat.getUser() == null) {
					return seat;
				}
			}
		}
		return null;
	}
	
	/**
	 * 加入到座位
	 * @param seat
	 * @param session
	 */
	public void joinSeat(Seat seat, RouteSession session) {
		User user = session.attr(NiuniujbConstant.USER_KEY).get();
		if (null == user) {
			throw new UselessRequestException("用户未登录！");
		}
		if(seatMap.get(user.getId()) != null){
			throw new UselessRequestException("此用户正在游戏中...");
		}
		if (null != seat.getUser()) {
			throw new UselessRequestException("已经有用户坐在座位上！");
		}
		Room room = seat.getRoom();
		String roomId = room.getId();
		userService.updateRoomId(user.getId(), roomId);
		seat.setUser(user);
		seat.setRouteSession(session);
		seat.setOnline(true);
		seat.setGold(user.getGold());
		seatMap.put(user.getId(), seat);
	}
	
	/**
	 * 离开房间
	 * @param seat
	 * @return
	 */
	public boolean leaveRoom(Seat seat) {
		User user = seat.getUser();
		if (null == user) {
			return false;
		}
		userService.updateRoomId(user.getId(), null);
		seat.setUser(null);
		seat.setRouteSession(null);
		seat.setOnline(false);
		seat.setReady(false);
		seatMap.remove(user.getId());
		return true;
	}
	
	/**
	 * 离开房间
	 * @param user
	 */
	public void leaveRoom(User user){
		userService.updateRoomId(user.getId(), null);
		seatMap.remove(user.getId());
	}
	
	/**
	 * 根据房间号获取房间
	 * @param roomId
	 * @return
	 */
	public Room getRoom(String roomId) {
		return roomMap.get(roomId);
	}
	
	/**
	 * 根据用户编号获取座位
	 * @param userId
	 * @return
	 */
	public Seat getSeat(Integer userId) {
		return seatMap.get(userId);
	}
	
	/**
	 * 根据session对象获取座位
	 * @param session
	 * @return
	 */
	public Seat getSeat(RouteSession session) {
		User user = session.attr(NiuniujbConstant.USER_KEY).get();
		if (null == user) {
			throw new UselessRequestException("用户未登录！");
		}
		return seatMap.get(user.getId());
	}
	
	/**
	 * 解散房间
	 * @param roomId
	 * @return
	 */
	public boolean dissolveRoom(String roomId) {
		Room room = roomMap.remove(roomId);
		if (null == room) {
			return false;
		}
		gameRoomService.delete(room.getId());
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			User user = seat.getUser();
			if (null == user) {
				continue;
			}
			userService.updateRoomId(user.getId(), null);
			Integer userId = user.getId();
			seatMap.remove(userId);
		}
		return true;
	}
	
	/**
	 * 随机获取一个房间编号
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
			if (gameRoom == null) {
				return roomId;
			}
		}
	}
	
	/**
	 * 座位是否都坐满了
	 * @param room
	 * @return
	 */
	public boolean isFull(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (seat.getUser() == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 上家的座位
	 * @param seat
	 * @return
	 */
	public Seat prevSeat(Seat seat) {
		Room room = seat.getRoom();
		int position = seat.getPosition() - 1;
		if (position <= 1) {
			position = 1;
		}
		return getSeat(room, position);
	}
	
	/**
	 * 下家的座位
	 * @param seat
	 * @return
	 */
	public Seat nextSeat(Seat seat) {
		Room room = seat.getRoom();
		int size = room.getSeatList().size();
		int position = seat.getPosition() + 1;
		if (position > size) {
			position = 1;
		}
		return getSeat(room, position);
	}
	
	/**
	 * 根据位置获取一个座位
	 * @param room
	 * @param position
	 * @return
	 */
	public Seat getSeat(Room room, int position) {
		List<Seat> seats = room.getSeatList();
		if (position <= 0 || position > seats.size()) {
			return null;
		}
		return seats.get(position - 1);
	}
	
	/**
	 * 广播消息
	 * @param room
	 * @param router
	 * @param msg
	 */
	public void broadcast(Room room, String router, Object msg) {
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
	 * 是否全部准备好了
	 * @param room
	 * @return
	 */
	public boolean isAllReady(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (!seat.isReady()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 计算已经准备的人数
	 * @return
	 */
	public int getReadySize(Room room){
		List<Seat> seatList = room.getSeatList();
		int count = 0;
		for(Seat seat : seatList){
			RouteSession routeSession = seat.getRouteSession();
			if(routeSession != null && seat.isReady()){
				count += 1;
			}
		}
		return  count;
	}
	
	/**
	 * 更改用户金币
	 * @param seat
	 * @param gold
	 */
	public void updateGold(Seat seat, int gold){
		Integer id = seat.getUser().getId();
		userService.updateConsumeGold(id, gold);
	}
	
	
	/**
	 * 获取用户对象
	 * @param token
	 * @return
	 */
	public User getUser(String token){
		return userService.getByToken(token);
	}
	
	/**
	 * 将用户从房间中移除
	 */
	public void updateUserInfo(Seat seat){
		User user = seat.getUser();
		Integer userId = user.getId();
		seatMap.remove(userId);
	}
	
	/**
	 * 解散房间
	 * @param room
	 */
	public void dissolveRoom(Room room){
		String roomId = room.getId();
		roomMap.remove(roomId);
		gameRoomService.delete(roomId);
	}
	
	/**
	 * 将战绩信息进行保存
	 * @param room
	 */
	public void saveSettle(Room room){
		GameServer gameServer = gameServerComponent.getGameServer();
		FightExploits fightExploits = new FightExploits();
		fightExploits.setCreateTime(new Date());
		fightExploits.setGameName(gameServer.getName());
		fightExploits.setGameType(gameServer.getGameType());
		fightExploits.setRoomId(room.getId());
		fightExploitsService.add(fightExploits);
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			User user = seat.getUser();
			if(user == null){
				continue;
			}
			FightDetail fightDetail = new FightDetail();
			fightDetail.setExploitsId(fightExploits.getId());
			fightDetail.setPosition(seat.getPosition());
			fightDetail.setScore(seat.getSettleInfo().getScore());
			fightDetail.setUserId(user.getId());
			fightDetail.setUserName(user.getName());
			fightDetailService.add(fightDetail);
		}
	}
	
	public boolean access(C2SJoinRoom param){
		switch (param.getAccessPoint()) {
		case 1000:
			return param.getBottomScore() == 50;
		case 2000:
			return param.getBottomScore() == 100;
		case 4000:
			return param.getBottomScore() == 200;
		case 10000:
			return param.getBottomScore() == 500;
		case 16000:
			return param.getBottomScore() == 800;
		case 20000:
			return param.getBottomScore() == 1000;
		default:
			return false;
		}
	}
	
}
