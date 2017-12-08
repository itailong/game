package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.GameRoom;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.GameRoomQuery;

public interface GameRoomDao {

	List<GameRoom> list(GameRoomQuery query, PageBounds bounds);
	
	GameRoom get(String id);
	
	void add(GameRoom gameRoom);
	
	void delete(String id);
	
	void update (GameRoom gameRoom);
	
}
