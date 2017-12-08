package com.starland.xyqp.gmback.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.RoleModule;
import com.starland.xyqp.gmback.query.RoleModuleQuery;
import com.starland.xyqp.gmback.service.RoleModuleService;
import com.starland.xyqp.gmback.vo.AjaxResponse;
import com.starland.xyqp.gmback.vo.MultipleChoice;

@Controller
@RequestMapping("roleModule/")
public class RoleModuleController {

	@Resource
	private RoleModuleService roleModuleService;
	
	@RequestMapping(value="list", method=RequestMethod.GET)
	public String list(RoleModuleQuery query, PageInfo pageInfo, Model model) {
		List<RoleModule> list = roleModuleService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "roleModule/list";
	}
	
	@ResponseBody
	@RequestMapping(value="listAsJson", method=RequestMethod.GET)
	public Object listAsJson(RoleModuleQuery query, PageInfo pageInfo, Model model) {
		List<RoleModule> list = roleModuleService.list(query, pageInfo);
		Map<String, Object> data = new HashMap<>();
		if (pageInfo.isPageEnable()) {
			data.put("pageInfo", pageInfo);
		}
		data.put("list", list);
		return new AjaxResponse(data);
	}
	
	@RequestMapping(value="allot", method=RequestMethod.GET)
	public String allot(@RequestParam("roleId") Integer roleId, Model model) {
		MultipleChoice multipleChoice = roleModuleService.getMultipleChoice(roleId);
		model.addAttribute("multipleChoice", multipleChoice);
		model.addAttribute("roleId", roleId);
		return "role/allot";
	}
	
	@RequestMapping(value="allot", method=RequestMethod.POST)
	public String allot(MultipleChoice multipleChoice, Model model) {
		roleModuleService.updateMultipleChoice(multipleChoice);
		return "redirect:/role/list";
	}
	
}
