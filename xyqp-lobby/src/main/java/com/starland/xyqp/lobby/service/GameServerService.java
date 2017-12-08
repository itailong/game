package com.starland.xyqp.lobby.service;

import java.util.List;

import com.starland.xyqp.lobby.domain.GameServer;

public interface GameServerService {

	GameServer get(Integer id);
	
	List<GameServer> findByGameType(String gameType);
	
}
