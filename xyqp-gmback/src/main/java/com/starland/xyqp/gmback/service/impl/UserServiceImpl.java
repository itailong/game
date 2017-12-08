package com.starland.xyqp.gmback.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.dao.AgentDao;
import com.starland.xyqp.gmback.dao.ModuleDao;
import com.starland.xyqp.gmback.dao.SystemConfigDao;
import com.starland.xyqp.gmback.dao.UserDao;
import com.starland.xyqp.gmback.dao.UserRoleDao;
import com.starland.xyqp.gmback.domain.Module;
import com.starland.xyqp.gmback.domain.SystemConfig;
import com.starland.xyqp.gmback.domain.User;
import com.starland.xyqp.gmback.exception.ParamException;
import com.starland.xyqp.gmback.query.UserQuery;
import com.starland.xyqp.gmback.service.UserService;
import com.starland.xyqp.gmback.utils.Md5Utils;
import com.starland.xyqp.gmback.vo.LoginInfo;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Resource
	private ModuleDao moduleDao;
	
	@Resource
	private SystemConfigDao systemConfigDao;
	
	@Resource
	private AgentDao agentDao;
	
	@Resource
	private UserRoleDao userRoleDao;
	
	@Override
	public List<User> list(UserQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return userDao.list(query, bounds);
	}

	@Override
	public User get(Integer id) {
		return userDao.get(id);
	}

	@Override
	public void add(User user) {
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int num = random.nextInt(10);
			buf.append(num);
		}
		String password = buf.toString();
		user.setPassword(Md5Utils.encoding(password));
		user.setUserType(3);
		user.setDiamond(0);
		user.setGold(0L);
		userDao.add(user);
		user.setPassword(password);
	}

	@Override
	public void delete(Integer id) {
		userDao.delete(id);
		agentDao.delete(id);
		userRoleDao.deleteByUserId(id);
	}

	@Override
	public void update(User user) {
		userDao.update(user);
	}
	
	@Override
	public LoginInfo getLoginInfo(Integer userId) {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setUserId(userId);
		Set<String> withinUrls = new HashSet<>();
		Set<String> withoutUrls = new HashSet<>();
		SystemConfig systemConfig = systemConfigDao.get("super_admin_id");
		Integer adminId = -1;
		if (null != systemConfig) {
			adminId = Integer.valueOf(systemConfig.getValue());
		}
		List<Module> modules = moduleDao.findAll();
		List<Module> moduleList = moduleDao.findByUserId(userId);
		List<Integer> ids = new ArrayList<>();
		for (Module module : moduleList) {
			ids.add(module.getId());
		}
		for (Module module : modules) {
			if (userId.equals(adminId) || ids.contains(module.getId())) {
				withinUrls.add(module.getUrl());
			} else {
				withoutUrls.add(module.getUrl());
			}
		}
		loginInfo.setWithinUrls(withinUrls);
		loginInfo.setWithoutUrls(withoutUrls);
		return loginInfo;
	}
	
	@Override
	public void updatePassword(Integer id, String password) {
		userDao.updatePassword(id, password);
	}
	
	@Override
	public void updatePayDiamond(Integer userId, Integer diamond, Integer operatorId) {
		User operator = userDao.get(operatorId);
		Integer selfDiamond = operator.getDiamond();
		if (selfDiamond < diamond) {
			throw new ParamException("对不起，您没有足够的星钻！");
		}
		userDao.appendDiamond(operatorId, -diamond);
		userDao.appendDiamond(userId, diamond);
	}

}
