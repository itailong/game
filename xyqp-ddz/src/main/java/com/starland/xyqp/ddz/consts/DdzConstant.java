package com.starland.xyqp.ddz.consts;

import com.starland.xyqp.db.domain.User;

import io.netty.util.AttributeKey;

public interface DdzConstant {

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
	 * 创建房间
	 */
	String CREATE_ROOM = "20001";
	
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
	 * 提示叫地主
	 */
	String HINT_CALL_LANDLORD = "20008";
	
	/**
	 * 叫地主
	 */
	String CALL_LANDLORD = "20009";
	
	/**
	 * 不叫
	 */
	String PASS_LANDLORD = "20010";
	
	/**
	 * 产生地主
	 */
	String MAKE_LANDLORD = "20011";
	
	/**
	 * 发牌
	 */
	String ALLOCATE_CARD = "20012";
	
	/**
	 * 提示出牌
	 */
	String HINT_OUT_CARD = "20013";
	
	/**
	 * 出牌
	 */
	String OUT_CARD = "20014";

	/**
	 * 过
	 */
	String PASS_CARD = "20015";
	
	/**
	 * 准备
	 */
	String READY = "20016";
	
	/**
	 * 小结算
	 */
	String SMALL_SETTLE = "20017";
	
	/**
	 * 大结算
	 */
	String BIG_SETTLE = "20018";
	
	/**
	 * 申请解散房间
	 */
	String APPLY_DISSOLVE_ROOM = "20019";
	
	/**
	 * 解散房间
	 */
	String DISSOLVE_ROOM = "20020";
	
	/**
	 * 聊天
	 */
	String CHAT = "20021";
	
	/**
	 * 替人创建房间
	 */
	String INSTEAD_CREATE_ROOM = "20022";
}
