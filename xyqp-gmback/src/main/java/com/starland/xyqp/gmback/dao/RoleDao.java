package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.Role;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.RoleQuery;

public interface RoleDao {

	List<Role> list(RoleQuery query, PageBounds bounds);
	
	Role get(Integer id);
	
	void add(Role role);
	
	void delete(Integer id);
	
	void update (Role role);
	
}
