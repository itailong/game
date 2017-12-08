package com.starland.xyqp.zjhjb.business;

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
import com.starland.xyqp.zjhjb.c2s.C2SJoinRoom;
import com.starland.xyqp.zjhjb.consts.ZjhConstant;
import com.starland.xyqp.zjhjb.model.PlayWay;
import com.starland.xyqp.zjhjb.model.Room;
import com.starland.xyqp.zjhjb.model.Seat;

@Component
public class RoomManager {
	
	/**
	 * 所有在房间的集合
	 */
	private Map<String, Room> roomMap = new ConcurrentHashMap<>();
	
	/**
	 * 用户所在座位的集合
	 */
	private Map<Integer, Seat> seatMap = new ConcurrentHashMap<>();
	
	@Resource
	private UserService userService;
	
	@Resource
	private GameRoomService gameRoomService;
	
	@Resource
	private GameServerComponent gameServiceComponent;
	
	@Resource
	private FightExploitsService fightExploitsService;
	
	@Resource
	private FightDetailService fightDetailService;
	
	private Random random = new Random();
	
	
	public User getUser(String token){
		if(null == token || token == ""){
			throw new UselessRequestException("token为空");
		}
		return userService.getByToken(token);
	}
	
	
	/**
	 * 查找一个空座位
	 * @param playWay
	 * @return
	 */
	public Seat findEmptySeat(PlayWay playWay, String roomId){
		Iterator<Room> iter = roomMap.values().iterator();
		while(iter.hasNext()){
			Room room = iter.next();
			if(!room.getPlayWay().equals(playWay) || room.getId() == roomId){
				continue;
			}
			List<Seat> seatList = room.getSeatList();
			for(Seat seat : seatList){
				User user = seat.getUser();
				if(null == user){
					return seat;
				}
			}
		}
		return null;
	}
	
	/**
	 * 创建房间
	 * @param playWay
	 * @return
	 */
	public Room createRoom(PlayWay playWay){
		String roomId = randomRoomId();
		Room room = new Room(roomId, 5);
		room.setPlayWay(playWay);
		GameServer gameServer = gameServiceComponent.getGameServer();
		GameRoom gameRoom = new GameRoom();
		gameRoom.setId(roomId);
		gameRoom.setCreateTime(new Date());
		gameRoom.setCreatorId(0);
		gameRoom.setCurrentPerson(0);
		gameRoom.setGameName(gameServer.getName());
		gameRoom.setInstead(0);
		gameRoom.setMaxPerson(5);
		gameRoom.setRoundCount(0);
		gameRoom.setScore(playWay.getAntes());
		gameRoom.setServerId(gameServer.getId());
		gameRoomService.add(gameRoom);
		roomMap.put(roomId, room);
		return room;
	}
	
	/**
	 * 生成房间号的工具方法
	 * @return
	 */
	private String randomRoomId(){
		while(true){
			StringBuilder bud = new StringBuilder();
			for(int i = 0 ; i < 6 ; i++){
				int next = random.nextInt(10);
				bud.append(next);
			}
			String roomId = bud.toString();
			GameRoom gameRoom = gameRoomService.get(roomId);
			if(null == gameRoom){
				return roomId;
			}
		}
	}
	
