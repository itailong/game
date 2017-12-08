package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.Module;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.ModuleQuery;

public interface ModuleService {

	List<Module> list(ModuleQuery query, PageInfo pageInfo);
	
	Module get(Integer id);
	
	void add(Module module);
	
	void delete(Integer id);
	
	void update (Module module);
	
}
