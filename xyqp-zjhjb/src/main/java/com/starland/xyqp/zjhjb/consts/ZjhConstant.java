package com.starland.xyqp.zjhjb.consts;

import com.starland.xyqp.db.domain.User;

import io.netty.util.AttributeKey;

public interface ZjhConstant {

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
	 * 准备
	 */
	String READY = "20005";
	
	/**
	 * 发牌
	 */
	String ALLOCATE_CARD = "20006";
	
	/**
	 * 显示操作面板
	 */
	String SHOW_OPERATE_VIEW = "20007";

	/**
	 * 跟注
	 */
	String ANTE = "20008";
	
	/**
	 * 加注
	 */
	String ADD_ANTE = "20009";
	
	/**
	 * 看牌
	 */
	String LOOK_CARD = "20010";
	
	/**
	 * 向其他玩家响应看牌
	 */
	String LOOK_CARD_OTHER = "20011";
	
	/**
	 * 弃牌
	 */
	String GIVE_UP_CARD = "20012";
	
	/**
	 * 比牌
	 */
	String COMPARE_CARD = "20013";
	
	/**
	 * 开牌
	 */
	String OPEN_CARD = "20014";
	
	/**
	 * 向其他玩家响应操作面板的位置
	 */
	String OPERATE_VIEW_OTHER = "20015";
	
	/**
	 * 离线
	 */
	String LEAVE_LINE = "20016";
	
	/**
	 * 上线 
	 */
	String UP_LINE = "20017";
	
	/**
	 * 结算
	 */
	String SETTLE = "20018";
	
	/**
	 * 断线重连
	 */
	String RECONNECTION = "20019";
	
	/**
	 * 换桌
	 */
	String CHANGE_TABLE = "20020";
	
	/**
	 * 启动房间定时器
	 */
	String START_ROOM_SCHEDULE = "20021";
	
	/**
	 * 取消房间定时器
	 */
	String CANCEL_ROOM_SCHEDULE = "20022";
	
	/**
	 * 聊天
	 */
	String CHAT = "20023";
}
