package com.starland.xyqp.gmback.controller;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.domain.Agent;
import com.starland.xyqp.gmback.domain.User;
import com.starland.xyqp.gmback.exception.ParamException;
import com.starland.xyqp.gmback.query.UserQuery;
import com.starland.xyqp.gmback.service.AgentService;
import com.starland.xyqp.gmback.service.UserService;
import com.starland.xyqp.gmback.utils.Md5Utils;
import com.starland.xyqp.gmback.vo.AjaxResponse;
import com.starland.xyqp.gmback.vo.LoginInfo;

@Controller
@RequestMapping("user/")
public class UserController {

	@Resource
	private UserService userService;
	
	@Resource
	private AgentService agentService;
	
	@RequestMapping("list")
	public String list(UserQuery query, PageInfo pageInfo, Model model) {
		List<User> list = userService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "user/list";
	}
	
	@RequestMapping(value="detail", method=RequestMethod.GET)
	public String detail(@RequestParam("userId") Integer userId, Model model) {
		User user = userService.get(userId);
		model.addAttribute("user", user);
		return "user/detail";
	}
	
	@RequestMapping(value="add", method=RequestMethod.GET)
	public String add() {
		return "user/add";
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String add(User user, Model model) {
		userService.add(user);
		model.addAttribute("user", user);
		return "user/addSuccess";
	}
	
	@RequestMapping(value="mydetail", method=RequestMethod.GET)
	public String mydetail(Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		User user = userService.get(loginInfo.getUserId());
		Agent agent = agentService.get(loginInfo.getUserId());
		if (null != agent) {
			if (agent.getAddress() == null || agent.getRealName() == null || agent.getTelephone() == null) {
				return "redirect:/agent/update?id=" + agent.getId();
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("agent", agent);
		return "user/detail";
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(@RequestParam("id") Integer id) {
		userService.delete(id);
		return new AjaxResponse("删除成功！");
	}
	
	@RequestMapping(value="pay", method=RequestMethod.GET)
	public String pay(@RequestParam("id") Integer id, Model model, HttpSession session) {
		model.addAttribute("userId", id);
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		User user = userService.get(loginInfo.getUserId());
		model.addAttribute("user", user);
		return "user/pay";
	}
	
	@ResponseBody
	@RequestMapping(value="pay", method=RequestMethod.POST)
	public Object pay(@RequestParam("id") Integer id, @RequestParam("diamond") Integer diamond, HttpSession session) {
		if (diamond <= 0) {
			return new AjaxResponse(500, "充值不成功！");
		}
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		userService.updatePayDiamond(id, diamond, loginInfo.getUserId());
		return new AjaxResponse("充值成功！");
	}
	
	@RequestMapping("mylist")
	public String twoList(UserQuery query, PageInfo pageInfo, Model model, HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		query.setUpperId(loginInfo.getUserId());
		List<User> list = userService.list(query, pageInfo);
		model.addAttribute("list", list);
		return "user/mylist";
	}
	
	@RequestMapping(value="give", method=RequestMethod.GET)
	public String give(@RequestParam("id") Integer id, Model model, HttpSession session) {
		model.addAttribute("userId", id);
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		User user = userService.get(loginInfo.getUserId());
		model.addAttribute("user", user);
		return "user/give";
	}
	
	@ResponseBody
	@RequestMapping(value="give", method=RequestMethod.POST)
	public Object give(@RequestParam("id") Integer id, @RequestParam("diamond") Integer diamond, HttpSession session) {
		if (diamond <= 0) {
			return new AjaxResponse(500, "赠送不成功！");
		}
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		userService.updatePayDiamond(id, diamond, loginInfo.getUserId());
		return new AjaxResponse("赠送成功！");
	}
	
	@ResponseBody
	@RequestMapping(value="reset", method=RequestMethod.POST)
	public Object reset(@RequestParam("id") Integer id) {
		User user = userService.get(id);
		if (user.getUserType() == null || user.getUserType().intValue() != 3) {
			throw new ParamException("只有后台用户可以重置密码！");
		}
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int num = random.nextInt(10);
			buf.append(num);
		}
		String password = buf.toString();
		userService.updatePassword(id, Md5Utils.encoding(password));
		return new AjaxResponse("密码重置为：" + password + "，请及时修改！");
	}
}
