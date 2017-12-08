package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.GameRoomDao;
import com.starland.xyqp.lobby.domain.GameRoom;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.lobby.query.GameRoomQuery;
import com.starland.xyqp.lobby.service.GameRoomService;

@Service
public class GameRoomServiceImpl implements GameRoomService {

	@Resource
	private GameRoomDao gameRoomDao;
	
	@Override
	public List<GameRoom> list(GameRoomQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return gameRoomDao.list(query, bounds);
	}

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
	public void update(GameRoom gameRoom) {
		gameRoomDao.update(gameRoom);
	}

	@Override
	public List<GameRoom> findByCreatorId(Integer creatorId) {
		return gameRoomDao.findByCreatorId(creatorId);
	}

}
