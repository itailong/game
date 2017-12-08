package com.starland.xyqp.gmback.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.GoldIncome;
import com.starland.xyqp.gmback.query.GoldIncomeQuery;
import com.starland.xyqp.gmback.service.GoldIncomeService;
import com.starland.xyqp.gmback.vo.LoginInfo;

@Controller
@RequestMapping("goldIncome/")
public class GoldIncomeController {

	@Resource
	private GoldIncomeService goldIncomeService;
	
	@RequestMapping("list")
	public String list(GoldIncomeQuery query, PageInfo pageInfo, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		query.setUserId(loginInfo.getUserId());
		List<GoldIncome> list = goldIncomeService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "goldIncome/list";
	}
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
