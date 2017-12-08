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
import com.starland.xyqp.gmback.domain.DiamondConsume;
import com.starland.xyqp.gmback.query.DiamondConsumeQuery;
import com.starland.xyqp.gmback.service.DiamondConsumeService;

@Controller
@RequestMapping("diamondConsume/")
public class DiamondConsumeController {

	@Resource
	private DiamondConsumeService diamondConsumeService;
	
	@RequestMapping("list")
	public String list(DiamondConsumeQuery query, PageInfo pageInfo, Model model) {
		List<DiamondConsume> list = diamondConsumeService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "diamondConsume/list";
	}
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
