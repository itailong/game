package com.starland.xyqp.lobby.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.SuggestDao;
import com.starland.xyqp.lobby.domain.Suggest;
import com.starland.xyqp.lobby.service.SuggestService;

@Service
public class SuggestServiceImpl implements SuggestService {

	@Resource
	private SuggestDao suggestDao;
	
	@Override
	public void add(Suggest suggest) {
		suggestDao.add(suggest);
	}
}
