package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.RoleDao;
import com.starland.xyqp.gmback.domain.Role;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.RoleQuery;
import com.starland.xyqp.gmback.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleDao roleDao;
	
	@Override
	public List<Role> list(RoleQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return roleDao.list(query, bounds);
	}

	@Override
	public Role get(Integer id) {
		return roleDao.get(id);
	}

	@Override
	public void add(Role role) {
		roleDao.add(role);
	}

	@Override
	public void delete(Integer id) {
		roleDao.delete(id);
	}

	@Override
	public void update(Role role) {
		roleDao.update(role);
	}

}
