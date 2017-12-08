package com.starland.xyqp.lobby.service;

import java.util.List;

import com.starland.xyqp.lobby.domain.Welfare;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.lobby.query.WelfareQuery;

public interface WelfareService {

	List<Welfare> list(WelfareQuery query, PageInfo pageInfo);
	
	Welfare get(Integer id);
	
	void add(Welfare welfare);
	
	void delete(Integer id);
	
	void update (Welfare welfare);
	
	Welfare getByUserId(Integer userId);

	void updateReceive(Integer userId);
	
}
