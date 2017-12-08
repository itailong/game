package com.starland.xyqp.lobby.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.lobby.dao.UserDao;
import com.starland.xyqp.lobby.dao.WelfareDao;
import com.starland.xyqp.lobby.domain.Welfare;
import com.starland.xyqp.lobby.query.WelfareQuery;
import com.starland.xyqp.lobby.service.WelfareService;

@Service
public class WelfareServiceImpl implements WelfareService {

	@Resource
	private WelfareDao welfareDao;
	
	@Resource
	private UserDao userDao;
	
	@Override
	public List<Welfare> list(WelfareQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return welfareDao.list(query, bounds);
	}

	@Override
	public Welfare get(Integer id) {
		return welfareDao.get(id);
	}

	@Override
	public void add(Welfare welfare) {
		welfareDao.add(welfare);
	}

	@Override
	public void delete(Integer id) {
		welfareDao.delete(id);
	}

	@Override
	public void update(Welfare welfare) {
		welfareDao.update(welfare);
	}

	@Override
	public Welfare getByUserId(Integer userId) {
		return welfareDao.getByUserId(userId);
	}
	
	@Override
	public void updateReceive(Integer userId) {
		Welfare welfare = welfareDao.getByUserId(userId);
		if (null == welfare) {
			welfare = new Welfare();
			welfare.setLastTime(new Date());
			welfare.setProgress(1);
			welfare.setUserId(userId);
			welfareDao.add(welfare);
		} else {
			welfare.setProgress(welfare.getProgress() + 1);
			welfare.setLastTime(new Date());
			welfareDao.update(welfare);
		}
		int progress = welfare.getProgress();
		int gold = 100;
		if (progress % 10 == 0) {
			gold = 150;
		}
		userDao.appendGold(userId, gold);
	}

}
