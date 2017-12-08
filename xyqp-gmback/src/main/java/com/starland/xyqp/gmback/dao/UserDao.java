package com.starland.xyqp.gmback.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.starland.xyqp.gmback.domain.User;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.UserQuery;

public interface UserDao {

	List<User> list(UserQuery query, PageBounds bounds);
	
	User get(Integer id);
	
	void add(User user);
	
	void delete(Integer id);
	
	void update (User user);

	void appendDiamond(@Param("id") Integer id, @Param("diamond") Integer diamond);
	
	void updatePassword(@Param("id") Integer id, @Param("password") String password);
	
}
