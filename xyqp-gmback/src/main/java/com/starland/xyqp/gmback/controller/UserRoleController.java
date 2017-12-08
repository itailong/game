package com.starland.xyqp.gmback.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.starland.xyqp.gmback.service.UserRoleService;
import com.starland.xyqp.gmback.vo.MultipleChoice;

@Controller
@RequestMapping("userRole/")
public class UserRoleController {

	@Resource
	private UserRoleService userRoleService;
	
	@RequestMapping(value="allot", method=RequestMethod.GET)
	public String allot(@RequestParam("userId") Integer userId, Model model) {
		MultipleChoice multipleChoice = userRoleService.getMultipleChoice(userId);
		model.addAttribute("multipleChoice", multipleChoice);
		model.addAttribute("userId", userId);
		return "user/allot";
	}
	
	@RequestMapping(value="allot", method=RequestMethod.POST)
	public String allot(MultipleChoice multipleChoice, Model model) {
		userRoleService.updateMultipleChoice(multipleChoice);
		return "redirect:/user/list";
	}
	
}
