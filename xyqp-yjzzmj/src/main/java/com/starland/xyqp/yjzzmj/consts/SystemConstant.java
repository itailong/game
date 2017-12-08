package com.starland.xyqp.yjzzmj.consts;

import com.starland.xyqp.db.domain.User;

import io.netty.util.AttributeKey;

public interface SystemConstant {

	/**
	 * 登录用的key
	 */
	AttributeKey<User> USER_KEY = AttributeKey.valueOf("USER_KEY");
	
	/**
	 * token有效时间7天
	 */
	long TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000;
	
	/**
	 * 通过token登录
	 */
	String LOGIN_BY_TOKEN = "10002";
	
	/**
	 * 心跳
	 */
	String HEARTBEAT = "10004";
	
	/**
	 * 公告
	 */
	String SYSTEM_NOTICE = "10005";
	
	/**
	 * 获取语音token
	 */
	String VOICE_TOKEN = "10006";
}
