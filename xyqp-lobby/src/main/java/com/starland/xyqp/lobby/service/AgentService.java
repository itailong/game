package com.starland.xyqp.lobby.service;

import java.util.List;

import com.starland.xyqp.lobby.domain.Agent;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.lobby.query.AgentQuery;

public interface AgentService {

	List<Agent> list(AgentQuery query, PageInfo pageInfo);
	
	Agent get(Integer id);
	
	void add(Agent agent);
	
	void delete(Integer id);
	
	void update (Agent agent);
	
}
