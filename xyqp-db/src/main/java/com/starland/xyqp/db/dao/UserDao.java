package com.starland.xyqp.db.dao;

import org.apache.ibatis.annotations.Param;

import com.starland.xyqp.db.domain.User;

public interface UserDao {

	User get(Integer id);
	
	User getByToken(String token);
	
	@Deprecated
	void updateAppendDiamond(@Param("id") Integer id, @Param("diamond") Integer diamond);
	
	void updateConsumeDiamond(@Param("id") Integer id, @Param("diamond") Integer diamond);
	
	void updateConsumeGold(@Param("id") Integer id, @Param("gold") int gold);
	
	void updateRoomId(@Param("id") Integer id, @Param("roomId") String roomId);
	
}
