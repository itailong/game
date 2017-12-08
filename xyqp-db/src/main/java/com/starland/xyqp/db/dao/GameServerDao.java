package com.starland.xyqp.db.dao;

import com.starland.xyqp.db.domain.GameServer;

public interface GameServerDao {

	GameServer get(Integer id);
	
	void updateRefreshTime(Integer id);
	
}
