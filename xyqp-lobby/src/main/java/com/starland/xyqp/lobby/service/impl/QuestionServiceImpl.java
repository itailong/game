package com.starland.xyqp.lobby.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.QuestionDao;
import com.starland.xyqp.lobby.domain.Question;
import com.starland.xyqp.lobby.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Resource
	private QuestionDao questionDao;
	

	@Override
	public void add(Question question) {
		questionDao.add(question);
	}

}
