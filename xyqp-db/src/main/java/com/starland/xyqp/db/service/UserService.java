package com.starland.xyqp.db.service;

import com.starland.xyqp.db.domain.User;

public interface UserService {

	User get(Integer id);
	
	User getByToken(String token);
	
	@Deprecated
	void updateAppendDiamond(Integer id, Integer diamond);
	
	/**
	 * 修改用户所在房间
	 * @param id
	 * @param roomId
	 */
	void updateRoomId(Integer id, String roomId);
	
	/**
	 * 消耗钻石
	 * @param id
	 * @param diamond
	 */
	void updateConsumeDiamond(Integer id, int diamond);
	
	/**
	 * 消耗金币
	 * @param id
	 * @param gold
	 */
	void updateConsumeGold(Integer id, int gold);
	
}
