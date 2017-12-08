package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.ExtractDeposit;
import com.starland.xyqp.gmback.query.ExtractDepositQuery;

public interface ExtractDepositService {

	List<ExtractDeposit> list(ExtractDepositQuery query, PageInfo pageInfo);
	
	ExtractDeposit get(Integer id);
	
	void add(ExtractDeposit extractDeposit);
	
	void delete(Integer id);
	
	void update (ExtractDeposit extractDeposit);
	
	void updateSuccess(Integer id);
	
	void updateFail(Integer id, String remark);
	
}
