package com.starland.xyqp.lobby.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.lobby.domain.Question;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.service.QuestionService;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("/question")
public class QuestionController {

	private static final Logger LOGGER = LogManager.getLogger(QuestionController.class);

	@Resource
	private QuestionService questionService;

	@Resource
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = "/addquestion", method = RequestMethod.POST)
	public Object addquestion(@RequestParam("token") String token, @RequestParam("questionContent") String questionContent,@RequestParam("phone") String phone) {
		if (null == token || "" == token) {
			LOGGER.error("传入的token错误");
			return new AjaxResponse(501, "传入token错误");
		}
		User user = userService.getByToken(token);
		if (null == user) {
			LOGGER.error("获取用户信息失败");
			return new AjaxResponse(502, "获取用户登录信息失败");
		}
		if ((user.getTokenTime().getTime()) < System.currentTimeMillis()) {
			LOGGER.error("token登录过期");
			return new AjaxResponse(503, "token登录过期");
		}
		if(null == phone || phone.length() < 11){
			LOGGER.error("手机号码为空");
			return new AjaxResponse(504,"手机号错误");
		}
		if(!phone.matches("1(3|5|7|8)[0-9]{9}")){
			LOGGER.error("手机号码格式不正确");
			return new AjaxResponse(505,"手机号格式不正确");
		}
		if(null == questionContent || "" == questionContent){
			LOGGER.error("内容不能为空");
			return new AjaxResponse(506,"内容不能为空");
		}
		Question question = new Question();
		question.setPhone(phone);
		question.setQuestionContent(questionContent);
		question.setUserId(user.getId());
		question.setQuestionDate(new Date());
		questionService.add(question);
		return new AjaxResponse(200,"添加成功");
	}
}
