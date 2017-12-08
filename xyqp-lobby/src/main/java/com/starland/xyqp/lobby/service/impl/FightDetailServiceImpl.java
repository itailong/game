package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.FightDetailDao;
import com.starland.xyqp.lobby.domain.FightDetail;
import com.starland.xyqp.lobby.service.FightDetailService;

@Service
public class FightDetailServiceImpl implements FightDetailService {

	@Resource
	private FightDetailDao fightDetailDao;

	@Override
	public List<FightDetail> findByExploitsIds(Integer[] ids) {
		return fightDetailDao.findByExploitsIds(ids);
	}

}
