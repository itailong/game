package com.starland.xyqp.gmback.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.domain.SystemConfig;
import com.starland.xyqp.gmback.exception.ParamException;
import com.starland.xyqp.gmback.helper.VerifyCodeHelper;
import com.starland.xyqp.gmback.query.AgentQuery;
import com.starland.xyqp.gmback.service.AgentService;
import com.starland.xyqp.gmback.service.SystemConfigService;
import com.starland.xyqp.gmback.vo.LoginInfo;

@Controller
@RequestMapping("agent/")
public class AgentController {

	@Resource
	private AgentService agentService;
	
	@Resource
	private SystemConfigService systemConfigService;
	
	@RequestMapping("list")
	public String list(AgentQuery query, PageInfo pageInfo, Model model) {
		List<Agent> list = agentService.findWithUser(query, pageInfo);
		model.addAttribute("list", list);
		return "agent/list";
	}
	
	@RequestMapping("twoList")
	public String twoList(AgentQuery query, PageInfo pageInfo, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		query.setUpperId(loginInfo.getUserId());
		List<Agent> list = agentService.findWithUser(query, pageInfo);
		model.addAttribute("list", list);
		return "agent/twoList";
	}
	
	@RequestMapping("threeList")
	public String threeList(AgentQuery query, PageInfo pageInfo, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		query.setUpperTwoId(loginInfo.getUserId());
		List<Agent> list = agentService.findWithUser(query, pageInfo);
		model.addAttribute("list", list);
		return "agent/threeList";
	}
	
	@RequestMapping(value="add", method=RequestMethod.GET)
	public String add() {
		return "agent/add";
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String add(Agent agent, Model model) {
		agentService.add(agent);
		model.addAttribute("agent", agent);
		return "agent/addSuccess";
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public String update(@RequestParam("id") Integer id, Model model) {
		Agent agent = agentService.get(id);
		model.addAttribute("agent", agent);
		return "agent/update";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update(Agent agent, @RequestParam("verifyCode") String verifyCode, Model model, HttpSession session) {
		VerifyCodeHelper verifyCodeHelper = (VerifyCodeHelper) session.getAttribute("verifyCodeHelper");
		if (null == verifyCodeHelper || !verifyCodeHelper.verify(verifyCode, agent.getTelephone())) {
			throw new ParamException("验证码无效！");
		}
		session.removeAttribute("verifyCodeHelper");
		Agent oldAgent = agentService.get(agent.getId());
		if (oldAgent.getTelephone() != null && !"".equals(oldAgent.getTelephone())) {
			if (!oldAgent.getTelephone().equals(agent.getTelephone())) {
				throw new ParamException("已经绑定了手机，不能更改！");
			}
		}
		agentService.update(agent);
		return "redirect:/user/mydetail";
	}
	
	@RequestMapping(value="addDown", method=RequestMethod.GET)
	public String addDown(Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		Agent myagent = agentService.get(loginInfo.getUserId());
		if (null == myagent) {
			model.addAttribute("msg", "您不是代理商，不能添加下级代理！");
			return "agent/hint";
		}
		SystemConfig systemConfig = systemConfigService.get("add_down_agent_income");
		BigDecimal totalIncome = myagent.getTotalIncome();
		if (null == totalIncome || totalIncome.compareTo(new BigDecimal(systemConfig.getValue())) < 0) {
			systemConfig = systemConfigService.get("add_down_agent_income_text");
			model.addAttribute("msg", systemConfig.getValue());
			return "agent/hint";
		}
		return "agent/addDown";
	}
	
	@RequestMapping(value="addDown", method=RequestMethod.POST)
	public String addDown(Agent agent, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		Agent myagent = agentService.get(loginInfo.getUserId());
		if (null == myagent) {
			throw new ParamException("您不是代理商，不能添加下级代理！");
		}
		SystemConfig systemConfig = systemConfigService.get("add_down_agent_income");
		BigDecimal totalIncome = myagent.getTotalIncome();
		if (null == totalIncome || totalIncome.compareTo(new BigDecimal(systemConfig.getValue())) < 0) {
			throw new ParamException("参数错误！");
		}
		agent.setUpperId(myagent.getId());
		agent.setUpperTwoId(myagent.getUpperId());
		agentService.add(agent);
		model.addAttribute("agent", agent);
		return "agent/addSuccess";
	}
	
}
