package com.starland.xyqp.lobby.dao;

import java.util.List;

import com.starland.xyqp.lobby.domain.Welfare;
import com.starland.tools.page.PageBounds;
import com.starland.xyqp.lobby.query.WelfareQuery;

public interface WelfareDao {

	List<Welfare> list(WelfareQuery query, PageBounds bounds);
	
	Welfare get(Integer id);
	
	void add(Welfare welfare);
	
	void delete(Integer id);
	
	void update (Welfare welfare);
	
	Welfare getByUserId(Integer userId);
	
}
