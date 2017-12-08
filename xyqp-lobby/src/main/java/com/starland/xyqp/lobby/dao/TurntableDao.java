package com.starland.xyqp.lobby.dao;

import java.util.List;

import com.starland.xyqp.lobby.domain.Turntable;

public interface TurntableDao {

	List<Turntable> list();
	
	Turntable get(Integer user_id);
	
	void add(Turntable turntable);
	
	void delete(Integer id);
	
	void update(Turntable turntable);
	
	List<Turntable> getSparrowWeek();
	
	List<Turntable> getSparrowMonth();
	
	List<Turntable> getNegativeWeek();
	
	List<Turntable> getNegativeMonth();
	
	List<Turntable> getRoomCardWeek();
	
	List<Turntable> getRoomCardMonth();
}
