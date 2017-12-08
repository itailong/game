package com.starland.xyqp.lobby.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.AgentDao;
import com.starland.xyqp.lobby.domain.Agent;
import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.lobby.query.AgentQuery;
import com.starland.xyqp.lobby.service.AgentService;

@Service
public class AgentServiceImpl implements AgentService {

	@Resource
	private AgentDao agentDao;
	
	@Override
	public List<Agent> list(AgentQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return agentDao.list(query, bounds);
	}

	@Override
	public Agent get(Integer id) {
		return agentDao.get(id);
	}

	@Override
	public void add(Agent agent) {
		agentDao.add(agent);
	}

	@Override
	public void delete(Integer id) {
		agentDao.delete(id);
	}

	@Override
	public void update(Agent agent) {
		agentDao.update(agent);
	}

}
