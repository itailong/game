package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.ModuleGroup;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.ModuleGroupQuery;

public interface ModuleGroupService {

	List<ModuleGroup> list(ModuleGroupQuery query, PageInfo pageInfo);
	
	ModuleGroup get(Integer id);
	
	void add(ModuleGroup moduleGroup);
	
	void delete(Integer id);
	
	void update (ModuleGroup moduleGroup);
	
}
