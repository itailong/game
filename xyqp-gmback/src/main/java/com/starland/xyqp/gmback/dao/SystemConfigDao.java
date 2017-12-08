package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.SystemConfig;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.SystemConfigQuery;

public interface SystemConfigDao {

	List<SystemConfig> list(SystemConfigQuery query, PageBounds bounds);
	
	SystemConfig get(String id);
	
	void add(SystemConfig systemConfig);
	
	void delete(String id);
	
	void update (SystemConfig systemConfig);
	
}
