package com.starland.xyqp.lobby.service;

import com.starland.xyqp.lobby.domain.Invitation;

public interface InvitationService {

	Invitation get(Integer invitationCode);
	
	void add(Invitation invitation);
	
	void update (Invitation invitation);
	
}
