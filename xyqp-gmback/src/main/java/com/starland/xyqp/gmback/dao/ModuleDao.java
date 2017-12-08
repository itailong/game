package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.Module;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.ModuleQuery;

public interface ModuleDao {

	List<Module> list(ModuleQuery query, PageBounds bounds);
	
	Module get(Integer id);
	
	void add(Module module);
	
	void delete(Integer id);
	
	void update(Module module);
	
	List<Module> findAll();
	
	List<Module> findByUserId(Integer userId);
	
}
