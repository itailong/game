package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.NoticeDao;
import com.starland.xyqp.lobby.domain.Notice;
import com.starland.xyqp.lobby.service.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Resource
	private NoticeDao noticeDao;
	
	@Override
	public List<Notice> list() {
		return noticeDao.list();
	}

	@Override
	public Notice get(Integer id) {
		return noticeDao.get(id);
	}

	@Override
	public void add(Notice notice) {
		noticeDao.add(notice);
	}

	@Override
	public void delete(Integer id) {
		noticeDao.delete(id);
	}

	@Override
	public void update(Notice notice) {
		noticeDao.update(notice);
	}

}
