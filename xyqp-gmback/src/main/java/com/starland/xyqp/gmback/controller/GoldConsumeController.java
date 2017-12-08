package com.starland.xyqp.gmback.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.GoldConsume;
import com.starland.xyqp.gmback.query.GoldConsumeQuery;
import com.starland.xyqp.gmback.service.GoldConsumeService;

@Controller
@RequestMapping("goldConsume/")
public class GoldConsumeController {

	@Resource
	private GoldConsumeService goldConsumeService;
	
	@RequestMapping("list")
	public String list(GoldConsumeQuery query, PageInfo pageInfo, Model model) {
		List<GoldConsume> list = goldConsumeService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "goldConsume/list";
	}
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
