package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.ModuleDao;
import com.starland.xyqp.gmback.domain.Module;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.ModuleQuery;
import com.starland.xyqp.gmback.service.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService {

	@Resource
	private ModuleDao moduleDao;
	
	@Override
	public List<Module> list(ModuleQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return moduleDao.list(query, bounds);
	}

	@Override
	public Module get(Integer id) {
		return moduleDao.get(id);
	}

	@Override
	public void add(Module module) {
		moduleDao.add(module);
	}

	@Override
	public void delete(Integer id) {
		moduleDao.delete(id);
	}

	@Override
	public void update(Module module) {
		moduleDao.update(module);
	}

}
