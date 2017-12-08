package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.RoleModule;
import com.starland.xyqp.gmback.query.RoleModuleQuery;
import com.starland.xyqp.gmback.vo.MultipleChoice;

public interface RoleModuleService {

	List<RoleModule> list(RoleModuleQuery query, PageInfo pageInfo);
	
	RoleModule get(Integer id);
	
	void add(RoleModule roleModule);
	
	void delete(Integer id);
	
	void update (RoleModule roleModule);

	void updateMultipleChoice(MultipleChoice multipleChoice);

	MultipleChoice getMultipleChoice(Integer roleId);
	
}
