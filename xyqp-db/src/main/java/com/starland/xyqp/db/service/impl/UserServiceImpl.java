package com.starland.xyqp.db.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.db.dao.DiamondConsumeDao;
import com.starland.xyqp.db.dao.GameRoomDao;
import com.starland.xyqp.db.dao.GoldConsumeDao;
import com.starland.xyqp.db.dao.UserDao;
import com.starland.xyqp.db.domain.DiamondConsume;
import com.starland.xyqp.db.domain.GameRoom;
import com.starland.xyqp.db.domain.GoldConsume;
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.db.exception.DatabaseException;
import com.starland.xyqp.db.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Resource
	private GameRoomDao gameRoomDao;
	
	@Resource
	private DiamondConsumeDao diamondConsumeDao;
	
	@Resource
	private GoldConsumeDao goldConsumeDao;
	
	@Override
	public User get(Integer id) {
		return userDao.get(id);
	}

	@Override
	public User getByToken(String token) {
		return userDao.getByToken(token);
	}

	@Deprecated
	@Override
	public void updateAppendDiamond(Integer id, Integer diamond) {
		userDao.updateAppendDiamond(id, diamond);
	}

	@Override
	public void updateRoomId(Integer id, String roomId) {
		userDao.updateRoomId(id, roomId);
	}

	@Override
	public void updateConsumeDiamond(Integer id, int diamond) {
		User user = userDao.get(id);
		String roomId = user.getRoomId();
		if (null == roomId || "".equals(roomId)) {
			throw new DatabaseException("房间编号为空！");
		}
		GameRoom gameRoom = gameRoomDao.get(roomId);
		if (null == gameRoom) {
			throw new DatabaseException("房间不存在！");
		}
		DiamondConsume diamondConsume = new DiamondConsume();
		diamondConsume.setConsume(diamond);
		diamondConsume.setCreateTime(new Date());
		diamondConsume.setGameName(gameRoom.getGameName());
		diamondConsume.setRoomId(roomId);
		diamondConsume.setRoundCount(gameRoom.getRoundCount());
		diamondConsume.setServerId(gameRoom.getServerId());
		diamondConsume.setUserId(id);
		diamondConsumeDao.add(diamondConsume);
		userDao.updateConsumeDiamond(id, diamond);
	}

	@Override
	public void updateConsumeGold(Integer id, int gold) {
		User user = userDao.get(id);
		String roomId = user.getRoomId();
		if (null == roomId || "".equals(roomId)) {
			throw new DatabaseException("房间编号为空！");
		}
		GameRoom gameRoom = gameRoomDao.get(roomId);
		if (null == gameRoom) {
			throw new DatabaseException("房间不存在！");
		}
		GoldConsume goldConsume = new GoldConsume();
		goldConsume.setConsume(gold);
		goldConsume.setCreateTime(new Date());
		goldConsume.setGameName(gameRoom.getGameName());
		goldConsume.setRoomId(roomId);
		goldConsume.setRoundCount(gameRoom.getRoundCount());
		goldConsume.setServerId(gameRoom.getRoundCount());
		goldConsume.setUserId(id);
		goldConsumeDao.add(goldConsume);
		userDao.updateConsumeGold(id, gold);
		// TODO 返利未实现
	}

}
