package com.starland.xyqp.lobby.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.InvitationDao;
import com.starland.xyqp.lobby.domain.Invitation;
import com.starland.xyqp.lobby.service.InvitationService;

@Service
public class InvitationServiceImpl implements InvitationService {

	@Resource
	private InvitationDao invitationDao;
	

	@Override
	public Invitation get(Integer invitationCode) {
		return invitationDao.get(invitationCode);
	}

	@Override
	public void add(Invitation invitation) {
		invitationDao.add(invitation);
	}

	@Override
	public void update(Invitation invitation) {
		invitationDao.update(invitation);
	}

}
