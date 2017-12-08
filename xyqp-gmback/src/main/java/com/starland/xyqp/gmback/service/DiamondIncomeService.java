package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.DiamondIncome;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.DiamondIncomeQuery;

public interface DiamondIncomeService {

	List<DiamondIncome> list(DiamondIncomeQuery query, PageInfo pageInfo);
	
	DiamondIncome get(Integer id);
	
	void add(DiamondIncome diamondIncome);
	
	void delete(Integer id);
	
	void update (DiamondIncome diamondIncome);
	
}
