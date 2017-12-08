package com.starland.xyqp.gmback.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.starland.xyqp.gmback.exception.BaseException;
import com.starland.xyqp.gmback.exception.LoginTimeoutException;

@Component
public class ExceptionComponent implements HandlerExceptionResolver {

	private static final Logger LOGGER = LogManager.getLogger(ExceptionComponent.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView = new ModelAndView();
		String message = "";
		int code = 0;
		if (ex instanceof BaseException) {
			message = ex.getMessage();
			code = ((BaseException) ex).getCode();
		} else {
			LOGGER.error("", ex);
			message = "未知错误！";
			code = 500;
		}
		String accept = request.getHeader("Accept");
		if (null != accept && accept.indexOf("text/html") >= 0) {
			if (ex instanceof LoginTimeoutException) {
				modelAndView.setViewName("system/loginTimeout");
			} else {
				request.setAttribute("message", message);
				modelAndView.setViewName("system/error");
			}
		} else {
			MappingJackson2JsonView view = new MappingJackson2JsonView();
			view.addStaticAttribute("code", code);
			view.addStaticAttribute("msg", message);
			modelAndView.setView(view);
		}
		return modelAndView;
	}

}
