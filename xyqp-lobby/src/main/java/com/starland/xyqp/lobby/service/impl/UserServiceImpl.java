package com.starland.xyqp.lobby.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.starland.xyqp.lobby.dao.UserDao;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	

	@Resource
	private UserDao userDao;

	
	@Override
	public User getByToken(String token) {
		 return userDao.getByToken(token);
	}

	@Override
	public void add(User user) {
		userDao.add(user);
		
	}

	@Override
	public void delete(Integer id) {
		userDao.delete(id);
	}

	@Override
	public void update(User user) {
		userDao.update(user);		
	}

	@Override
	public User getByUnionId(String unionId) {
		return userDao.getByUnionId(unionId);
	}

}
