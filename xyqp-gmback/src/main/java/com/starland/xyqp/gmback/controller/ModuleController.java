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
import com.starland.xyqp.gmback.domain.Module;
import com.starland.xyqp.gmback.query.ModuleQuery;
import com.starland.xyqp.gmback.service.ModuleService;
import com.starland.xyqp.gmback.vo.AjaxResponse;

@Controller
@RequestMapping("module/")
public class ModuleController {

	@Resource
	private ModuleService moduleService;
	
	@RequestMapping("list")
	public String list(ModuleQuery query, PageInfo pageInfo, Model model) {
		List<Module> list = moduleService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "module/list";
	}
	
	@ResponseBody
	@RequestMapping("getAsJson")
	public Object getAsJson(@RequestParam("id") Integer id) {
		Module module = moduleService.get(id);
		return new AjaxResponse(module);
	}
	
	@RequestMapping(value="add", method=RequestMethod.GET)
	public String add() {
		return "module/add";
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String add(Module module) {
		moduleService.add(module);
		return "redirect:/module/list";
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(@RequestParam("id") Integer id) {
		moduleService.delete(id);
		return new AjaxResponse("删除成功！");
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public String update(@RequestParam("id") Integer id, Model model) {
		Module module = moduleService.get(id);
		model.addAttribute("module", module);
		return "module/update";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update(Module module) {
		moduleService.update(module);
		return "redirect:/module/list";
	}
	
}
