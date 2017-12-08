package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.ModuleGroupDao;
import com.starland.xyqp.gmback.domain.ModuleGroup;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.ModuleGroupQuery;
import com.starland.xyqp.gmback.service.ModuleGroupService;

@Service
public class ModuleGroupServiceImpl implements ModuleGroupService {

	@Resource
	private ModuleGroupDao moduleGroupDao;
	
	@Override
	public List<ModuleGroup> list(ModuleGroupQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return moduleGroupDao.list(query, bounds);
	}

	@Override
	public ModuleGroup get(Integer id) {
		return moduleGroupDao.get(id);
	}

	@Override
	public void add(ModuleGroup moduleGroup) {
		moduleGroupDao.add(moduleGroup);
	}

	@Override
	public void delete(Integer id) {
		moduleGroupDao.delete(id);
	}

	@Override
	public void update(ModuleGroup moduleGroup) {
		moduleGroupDao.update(moduleGroup);
	}

}
