package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.GameServerDao;
import com.starland.xyqp.lobby.domain.GameServer;
import com.starland.xyqp.lobby.service.GameServerService;

@Service
public class GameServerServiceImpl implements GameServerService {

	@Resource
	private GameServerDao gameServerDao;
	
	@Override
	public GameServer get(Integer id) {
		return gameServerDao.get(id);
	}

	@Override
	public List<GameServer> findByGameType(String gameType) {
		return gameServerDao.findByGameType(gameType);
	}
	
}
