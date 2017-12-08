package com.starland.xyqp.lobby.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.starland.xyqp.lobby.domain.FightDetail;

public interface FightDetailDao {

	List<FightDetail> findByExploitsIds(@Param("ids") Integer[] ids);
	
}
