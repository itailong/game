package com.starland.xyqp.lobby.controller;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.lobby.domain.GameRoom;
import com.starland.xyqp.lobby.domain.GameServer;
import com.starland.xyqp.lobby.service.GameRoomService;
import com.starland.xyqp.lobby.service.GameServerService;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("gameServer/")
public class GameServerController {

	@Resource
	private GameServerService gameServerService;
	
	@Resource
	private GameRoomService gameRoomService;
	
	private Random random = new Random();
	
	@ResponseBody
	@RequestMapping("getByGameType")
	public Object getByGameType(@RequestParam("gameType") String gameType) {
		List<GameServer> gameServers = gameServerService.findByGameType(gameType);
		if (gameServers.isEmpty()) {
			return new AjaxResponse(400, "游戏类型不存在！");
		}
		int index = random.nextInt(gameServers.size());
		GameServer gameServer = gameServers.get(index);
		return new AjaxResponse(gameServer);
	}
	
	@ResponseBody
	@RequestMapping("getByRoomId")
	public Object getByRoomId(@RequestParam("roomId") String roomId) {
		GameRoom gameRoom = gameRoomService.get(roomId);
		if (null == gameRoom) {
			return new AjaxResponse(400, "房间不存在！");
		}
		Integer serverId = gameRoom.getServerId();
		GameServer gameServer = gameServerService.get(serverId);
		return new AjaxResponse(gameServer);
	}
	
}
