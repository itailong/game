package com.starland.xyqp.db.dao;

import java.util.List;

import com.starland.xyqp.db.domain.GoldConsume;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.db.query.GoldConsumeQuery;

public interface GoldConsumeDao {

	List<GoldConsume> list(GoldConsumeQuery query, PageBounds bounds);
	
	GoldConsume get(Integer id);
	
	void add(GoldConsume goldConsume);
	
	void delete(Integer id);
	
	void update (GoldConsume goldConsume);
	
}
