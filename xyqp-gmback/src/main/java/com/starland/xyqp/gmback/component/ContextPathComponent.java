package com.starland.xyqp.gmback.component;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class ContextPathComponent implements ServletContextAware, InitializingBean {

	private ServletContext servletContext;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (null == servletContext) {
			return;
		}
		String cp = servletContext.getContextPath();
		servletContext.setAttribute("cp", cp);
	}

}
