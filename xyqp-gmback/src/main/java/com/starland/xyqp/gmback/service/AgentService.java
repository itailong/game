package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.query.AgentQuery;

public interface AgentService {

	List<Agent> list(AgentQuery query, PageInfo pageInfo);
	
	Agent get(Integer id);
	
	void add(Agent agent);
	
	void delete(Integer id);
	
	void update (Agent agent);
	
	Agent getWithUser(Integer id);
	
	List<Agent> findWithUser(AgentQuery query, PageInfo pageInfo);

}
