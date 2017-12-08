package com.starland.xyqp.lobby.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.starland.xyqp.lobby.domain.News;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.service.NewsService;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.vo.AjaxResponse;
@Controller
@RequestMapping("/news")
public class NewsController {

	private static final Logger LOGGER = LogManager.getLogger(NewsController.class);
	
	@Resource
	private NewsService newsService;
	
	@Resource 
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="/new", method=RequestMethod.POST)
	public Object news(@RequestParam("token") String token) {
		if(null == token || "" == token){
			LOGGER.error("传入的token错误");
			return new AjaxResponse(501,"传入token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取消息失败 没有对应的Token");
			return new AjaxResponse(502,"获取失败 没有相应的token");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token登录已过期");
			return new AjaxResponse(503,"token登录过期");
		}
		List<News> list = newsService.list();
		if(null == list){
			LOGGER.error("获取消息失败");
			return new AjaxResponse(504,"获取消息失败");
		}
		if(list.size() == 0){
			LOGGER.error("数据库没有相关消息");
			return new AjaxResponse(505,"数据库无相关信息");
		}
		return new AjaxResponse(list);
	}
}