	/**
	 * 加入到座位
	 * @param seat
	 * @param session
	 */
	public void joinSeat(Seat seat, RouteSession session){
		User user = session.attr(ZjhConstant.USER_KEY).get();
		if(null == user){
			throw new UselessRequestException("用户未登录");
		}
		if(seatMap.get(user.getId()) != null){
			throw new UselessRequestException("此用户已经在游戏中");
		}
		if(seat.getUser() != null){
			throw new UselessRequestException("已经有用户在此座位上...");
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
	 * 根据session获取座位对象
	 * @param session
	 * @return
	 */
	public Seat getSeat(RouteSession session){
		User user = session.attr(ZjhConstant.USER_KEY).get();
		if(null == user){
			throw new UselessRequestException("用户未登录...");
		}
		return seatMap.get(user.getId());
	}
	
	/**
	 * 消息广播
	 * @param room
	 * @param router
	 * @param msg
	 */
	public void broadcast(Room room, String router, Object msg){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			RouteSession routeSession = seat.getRouteSession();
			if(null == routeSession){
				continue;
			}
			routeSession.sendMessage(router, msg);
		}
	}
	
	/**
	 * 判断是否已经都准备
	 * @param room
	 * @return
	 */
	public boolean isAllReady(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(null == seat.getUser() || !seat.isReady()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 准备的人数
	 * @param room
	 * @return
	 */
	public int readyNum(Room room){
		List<Seat> seatList = room.getSeatList();
		int count = 0;
		for(Seat seat : seatList){
			if(seat.getUser() == null){
				continue;
			}
			if(seat.isReady()){
				count += 1;
			}
		}
		return count;
	}
	
	/**
	 * 获取座位对象
	 * @param room
	 * @param position
	 * @return
	 */
	public Seat getSeat(Room room, int position){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(seat.getPosition() == position){
				return seat;
			}
		}
		return null;
	}
	
	/**
	 * 获取下一家的座位对象
	 * @param seat
	 * @return
	 */
	public Seat getNextSeat(Seat seat){
		Room room = seat.getRoom();
		int size = room.getSeatList().size();
		Seat pseat = null;
		boolean flag = true;
		int position = seat.getPosition();
		while(flag){
			position = position + 1;
			if(position > size){
				position = 1;
			}
			pseat = getSeat(room,position);
			if(pseat.getPosition() == room.getBankerPosition()){
				room.setRoundNums(room.getRoundNums() + 1);
			}
			if(null != pseat.getUser() && pseat.isReady() == true && pseat.isGiveUp() == false){
				flag = false;
			}
		}
		return pseat;
	}
	
	/**
	 * 判断房间游戏中剩余人数
	 * @param room
	 * @return
	 */
	public int gameingSize(Room room){
		List<Seat> seatList = room.getSeatList();
		int count = 0;
		for(Seat seat : seatList){
			if(null == seat.getRouteSession() || !seat.isReady()){
				continue;
			}
			if(!seat.isGiveUp()){
				count += 1;
			}
		}
		return count;
	}
	
	
	/**
	 * 更改用户金币
	 * @param seat
	 */
	public void updateUserGold(Room room){
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			if(!seat.isReady()){
				continue;
			}
			User user = seat.getUser();
			if(null == user){
				continue;
			}
			userService.updateConsumeGold(user.getId(), -seat.getTotalAnte());
		}
	}
	
	/**
	 * 更改用户金币
	 * @param seat
	 */
	public void updateSeatGold(Seat seat){
		User user = seat.getUser();
		Integer userId = user.getId();
		userService.updateConsumeGold(userId, user.getGold() - seat.getGold());
	}
	
	/**
	 * 更改房间数据
	 */
	public void updateSeatInfo(Seat seat){
		User user = seat.getUser();
		Integer userId = user.getId();
		userService.updateRoomId(userId, null);
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
		GameServer gameServer = gameServiceComponent.getGameServer();
		FightExploits fightExploits = new FightExploits();
		fightExploits.setCreateTime(new Date());
		fightExploits.setGameName(gameServer.getName());
		fightExploits.setGameType(gameServer.getGameType());
		fightExploits.setRoomId(room.getId());
		fightExploitsService.add(fightExploits);
		List<Seat> seatList = room.getSeatList();
		for(Seat seat : seatList){
			User user = seat.getUser();
			if(user == null || !seat.isReady()){
				continue;
			}
			FightDetail fightDetail = new FightDetail();
			fightDetail.setExploitsId(fightExploits.getId());
			fightDetail.setPosition(seat.getPosition());
			fightDetail.setScore(seat.getTotalAnte());
			fightDetail.setUserId(user.getId());
			fightDetail.setUserName(user.getName());
			fightDetailService.add(fightDetail);
		}
	}
	
	/**
	 * 判断用户是否没有房间
	 * @param user
	 * @param playWay
	 * @return
	 */
	public int hasInRoom(User user, PlayWay playWay){
		Integer userId = user.getId();
		Seat seat = seatMap.get(userId);
		if(seat == null){
			return 0;
		}else{
			Room room = seat.getRoom();
			if(room.getPlayWay().equals(playWay)){
				return 0;
			}else{
				return room.getPlayWay().getAntes();
			}
		}
	}
	
	/**
	 * 判断是否准入
	 * @param param
	 * @return
	 */
	public boolean access(C2SJoinRoom param){
			switch (param.getLowGold()) {
			case 500:
				return param.getAntes() == 50;
			case 1000:
				return param.getAntes() == 100;
			case 4000:
				return param.getAntes() == 200;
			case 10000:
				return param.getAntes() == 500;
			case 16000:
				return param.getAntes() == 800;
			case 20000:
				return param.getAntes() == 1000;
			default:
				return false;
			}
	}
	
}
