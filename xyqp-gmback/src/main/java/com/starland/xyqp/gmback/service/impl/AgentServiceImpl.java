package com.starland.xyqp.gmback.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.dao.AgentDao;
import com.starland.xyqp.gmback.dao.SystemConfigDao;
import com.starland.xyqp.gmback.dao.UserDao;
import com.starland.xyqp.gmback.dao.UserRoleDao;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.domain.SystemConfig;
import com.starland.xyqp.gmback.domain.User;
import com.starland.xyqp.gmback.domain.UserRole;
import com.starland.xyqp.gmback.query.AgentQuery;
import com.starland.xyqp.gmback.service.AgentService;
import com.starland.xyqp.gmback.utils.Md5Utils;

@Service
public class AgentServiceImpl implements AgentService {

	@Resource
	private AgentDao agentDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private SystemConfigDao systemConfigDao;
	
	@Resource
	private UserRoleDao userRoleDao;
	
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
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int num = random.nextInt(10);
			buf.append(num);
		}
		String password = buf.toString();
		User user = new User();
		user.setPassword(Md5Utils.encoding(password));
		user.setCreateTime(new Date());
		user.setUserType(3);
		user.setDiamond(0);
		user.setGold(0L);
		userDao.add(user);
		agent.setId(user.getId());
		agent.setMoney(new BigDecimal(0));
		agent.setTotalIncome(new BigDecimal(0));
		agent.setCreateTime(new Date());
		agentDao.add(agent);
		SystemConfig systemConfig = systemConfigDao.get("agent_role_id");
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		userRole.setRoleId(Integer.valueOf(systemConfig.getValue()));
		userRoleDao.add(userRole);
		
		user.setPassword(password);
		agent.setUser(user);
	}

	@Override
	public void delete(Integer id) {
		agentDao.delete(id);
	}

	@Override
	public void update(Agent agent) {
		agentDao.update(agent);
	}

	@Override
	public Agent getWithUser(Integer id) {
		return agentDao.getWithUser(id);
	}

	@Override
	public List<Agent> findWithUser(AgentQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return agentDao.findWithUser(query, bounds);
	}
	
}
