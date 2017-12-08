package com.starland.xyqp.db.service;

import com.starland.xyqp.db.domain.GameRoom;

public interface GameRoomService {

	GameRoom get(String id);
	
	void add(GameRoom gameRoom);
	
	void delete(String id);
	
	void updateCurrentPerson(String id, Integer currentPerson);

	/**
	 * 消耗砖石
	 * @param roomId
	 * @param diamond
	 */
	void updateConsumeDiamond(String roomId, Integer diamond);
	
}
