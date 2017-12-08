package com.starland.xyqp.gmback.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.query.AgentQuery;

public interface AgentDao {

	List<Agent> list(AgentQuery query, PageBounds bounds);
	
	Agent get(Integer id);
	
	void add(Agent agent);
	
	void delete(Integer id);
	
	void update (Agent agent);
	
	Agent getWithUser(Integer id);
	
	List<Agent> findWithUser(AgentQuery query, PageBounds bounds);
	
	/**
	 * 添加代理商的余额
	 * @param id
	 * @param money
	 * @return
	 */
	int appendMoney(@Param("id") Integer id, @Param("money") BigDecimal money);
	
}
