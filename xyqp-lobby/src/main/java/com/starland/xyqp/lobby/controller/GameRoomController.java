package com.starland.xyqp.lobby.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.lobby.domain.GameRoom;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.service.GameRoomService;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("gameRoom/")
public class GameRoomController {

	@Resource
	private GameRoomService gameRoomService;
	
	@Resource
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("list")
	public Object list(@RequestParam("token") String token) {
		User user = userService.getByToken(token);
		if (null == user) {
			new AjaxResponse(500, "token无效");
		}
		List<GameRoom> list = gameRoomService.findByCreatorId(user.getId());
		return new AjaxResponse(list);
	}
	
}
