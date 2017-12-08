package com.starland.xyqp.lobby.dao;


import org.apache.ibatis.annotations.Param;

import com.starland.xyqp.lobby.domain.User;

public interface UserDao {

	User get(Integer id);
	
	void add(User user);
	
	void delete(Integer id);
	
	void update(User user);
	
	User getByToken(String token);
	
	User getByUnionId(String unionId);
	
	void appendGold(@Param("id") Integer id, @Param("gold") Integer gold);
	
}
