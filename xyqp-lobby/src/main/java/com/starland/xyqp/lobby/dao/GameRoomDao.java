package com.starland.xyqp.lobby.dao;

import java.util.List;

import com.starland.xyqp.lobby.domain.GameRoom;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.lobby.query.GameRoomQuery;

public interface GameRoomDao {

	List<GameRoom> list(GameRoomQuery query, PageBounds bounds);
	
	GameRoom get(String id);
	
	void add(GameRoom gameRoom);
	
	void delete(String id);
	
	void update (GameRoom gameRoom);
	
	List<GameRoom> findByCreatorId(Integer creatorId);
	
}
