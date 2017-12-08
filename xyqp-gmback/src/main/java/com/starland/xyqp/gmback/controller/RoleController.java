package com.starland.xyqp.gmback.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.Role;
import com.starland.xyqp.gmback.query.RoleQuery;
import com.starland.xyqp.gmback.service.RoleService;
import com.starland.xyqp.gmback.vo.AjaxResponse;

@Controller
@RequestMapping("role/")
public class RoleController {

	@Resource
	private RoleService roleService;
	
	@RequestMapping("list")
	public String list(RoleQuery query, PageInfo pageInfo, Model model) {
		List<Role> list = roleService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "role/list";
	}
	
	@ResponseBody
	@RequestMapping("getAsJson")
	public Object getAsJson(@RequestParam("id") Integer id) {
		Role role = roleService.get(id);
		return new AjaxResponse(role);
	}
	
	@RequestMapping(value="add", method=RequestMethod.GET)
	public String add() {
		return "role/add";
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String add(Role role) {
		roleService.add(role);
		return "redirect:/role/list";
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(@RequestParam("id") Integer id) {
		roleService.delete(id);
		return new AjaxResponse("删除成功！");
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public String update(@RequestParam("id") Integer id, Model model) {
		Role role = roleService.get(id);
		model.addAttribute("role", role);
		return "role/update";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update(Role role) {
		roleService.update(role);
		return "redirect:/role/list";
	}
	
}
