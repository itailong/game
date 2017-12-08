package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.DiamondConsume;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.DiamondConsumeQuery;

public interface DiamondConsumeService {

	List<DiamondConsume> list(DiamondConsumeQuery query, PageInfo pageInfo);
	
	DiamondConsume get(Integer id);
	
	void add(DiamondConsume diamondConsume);
	
	void delete(Integer id);
	
	void update (DiamondConsume diamondConsume);
	
}
