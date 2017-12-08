package com.starland.xyqp.db.dao;

import org.apache.ibatis.annotations.Param;

import com.starland.xyqp.db.domain.GameRoom;

public interface GameRoomDao {

	GameRoom get(String id);
	
	void add(GameRoom gameRoom);
	
	void delete(String id);
	
	void updateCurrentPerson(@Param("id") String id, @Param("currentPerson") Integer currentPerson);
}
