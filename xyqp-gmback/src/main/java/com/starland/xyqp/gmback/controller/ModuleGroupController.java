package com.starland.xyqp.gmback.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.gmback.domain.ModuleGroup;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.ModuleGroupQuery;
import com.starland.xyqp.gmback.service.ModuleGroupService;
import com.starland.xyqp.gmback.vo.AjaxResponse;

@Controller
@RequestMapping("moduleGroup/")
public class ModuleGroupController {

	@Resource
	private ModuleGroupService moduleGroupService;
	
	@RequestMapping("list")
	public String list(ModuleGroupQuery query, PageInfo pageInfo, Model model) {
		List<ModuleGroup> list = moduleGroupService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "moduleGroup/list";
	}
	
	@ResponseBody
	@RequestMapping("getAsJson")
	public Object getAsJson(@RequestParam("id") Integer id) {
		ModuleGroup moduleGroup = moduleGroupService.get(id);
		return new AjaxResponse(moduleGroup);
	}
	
	@ResponseBody
	@RequestMapping("add")
	public Object add(ModuleGroup moduleGroup) {
		moduleGroupService.add(moduleGroup);
		return new AjaxResponse("添加成功！");
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(@RequestParam("id") Integer id) {
		moduleGroupService.delete(id);
		return new AjaxResponse("删除成功！");
	}
	
	@ResponseBody
	@RequestMapping("update")
	public Object update(ModuleGroup moduleGroup) {
		moduleGroupService.update(moduleGroup);
		return new AjaxResponse("修改成功！");
	}
	
}
