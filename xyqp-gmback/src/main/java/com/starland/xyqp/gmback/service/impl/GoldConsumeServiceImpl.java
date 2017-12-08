package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.GoldConsumeDao;
import com.starland.xyqp.gmback.domain.GoldConsume;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.GoldConsumeQuery;
import com.starland.xyqp.gmback.service.GoldConsumeService;

@Service
public class GoldConsumeServiceImpl implements GoldConsumeService {

	@Resource
	private GoldConsumeDao goldConsumeDao;
	
	@Override
	public List<GoldConsume> list(GoldConsumeQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return goldConsumeDao.list(query, bounds);
	}

	@Override
	public GoldConsume get(Integer id) {
		return goldConsumeDao.get(id);
	}

	@Override
	public void add(GoldConsume goldConsume) {
		goldConsumeDao.add(goldConsume);
	}

	@Override
	public void delete(Integer id) {
		goldConsumeDao.delete(id);
	}

	@Override
	public void update(GoldConsume goldConsume) {
		goldConsumeDao.update(goldConsume);
	}

}
