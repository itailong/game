package com.starland.xyqp.gmback.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.dao.RoleDao;
import com.starland.xyqp.gmback.dao.UserRoleDao;
import com.starland.xyqp.gmback.domain.Role;
import com.starland.xyqp.gmback.domain.UserRole;
import com.starland.xyqp.gmback.query.RoleQuery;
import com.starland.xyqp.gmback.query.UserRoleQuery;
import com.starland.xyqp.gmback.service.UserRoleService;
import com.starland.xyqp.gmback.vo.MultipleChoice;
import com.starland.xyqp.gmback.vo.MultipleChoice.Option;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Resource
	private UserRoleDao userRoleDao;
	
	@Resource
	private RoleDao roleDao;
	
	@Override
	public List<UserRole> list(UserRoleQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return userRoleDao.list(query, bounds);
	}

	@Override
	public UserRole get(Integer id) {
		return userRoleDao.get(id);
	}

	@Override
	public void add(UserRole userRole) {
		userRoleDao.add(userRole);
	}

	@Override
	public void delete(Integer id) {
		userRoleDao.delete(id);
	}

	@Override
	public void update(UserRole userRole) {
		userRoleDao.update(userRole);
	}
	
	@Override
	public MultipleChoice getMultipleChoice(Integer userId) {
		RoleQuery roleQuery = new RoleQuery();
		List<Role> list = roleDao.list(roleQuery, PageBounds.NO_PAGE_BOUNDS);
		
		UserRoleQuery query = new UserRoleQuery();
		query.setUserId(userId);
		List<UserRole> urList = userRoleDao.list(query, PageBounds.NO_PAGE_BOUNDS);
		List<Integer> ids = new ArrayList<>();
		for (UserRole userRole : urList) {
			ids.add(userRole.getRoleId());
		}
		MultipleChoice result = new MultipleChoice();
		List<MultipleChoice.Option> options = new ArrayList<>();
		
		for (Role role : list) {
			MultipleChoice.Option option = new MultipleChoice.Option();
			option.setId(role.getId());
			option.setName(role.getName());
			if (ids.contains(role.getId())) {
				option.setSelected(true);
			}
			options.add(option);
		}
		result.setId(userId);
		result.setOptions(options);
		return result;
	}
	
	@Override
	public void updateMultipleChoice(MultipleChoice multipleChoice) {
		Integer userId = multipleChoice.getId();
		userRoleDao.deleteByUserId(userId);
		List<Option> options = multipleChoice.getOptions();
		if (null == options) {
			return;
		}
		for (MultipleChoice.Option option : options) {
			Integer moduleId = option.getId();
			if (null == moduleId) {
				continue;
			}
			UserRole userRole = new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(option.getId());
			userRoleDao.add(userRole);
		}
	}
	
}
