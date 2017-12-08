package com.starland.xyqp.niuniujb.consts;

import com.starland.xyqp.db.domain.User;

import io.netty.util.AttributeKey;

public interface NiuniujbConstant {

	AttributeKey<User> USER_KEY = AttributeKey.valueOf("USER_KEY");
	
	/**
	 * 通过token登录
	 */
	String LOGIN_BY_TOKEN = "10001";
	
	/**
	 * 心跳
	 */
	String HEARTBEAT = "10002";
	
	/**
	 * 加入房间
	 */
	String JOIN_ROOM = "20002";
	
	/**
	 * 其他人加入房间
	 */
	String JOIN_ROOM_OTHER = "20003";
	
	/**
	 * 离开房间
	 */
	String LEAVE_ROOM = "20004";
	
	/**
	 * 断线重连
	 */
	String RECONNECTION = "20005";
	
	/**
	 * 上线
	 */
	String UP_LINE = "20006";
	
	/**
	 * 离线
	 */
	String LEAVE_LINE = "20007";
	
	/**
	 * 准备
	 */
	String READY = "20008";
	
	/**
	 * 响应抢庄面板
	 */
	String GRAB_BANKER_VIEW = "20009";
	
	/**
	 * 抢庄
	 */
	String GRAB_BANKER = "20010";
	
	/**
	 * 响应庄家的位置
	 */
	String BANKER_POSITIOIN = "20011";
	
	/**
	 * 响应下注面板
	 */
	String CASE_ANTE_VIEW = "20012";
	
	/**
	 * 下注
	 */
	String CASE_ANTE = "20013";
	
	/**
	 * 发牌
	 */
	String ALLOCATE_CARD = "20014";
	
	/**
	 * 开牌面板
	 */
	String OPEN_CARD_VIEW = "20015";
	
	/**
	 * 开牌
	 */
	String OPEN_CARD = "20016";
	
	/**
	 * 结算
	 */
	String SETTLE = "20017";
	
	/**
	 * 启动房间定时器
	 */
	String START_ROOM_SCHEDULE = "20018";
	
	/**
	 * 取消房间定时器
	 */
	String CANCEL_ROOM_SCHEDULE = "20018";
	
	/**
	 * 聊天
	 */
	String CHAT = "20019";
}
