package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.GoldConsume;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.GoldConsumeQuery;

public interface GoldConsumeService {

	List<GoldConsume> list(GoldConsumeQuery query, PageInfo pageInfo);
	
	GoldConsume get(Integer id);
	
	void add(GoldConsume goldConsume);
	
	void delete(Integer id);
	
	void update (GoldConsume goldConsume);
	
}
