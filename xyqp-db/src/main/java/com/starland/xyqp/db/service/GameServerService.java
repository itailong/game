package com.starland.xyqp.db.service;

import com.starland.xyqp.db.domain.GameServer;

public interface GameServerService {

	GameServer get(Integer id);
	
	void updateRefreshTime(Integer id);
	
}
