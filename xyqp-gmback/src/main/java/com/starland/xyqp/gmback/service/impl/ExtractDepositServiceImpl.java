package com.starland.xyqp.gmback.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.tools.page.PageBounds;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.dao.AgentDao;
import com.starland.xyqp.gmback.dao.ExtractDepositDao;
import com.starland.xyqp.gmback.domain.ExtractDeposit;
import com.starland.xyqp.gmback.exception.ParamException;
import com.starland.xyqp.gmback.query.ExtractDepositQuery;
import com.starland.xyqp.gmback.service.ExtractDepositService;

@Service
public class ExtractDepositServiceImpl implements ExtractDepositService {

	@Resource
	private ExtractDepositDao extractDepositDao;
	
	@Resource
	private AgentDao agentDao;
	
	@Override
	public List<ExtractDeposit> list(ExtractDepositQuery query, PageInfo pageInfo) {
		PageBounds bounds = new PageBounds(pageInfo);
		return extractDepositDao.list(query, bounds);
	}

	@Override
	public ExtractDeposit get(Integer id) {
		return extractDepositDao.get(id);
	}

	@Override
	public void add(ExtractDeposit extractDeposit) {
		Integer agentId = extractDeposit.getAgentId();
		extractDeposit.setCreateTime(new Date());
		extractDeposit.setStatus(1);
		BigDecimal money = extractDeposit.getMoney();
		int count = agentDao.appendMoney(agentId, money.negate());
		if (count <= 0) {
			throw new ParamException("数据异常！");
		}
		extractDepositDao.add(extractDeposit);
	}

	@Override
	public void delete(Integer id) {
		extractDepositDao.delete(id);
	}

	@Override
	public void update(ExtractDeposit extractDeposit) {
		Integer id = extractDeposit.getId();
		ExtractDeposit deposit = extractDepositDao.get(id);
		Integer status = deposit.getStatus();
		if (null == status || status.intValue() != 3) {
			throw new ParamException("数据异常！");
		}
		extractDeposit.setStatus(1);
		extractDepositDao.update(extractDeposit);
	}

	@Override
	public void updateSuccess(Integer id) {
		extractDepositDao.updateSuccess(id);
	}

	@Override
	public void updateFail(Integer id, String remark) {
		extractDepositDao.updateFail(id, remark);
	}

}
