package com.starland.xyqp.gmback.helper;

import java.io.Serializable;

public class VerifyCodeHelper implements Serializable {

	private static final long serialVersionUID = 3447302513823903139L;

	private String code;
	
	private String telephone;
	
	private long time;

	public VerifyCodeHelper(String code, String telephone) {
		this.code = code;
		this.telephone = telephone;
		this.time = System.currentTimeMillis() + 3 * 60 * 1000;
	}
	
	public boolean verify(String code, String telephone) {
		if (time < System.currentTimeMillis()) {
			return false;
		}
		if (!this.code.equals(code)) {
			return false;
		}
		if (!this.telephone.equals(telephone)) {
			return false;
		}
		return true;
	}
	
}
