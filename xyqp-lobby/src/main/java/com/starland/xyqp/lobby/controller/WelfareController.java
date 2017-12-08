package com.starland.xyqp.lobby.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.domain.Welfare;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.service.WelfareService;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("welfare/")
public class WelfareController {

	@Resource
	private WelfareService welfareService;
	
	@Resource
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("detail")
	public Object detail(@RequestParam("token") String token) {
		User user = userService.getByToken(token);
		if (null == user) {
			return new AjaxResponse(400, "无效token！");
		}
		Map<String, Object> result = new HashMap<>();
		Welfare welfare = welfareService.getByUserId(user.getId());
		if (null == welfare) {
			result.put("progress", 0);
			result.put("enable", true);
		} else {
			result.put("progress", welfare.getProgress());
			Date lastTime = welfare.getLastTime();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			long now = calendar.getTimeInMillis();
			calendar.setTime(lastTime);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			long last = calendar.getTimeInMillis();
			boolean enable = now > last;
			if (welfare.getProgress() >= 30) {
				enable = false;
			}
			result.put("enable", enable);
		}
		return new AjaxResponse(result);
	}
	
	@ResponseBody
	@RequestMapping("receive")
	public Object receive(@RequestParam("token") String token) {
		User user = userService.getByToken(token);
		if (null == user) {
			return new AjaxResponse(400, "无效token！");
		}
		Welfare welfare = welfareService.getByUserId(user.getId());
		if (null != welfare) {
			Date lastTime = welfare.getLastTime();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			long now = calendar.getTimeInMillis();
			calendar.setTime(lastTime);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			long last = calendar.getTimeInMillis();
			boolean enable = now > last;
			if (welfare.getProgress() >= 30) {
				enable = false;
			}
			if (!enable) {
				return new AjaxResponse(400, "你今天已经领取过了！");
			}
		}
		welfareService.updateReceive(user.getId());
		return new AjaxResponse("领取成功！");
	}
	
}
