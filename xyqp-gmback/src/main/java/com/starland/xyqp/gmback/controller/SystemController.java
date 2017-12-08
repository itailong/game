package com.starland.xyqp.gmback.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starland.xyqp.gmback.domain.User;
import com.starland.xyqp.gmback.helper.VerifyCodeHelper;
import com.starland.xyqp.gmback.service.RoleModuleService;
import com.starland.xyqp.gmback.service.UserService;
import com.starland.xyqp.gmback.utils.Md5Utils;
import com.starland.xyqp.gmback.utils.ShortMessageUtils;
import com.starland.xyqp.gmback.utils.VerifyCodeUtils;
import com.starland.xyqp.gmback.vo.AjaxResponse;
import com.starland.xyqp.gmback.vo.LoginInfo;

@Controller
@RequestMapping("system/")
public class SystemController {

	@Resource
	private UserService userService;
	
	@Resource
	private RoleModuleService roleModuleService;
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpSession session, Model model) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		if (null != loginInfo) {
			Integer userId = loginInfo.getUserId();
			User user = userService.get(userId);
			model.addAttribute("user", user);
		}
		return "system/index";
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String login(HttpSession session) {
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		if (null != loginInfo) {
			return "redirect:/system/index";
		}
		return "system/login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(@RequestParam("account") Integer account, @RequestParam("password") String passwrod, @RequestParam("verifyCode") String verifyCode, HttpSession session, Model model) {
		if (null == verifyCode || "".equals(verifyCode)) {
			model.addAttribute("error", "验证码为空！");
			return "system/login";
		}
		String code = (String) session.getAttribute("verifyCode");
		session.removeAttribute("verifyCode");
		if (!verifyCode.equalsIgnoreCase(code)) {
			model.addAttribute("error", "验证码不正确！");
			return "system/login";
		}
		if ("".equals(passwrod)) {
			model.addAttribute("error", "密码为空！");
			return "system/login";
		}
		User user = userService.get(account);
		if (null == user) {
			model.addAttribute("error", "用户名或密码错误！");
			return "system/login";
		}
		String psd = Md5Utils.encoding(passwrod);
		if (!psd.equals(user.getPassword())) {
			model.addAttribute("error", "用户名或密码错误！");
			return "system/login";
		}
		LoginInfo loginInfo = userService.getLoginInfo(user.getId());
		session.setAttribute(LoginInfo.SESSION_KEY, loginInfo);
		return "redirect:/system/index";
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	public String logout(HttpSession session) {
		session.removeAttribute(LoginInfo.SESSION_KEY);
		return "redirect:/system/login";
	}
	
	@RequestMapping(value="updatePassword", method=RequestMethod.GET)
	public String updatePassword() {
		return "system/updatePassword";
	}
	
	@ResponseBody
	@RequestMapping(value="updatePassword", method=RequestMethod.POST)
	public Object updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password, HttpSession session) {
		if (null == oldPassword || "".equals(oldPassword)) {
			return new AjaxResponse(500, "原密码不能为空！");
		}
		if (null == password || "".equals(password)) {
			return new AjaxResponse(500, "密码不能为空！");
		}
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		User user = userService.get(loginInfo.getUserId());
		if (!user.getPassword().equals(Md5Utils.encoding(oldPassword))) {
			return new AjaxResponse(500, "原密码不正确！");
		}
		userService.updatePassword(loginInfo.getUserId(), Md5Utils.encoding(password));
		return new AjaxResponse("修改成功！");
	}
	
	@ResponseBody
	@RequestMapping(value="obtainVerifyCode", method=RequestMethod.POST)
	public Object obtainVerifyCode(@RequestParam("telephone") String telephone, HttpSession session) {
		if (!telephone.matches("^1\\d{10}$")) {
			return new AjaxResponse("发送失败！");
		}
		String code = ShortMessageUtils.getCode();
		String rs = ShortMessageUtils.send(telephone, "【星羽游戏】您的验证码为：" + code + "。有效期3分钟，请勿泄露！");
		if (null == rs || !rs.startsWith("success:")) {
			return new AjaxResponse("发送失败！");
		}
		VerifyCodeHelper verifyCodeHelper = new VerifyCodeHelper(code, telephone);
		session.setAttribute("verifyCodeHelper", verifyCodeHelper);
		String tele = telephone.replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1****$3");
		return new AjaxResponse("验证码已经发送到" + tele + "。");
	}
	
	@RequestMapping(value="imageVerifyCode", method=RequestMethod.GET)
	public void imageVerifyCode(HttpSession session, HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "No-cache");  
        response.setDateHeader("Expires", 0);  
        response.setContentType("image/jpeg");
		String code = VerifyCodeUtils.randomCode();
		session.setAttribute("verifyCode", code);
		BufferedImage image = VerifyCodeUtils.createImage(code);
		try(ServletOutputStream out = response.getOutputStream()) {
			ImageIO.write(image, "JPEG", out);
		}
	}
	
	
}
