package com.starland.xyqp.lobby.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.lobby.dao.VerificationDao;
import com.starland.xyqp.lobby.domain.Verification;
import com.starland.xyqp.lobby.service.VerificationService;

@Service
public class VerificationServiceImpl implements VerificationService {

	@Resource
	private VerificationDao verificationDao;
	

	@Override
	public Verification get(String phone) {
		return verificationDao.get(phone);
	}

	@Override
	public void add(Verification verification) {
		verificationDao.add(verification);
	}


	@Override
	public void update(Verification verification) {
		verificationDao.update(verification);
	}

}
