package com.starland.xyqp.lobby.dao;


import com.starland.xyqp.lobby.domain.Verification;


public interface VerificationDao {

	
	Verification get(String phone);
	
	void add(Verification verification);
	
	void update(Verification verification);
	
}
