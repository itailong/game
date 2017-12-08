package com.starland.xyqp.lobby.service;

import java.util.List;

import com.starland.xyqp.lobby.domain.Turntable;

public interface TurntableService {

	List<Turntable> list();
	
	Turntable getByUserId(Integer user_id);
	
	void add(Turntable turntable);
	
	void delete(Integer id);
	
	void update (Turntable turntable);

	List<Turntable> getSparrowWeek();
	
	List<Turntable> getSparrowMonth();
	
	List<Turntable> getNegativeWeek();
	
	List<Turntable> getNegativeMonth();
	
	List<Turntable> getRoomCardWeek();
	
	List<Turntable> getRoomCardMonth();
	
}
