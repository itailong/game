package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.UserRole;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.UserRoleQuery;

public interface UserRoleDao {

	List<UserRole> list(UserRoleQuery query, PageBounds bounds);
	
	UserRole get(Integer id);
	
	void add(UserRole userRole);
	
	void delete(Integer id);
	
	void update (UserRole userRole);

	void deleteByUserId(Integer userId);
	
}
