package com.starland.xyqp.gmback.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.dao.ModuleDao;
import com.starland.xyqp.gmback.dao.RoleModuleDao;
import com.starland.xyqp.gmback.domain.Module;
import com.starland.xyqp.gmback.domain.RoleModule;
import com.starland.xyqp.gmback.query.ModuleQuery;
import com.starland.xyqp.gmback.query.RoleModuleQuery;
import com.starland.xyqp.gmback.service.RoleModuleService;
import com.starland.xyqp.gmback.vo.MultipleChoice;
import com.starland.xyqp.gmback.vo.MultipleChoice.Option;

@Service
public class RoleModuleServiceImpl implements RoleModuleService {

	@Resource
	private RoleModuleDao roleModuleDao;
	
	@Resource
	private ModuleDao moduleDao;
	
	@Override
	public List<RoleModule> list(RoleModuleQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return roleModuleDao.list(query, bounds);
	}

	@Override
	public RoleModule get(Integer id) {
		return roleModuleDao.get(id);
	}

	@Override
	public void add(RoleModule roleModule) {
		roleModuleDao.add(roleModule);
	}

	@Override
	public void delete(Integer id) {
		roleModuleDao.delete(id);
	}

	@Override
	public void update(RoleModule roleModule) {
		roleModuleDao.update(roleModule);
	}
	
	@Override
	public MultipleChoice getMultipleChoice(Integer roleId) {
		ModuleQuery moduleQuery = new ModuleQuery();
		List<Module> list = moduleDao.list(moduleQuery, PageBounds.NO_PAGE_BOUNDS);
		RoleModuleQuery rmQuery = new RoleModuleQuery();
		rmQuery.setRoleId(roleId);
		List<RoleModule> rmList = roleModuleDao.list(rmQuery, PageBounds.NO_PAGE_BOUNDS);
		List<Integer> ids = new ArrayList<>();
		for (RoleModule roleModule : rmList) {
			ids.add(roleModule.getModuleId());
		}
		MultipleChoice result = new MultipleChoice();
		List<MultipleChoice.Option> options = new ArrayList<>();
		for (Module module : list) {
			MultipleChoice.Option option = new MultipleChoice.Option();
			option.setId(module.getId());
			option.setName(module.getName());
			if (ids.contains(module.getId())) {
				option.setSelected(true);
			}
			options.add(option);
		}
		result.setId(roleId);
		result.setOptions(options);
		return result;
	}
	
	@Override
	public void updateMultipleChoice(MultipleChoice multipleChoice) {
		Integer roleId = multipleChoice.getId();
		roleModuleDao.deleteByRoleId(roleId);
		List<Option> options = multipleChoice.getOptions();
		if (null == options) {
			return;
		}
		for (MultipleChoice.Option option : options) {
			Integer moduleId = option.getId();
			if (null == moduleId) {
				continue;
			}
			RoleModule roleModule = new RoleModule();
			roleModule.setRoleId(roleId);
			roleModule.setModuleId(option.getId());
			roleModuleDao.add(roleModule);
		}
	}

}
