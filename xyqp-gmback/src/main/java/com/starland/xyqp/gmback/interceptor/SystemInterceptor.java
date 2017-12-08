package com.starland.xyqp.gmback.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.starland.xyqp.gmback.exception.LoginTimeoutException;
import com.starland.xyqp.gmback.exception.NoPermissionException;
import com.starland.xyqp.gmback.vo.LoginInfo;

public class SystemInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.SESSION_KEY);
		if (null == loginInfo) {
			throw new LoginTimeoutException("登录超时！");
		}
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		path = path.substring(contextPath.length());
		Set<String> withoutUrls = loginInfo.getWithoutUrls();
		if (withoutUrls.contains(path)) {
			throw new NoPermissionException("您没有权限！");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}

}
