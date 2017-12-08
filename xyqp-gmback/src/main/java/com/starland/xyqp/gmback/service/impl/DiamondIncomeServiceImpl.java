package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.DiamondIncomeDao;
import com.starland.xyqp.gmback.domain.DiamondIncome;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.DiamondIncomeQuery;
import com.starland.xyqp.gmback.service.DiamondIncomeService;

@Service
public class DiamondIncomeServiceImpl implements DiamondIncomeService {

	@Resource
	private DiamondIncomeDao diamondIncomeDao;
	
	@Override
	public List<DiamondIncome> list(DiamondIncomeQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return diamondIncomeDao.list(query, bounds);
	}

	@Override
	public DiamondIncome get(Integer id) {
		return diamondIncomeDao.get(id);
	}

	@Override
	public void add(DiamondIncome diamondIncome) {
		diamondIncomeDao.add(diamondIncome);
	}

	@Override
	public void delete(Integer id) {
		diamondIncomeDao.delete(id);
	}

	@Override
	public void update(DiamondIncome diamondIncome) {
		diamondIncomeDao.update(diamondIncome);
	}

}
