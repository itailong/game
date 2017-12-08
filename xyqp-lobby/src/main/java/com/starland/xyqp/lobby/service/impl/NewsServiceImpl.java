package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.NewsDao;
import com.starland.xyqp.lobby.domain.News;
import com.starland.xyqp.lobby.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

	@Resource
	private NewsDao newsDao;
	
	@Override
	public List<News> list() {
		return newsDao.list();
	}
}
