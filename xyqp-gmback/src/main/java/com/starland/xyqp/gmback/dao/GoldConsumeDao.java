package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.GoldConsume;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.GoldConsumeQuery;

public interface GoldConsumeDao {

	List<GoldConsume> list(GoldConsumeQuery query, PageBounds bounds);
	
	GoldConsume get(Integer id);
	
	void add(GoldConsume goldConsume);
	
	void delete(Integer id);
	
	void update (GoldConsume goldConsume);
	
}
