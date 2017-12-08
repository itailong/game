package com.starland.xyqp.lobby.dao;

import java.util.List;

import com.starland.xyqp.lobby.domain.GameServer;

public interface GameServerDao {

	GameServer get(Integer id);
	
	List<GameServer> findByGameType(String gameType);
	
}
