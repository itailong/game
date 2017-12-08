package com.starland.xyqp.lobby.dao;



import com.starland.xyqp.lobby.domain.Invitation;

public interface InvitationDao {

	
	Invitation get(Integer invitationCode);
	
	void add(Invitation invitation);
	
	void update(Invitation invitation);
	
}
