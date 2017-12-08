package com.starland.xyqp.gmback.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.starland.tools.page.PageBounds;
import com.starland.xyqp.gmback.domain.ExtractDeposit;
import com.starland.xyqp.gmback.query.ExtractDepositQuery;

public interface ExtractDepositDao {

	List<ExtractDeposit> list(ExtractDepositQuery query, PageBounds bounds);
	
	ExtractDeposit get(Integer id);
	
	void add(ExtractDeposit extractDeposit);
	
	void delete(Integer id);
	
	void update(ExtractDeposit extractDeposit);
	
	void updateSuccess(Integer id);
	
	void updateFail(@Param("id") Integer id, @Param("remark") String remark);
	
}
