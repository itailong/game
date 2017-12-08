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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.domain.ExtractDeposit;
import com.starland.xyqp.gmback.exception.ParamException;
import com.starland.xyqp.gmback.helper.VerifyCodeHelper;
import com.starland.xyqp.gmback.query.ExtractDepositQuery;
import com.starland.xyqp.gmback.service.AgentService;
import com.starland.xyqp.gmback.service.ExtractDepositService;
import com.starland.xyqp.gmback.vo.AjaxResponse;
import com.starland.xyqp.gmback.vo.LoginInfo;

@Controller
@RequestMapping("extractDeposit/")
public class ExtractDepositController {

	@Resource
	private ExtractDepositService extractDepositService;
	
	@Resource
	private AgentService agentService;
	
	@RequestMapping("list")
	public String list(ExtractDepositQuery query, PageInfo pageInfo, Model model) {
		List<ExtractDeposit> list = extractDepositService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "extractDeposit/list";
	}
	
	@RequestMapping("mylist")
	public String mylist(ExtractDepositQuery query, PageInfo pageInfo, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		query.setAgentId(loginInfo.getUserId());
		List<ExtractDeposit> list = extractDepositService.list(query, pageInfo);
		for (ExtractDeposit extractDeposit : list) {
			String bankAccount = extractDeposit.getBankAccount();
			bankAccount = bankAccount.replaceAll("^(\\d{4})(\\d+)(\\d{4})$", "$1********$3");
			extractDeposit.setBankAccount(bankAccount);
		}
		model.addAttribute("list", list);
		return "extractDeposit/mylist";
	}
	
	@RequestMapping(value="add", method=RequestMethod.GET)
	public String add(HttpSession session, Model model) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		Agent agent = agentService.get(loginInfo.getUserId());
		if (null == agent) {
			throw new ParamException("您不是代理商!");
		}
		model.addAttribute("agent", agent);
		return "extractDeposit/add";
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String add(ExtractDeposit extractDeposit, HttpSession session, @RequestParam("verifyCode") String verifyCode) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		Integer agentId = loginInfo.getUserId();
		Agent agent = agentService.get(agentId);
		VerifyCodeHelper verifyCodeHelper = (VerifyCodeHelper) session.getAttribute("verifyCodeHelper");
		if (null == verifyCodeHelper || !verifyCodeHelper.verify(verifyCode, agent.getTelephone())) {
			throw new ParamException("验证码无效！");
		}
		session.removeAttribute("verifyCodeHelper");
		extractDeposit.setAgentId(agentId);
		extractDepositService.add(extractDeposit);
		return "redirect:/extractDeposit/mylist";
	}
	
	@ResponseBody
	@RequestMapping(value="success", method=RequestMethod.POST)
	public Object success(@RequestParam("id") Integer id) {
		extractDepositService.updateSuccess(id);
		return new AjaxResponse("操作成功！");
	}
	
	@RequestMapping(value="fail", method=RequestMethod.GET)
	public String fail(@RequestParam("id") Integer id, Model model) {
		model.addAttribute("id", id);
		return "extractDeposit/fail";
	}
	
	@RequestMapping(value="fail", method=RequestMethod.POST)
	public String fail(@RequestParam("id") Integer id, @RequestParam("remark") String remark) {
		extractDepositService.updateFail(id, remark);
		return "redirect:/extractDeposit/list";
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public String update(@RequestParam("id") Integer id, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		Agent agent = agentService.get(loginInfo.getUserId());
		if (null == agent) {
			throw new ParamException("您不是代理商!");
		}
		model.addAttribute("agent", agent);
		ExtractDeposit extractDeposit = extractDepositService.get(id);
		model.addAttribute("extractDeposit", extractDeposit);
		return "extractDeposit/update";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String update(ExtractDeposit extractDeposit, HttpSession session, @RequestParam("verifyCode") String verifyCode) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		Integer agentId = loginInfo.getUserId();
		Agent agent = agentService.get(agentId);
		VerifyCodeHelper verifyCodeHelper = (VerifyCodeHelper) session.getAttribute("verifyCodeHelper");
		if (null == verifyCodeHelper || !verifyCodeHelper.verify(verifyCode, agent.getTelephone())) {
			throw new ParamException("验证码无效！");
		}
		session.removeAttribute("verifyCodeHelper");
		extractDepositService.update(extractDeposit);
		return "redirect:/extractDeposit/mylist";
	}
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
