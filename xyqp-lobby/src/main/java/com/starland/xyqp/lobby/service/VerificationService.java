package com.starland.xyqp.lobby.service;


import com.starland.xyqp.lobby.domain.Verification;

public interface VerificationService {

	Verification get(String phone);
	
	void add(Verification verification);
	
	void update (Verification verification);
	
}
