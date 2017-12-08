package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.DiamondConsume;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.DiamondConsumeQuery;

public interface DiamondConsumeDao {

	List<DiamondConsume> list(DiamondConsumeQuery query, PageBounds bounds);
	
	DiamondConsume get(Integer id);
	
	void add(DiamondConsume diamondConsume);
	
	void delete(Integer id);
	
	void update (DiamondConsume diamondConsume);
	
}
