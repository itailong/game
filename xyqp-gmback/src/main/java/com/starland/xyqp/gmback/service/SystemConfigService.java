package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.SystemConfig;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.SystemConfigQuery;

public interface SystemConfigService {

	List<SystemConfig> list(SystemConfigQuery query, PageInfo pageInfo);
	
	SystemConfig get(String id);
	
	void add(SystemConfig systemConfig);
	
	void delete(String id);
	
	void update (SystemConfig systemConfig);
	
}
