package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.GoldIncome;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.GoldIncomeQuery;

public interface GoldIncomeService {

	List<GoldIncome> list(GoldIncomeQuery query, PageInfo pageInfo);
	
	GoldIncome get(Integer id);
	
	void add(GoldIncome goldIncome);
	
	void delete(Integer id);
	
	void update (GoldIncome goldIncome);
	
}
