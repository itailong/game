package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.User;
import com.starland.xyqp.gmback.query.UserQuery;
import com.starland.xyqp.gmback.vo.LoginInfo;

public interface UserService {

	List<User> list(UserQuery query, PageInfo pageInfo);
	
	User get(Integer id);
	
	void add(User user);
	
	void delete(Integer id);
	
	void update (User user);

	LoginInfo getLoginInfo(Integer userId);

	void updatePassword(Integer id, String password);

	void updatePayDiamond(Integer userId, Integer diamond, Integer operatorId);
	
}
