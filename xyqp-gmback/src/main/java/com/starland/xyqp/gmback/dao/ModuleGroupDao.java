package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.ModuleGroup;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.ModuleGroupQuery;

public interface ModuleGroupDao {

	List<ModuleGroup> list(ModuleGroupQuery query, PageBounds bounds);
	
	ModuleGroup get(Integer id);
	
	void add(ModuleGroup moduleGroup);
	
	void delete(Integer id);
	
	void update (ModuleGroup moduleGroup);
	
}
