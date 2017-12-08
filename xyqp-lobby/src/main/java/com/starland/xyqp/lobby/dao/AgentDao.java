package com.starland.xyqp.lobby.dao;

import java.util.List;

import com.starland.xyqp.lobby.domain.Agent;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.lobby.query.AgentQuery;

public interface AgentDao {

	List<Agent> list(AgentQuery query, PageBounds bounds);
	
	Agent get(Integer id);
	
	void add(Agent agent);
	
	void delete(Integer id);
	
	void update (Agent agent);
	
}
