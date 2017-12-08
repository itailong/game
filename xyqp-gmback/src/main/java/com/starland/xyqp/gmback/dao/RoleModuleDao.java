package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.RoleModule;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.RoleModuleQuery;

public interface RoleModuleDao {

	List<RoleModule> list(RoleModuleQuery query, PageBounds bounds);
	
	RoleModule get(Integer id);
	
	void add(RoleModule roleModule);
	
	void delete(Integer id);
	
	void update (RoleModule roleModule);
	
	void deleteByRoleId(Integer roleId);
	
}
