package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.GoldIncome;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.GoldIncomeQuery;

public interface GoldIncomeDao {

	List<GoldIncome> list(GoldIncomeQuery query, PageBounds bounds);
	
	GoldIncome get(Integer id);
	
	void add(GoldIncome goldIncome);
	
	void delete(Integer id);
	
	void update (GoldIncome goldIncome);
	
}
