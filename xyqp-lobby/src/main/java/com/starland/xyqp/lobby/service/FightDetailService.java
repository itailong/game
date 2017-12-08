package com.starland.xyqp.lobby.service;

import java.util.List;

import com.starland.xyqp.lobby.domain.FightDetail;

public interface FightDetailService {

	List<FightDetail> findByExploitsIds(Integer[] ids);
	
}
