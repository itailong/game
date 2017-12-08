package com.starland.xyqp.db.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.db.dao.GameServerDao;
import com.starland.xyqp.db.domain.GameServer;
import com.starland.xyqp.db.service.GameServerService;

@Service
public class GameServerServiceImpl implements GameServerService {

	@Resource
	private GameServerDao gameServerDao;
	
	@Override
	public GameServer get(Integer id) {
		return gameServerDao.get(id);
	}

	@Override
	public void updateRefreshTime(Integer id) {
		gameServerDao.updateRefreshTime(id);
	}

}
