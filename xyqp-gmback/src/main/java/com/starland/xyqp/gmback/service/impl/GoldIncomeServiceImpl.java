package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.GoldIncomeDao;
import com.starland.xyqp.gmback.domain.GoldIncome;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.GoldIncomeQuery;
import com.starland.xyqp.gmback.service.GoldIncomeService;

@Service
public class GoldIncomeServiceImpl implements GoldIncomeService {

	@Resource
	private GoldIncomeDao goldIncomeDao;
	
	@Override
	public List<GoldIncome> list(GoldIncomeQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return goldIncomeDao.list(query, bounds);
	}

	@Override
	public GoldIncome get(Integer id) {
		return goldIncomeDao.get(id);
	}

	@Override
	public void add(GoldIncome goldIncome) {
		goldIncomeDao.add(goldIncome);
	}

	@Override
	public void delete(Integer id) {
		goldIncomeDao.delete(id);
	}

	@Override
	public void update(GoldIncome goldIncome) {
		goldIncomeDao.update(goldIncome);
	}

}
