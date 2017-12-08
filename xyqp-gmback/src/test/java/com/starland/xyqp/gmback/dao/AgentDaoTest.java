package com.starland.xyqp.gmback.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.query.AgentQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class AgentDaoTest {

	@Resource
	private AgentDao agentDao;
	
	@Test
	public void getWithUser() {
		Agent agent = agentDao.getWithUser(18888);
		System.out.println(agent);
	}
	
	@Test
	public void findWithUser() {
		AgentQuery query = new AgentQuery();
		PageBounds bounds = new PageBounds(new PageInfo());
		List<Agent> agents = agentDao.findWithUser(query, bounds);
		System.out.println(agents);
	}
	
}
