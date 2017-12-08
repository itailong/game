package com.starland.xyqp.db.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.db.dao.GameRoomDao;
import com.starland.xyqp.db.domain.GameRoom;
import com.starland.xyqp.db.service.GameRoomService;
import com.starland.xyqp.db.service.UserService;

@Service
public class GameRoomServiceImpl implements GameRoomService {

	@Resource
	private GameRoomDao gameRoomDao;
	
	@Resource
	private UserService userService;
	
	@Override
	public GameRoom get(String id) {
		return gameRoomDao.get(id);
	}

	@Override
	public void add(GameRoom gameRoom) {
		gameRoomDao.add(gameRoom);
	}

	@Override
	public void delete(String id) {
		gameRoomDao.delete(id);
	}

	@Override
	public void updateCurrentPerson(String id, Integer currentPerson) {
		gameRoomDao.updateCurrentPerson(id, currentPerson);
	}
	
	@Override
	public void updateConsumeDiamond(String roomId, Integer diamond) {
		GameRoom gameRoom = gameRoomDao.get(roomId);
		Integer userId = gameRoom.getCreatorId();
		userService.updateConsumeDiamond(userId, diamond);
	}
	
}
