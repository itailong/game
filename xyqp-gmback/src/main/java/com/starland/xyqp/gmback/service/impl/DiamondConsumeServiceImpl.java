package com.starland.xyqp.gmback.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.gmback.dao.DiamondConsumeDao;
import com.starland.xyqp.gmback.domain.DiamondConsume;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.DiamondConsumeQuery;
import com.starland.xyqp.gmback.service.DiamondConsumeService;

@Service
public class DiamondConsumeServiceImpl implements DiamondConsumeService {

	@Resource
	private DiamondConsumeDao diamondConsumeDao;
	
	@Override
	public List<DiamondConsume> list(DiamondConsumeQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return diamondConsumeDao.list(query, bounds);
	}

	@Override
	public DiamondConsume get(Integer id) {
		return diamondConsumeDao.get(id);
	}

	@Override
	public void add(DiamondConsume diamondConsume) {
		diamondConsumeDao.add(diamondConsume);
	}

	@Override
	public void delete(Integer id) {
		diamondConsumeDao.delete(id);
	}

	@Override
	public void update(DiamondConsume diamondConsume) {
		diamondConsumeDao.update(diamondConsume);
	}

}
