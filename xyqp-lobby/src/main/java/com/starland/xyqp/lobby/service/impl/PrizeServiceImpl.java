package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.PrizeDao;
import com.starland.xyqp.lobby.domain.Prize;
import com.starland.xyqp.lobby.service.PrizeService;

@Service
public class PrizeServiceImpl implements PrizeService {

	@Resource
	private PrizeDao prizeDao;
	
	@Override
	public List<Prize> list() {
		return prizeDao.list();
	}

	@Override
	public List<Prize> getList() {
		return prizeDao.getList();
	}

}
