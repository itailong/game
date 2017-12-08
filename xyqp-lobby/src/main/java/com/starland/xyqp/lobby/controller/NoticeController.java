package com.starland.xyqp.lobby.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.lobby.domain.Notice;
import com.starland.xyqp.lobby.service.NoticeService;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("notice/")
public class NoticeController {

	@Resource
	private NoticeService noticeService;
	
	@ResponseBody
	@RequestMapping("list")
	public Object list() {
		List<Notice> list = noticeService.list();
		return new AjaxResponse(list);
	}
	
}
