package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.TurntableDao;
import com.starland.xyqp.lobby.domain.Turntable;
import com.starland.xyqp.lobby.service.TurntableService;

@Service
public class TurntableServiceImpl implements TurntableService {

	@Resource
	private TurntableDao turntableDao;
	
	@Override
	public List<Turntable> list() {
		return turntableDao.list();
	}

	@Override
	public void add(Turntable turntable) {
		turntableDao.add(turntable);
	}

	@Override
	public void delete(Integer id) {
		turntableDao.delete(id);
	}

	@Override
	public void update(Turntable turntable) {
		turntableDao.update(turntable);
	}

	@Override
	public Turntable getByUserId(Integer user_id) {
		return turntableDao.get(user_id);
	}

	@Override
	public List<Turntable> getSparrowWeek() {
		return turntableDao.getSparrowWeek();
	}

	@Override
	public List<Turntable> getSparrowMonth() {
		return turntableDao.getSparrowMonth();
	}

	@Override
	public List<Turntable> getNegativeWeek() {
		return turntableDao.getNegativeWeek();
	}

	@Override
	public List<Turntable> getNegativeMonth() {
		return turntableDao.getNegativeMonth();
	}

	@Override
	public List<Turntable> getRoomCardWeek() {
		return turntableDao.getRoomCardWeek();
	}

	@Override
	public List<Turntable> getRoomCardMonth() {
		return turntableDao.getRoomCardMonth();
	}


}
