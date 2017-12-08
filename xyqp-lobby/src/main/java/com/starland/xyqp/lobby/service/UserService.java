package com.starland.xyqp.lobby.service;


import com.starland.xyqp.lobby.domain.User;


public interface UserService {

	User getByToken(String token);
	
	void add(User user);
	
	void delete(Integer id);
	
	void update (User user);
	
	User getByUnionId(String unionId);

}
