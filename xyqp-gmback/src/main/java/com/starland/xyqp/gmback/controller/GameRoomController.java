package com.starland.xyqp.gmback.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.GameRoom;
import com.starland.xyqp.gmback.query.GameRoomQuery;
import com.starland.xyqp.gmback.service.GameRoomService;

@Controller
@RequestMapping("gameRoom/")
public class GameRoomController {

	@Resource
	private GameRoomService gameRoomService;
	
	@RequestMapping("list")
	public String list(GameRoomQuery query, PageInfo pageInfo, Model model) {
		List<GameRoom> list = gameRoomService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "gameRoom/list";
	}
	
}
