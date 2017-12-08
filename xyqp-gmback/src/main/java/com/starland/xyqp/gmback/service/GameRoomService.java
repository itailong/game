package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.GameRoom;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.GameRoomQuery;

public interface GameRoomService {

	List<GameRoom> list(GameRoomQuery query, PageInfo pageInfo);
	
	GameRoom get(String id);
	
	void add(GameRoom gameRoom);
	
	void delete(String id);
	
	void update (GameRoom gameRoom);
	
}
