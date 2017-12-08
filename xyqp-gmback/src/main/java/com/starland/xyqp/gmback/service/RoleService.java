package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.Role;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.RoleQuery;

public interface RoleService {

	List<Role> list(RoleQuery query, PageInfo pageInfo);
	
	Role get(Integer id);
	
	void add(Role role);
	
	void delete(Integer id);
	
	void update (Role role);
	
}
