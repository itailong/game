package com.starland.xyqp.gmback.dao;

import java.util.List;

import com.starland.xyqp.gmback.domain.DiamondIncome;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.query.DiamondIncomeQuery;

public interface DiamondIncomeDao {

	List<DiamondIncome> list(DiamondIncomeQuery query, PageBounds bounds);
	
	DiamondIncome get(Integer id);
	
	void add(DiamondIncome diamondIncome);
	
	void delete(Integer id);
	
	void update (DiamondIncome diamondIncome);
	
}
