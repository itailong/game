package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.SystemConfigDao;
import com.starland.xyqp.gmback.domain.SystemConfig;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.SystemConfigQuery;
import com.starland.xyqp.gmback.service.SystemConfigService;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

	@Resource
	private SystemConfigDao systemConfigDao;
	
	@Override
	public List<SystemConfig> list(SystemConfigQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return systemConfigDao.list(query, bounds);
	}

	@Override
	public SystemConfig get(String id) {
		return systemConfigDao.get(id);
	}

	@Override
	public void add(SystemConfig systemConfig) {
		systemConfigDao.add(systemConfig);
	}

	@Override
	public void delete(String id) {
		systemConfigDao.delete(id);
	}

	@Override
	public void update(SystemConfig systemConfig) {
		systemConfigDao.update(systemConfig);
	}

}
