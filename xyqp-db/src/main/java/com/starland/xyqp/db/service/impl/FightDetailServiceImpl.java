package com.starland.xyqp.db.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.db.dao.FightDetailDao;
import com.starland.xyqp.db.domain.FightDetail;
import com.starland.xyqp.db.service.FightDetailService;

@Service
public class FightDetailServiceImpl implements FightDetailService {

	@Resource
	private FightDetailDao fightDetailDao;

	@Override
	public void add(FightDetail fightDetail) {
		fightDetailDao.add(fightDetail);
	}

}
