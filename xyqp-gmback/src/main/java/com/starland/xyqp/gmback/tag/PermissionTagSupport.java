package com.starland.xyqp.gmback.tag;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.starland.xyqp.gmback.vo.LoginInfo;

public class PermissionTagSupport extends SimpleTagSupport {

	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void doTag() throws JspException, IOException {
		LoginInfo loginInfo = (LoginInfo) getJspContext().getAttribute(LoginInfo.SESSION_KEY, PageContext.SESSION_SCOPE);
		if (null == loginInfo) {
			return;
		}
		Set<String> withinUrls = loginInfo.getWithinUrls();
		if (withinUrls.contains(url)) {
			JspTag parent = getParent();
			if (parent instanceof PermissionGroupTagSupport) {
				((PermissionGroupTagSupport) parent).setEmpty(false);
			}
			getJspBody().invoke(null);
		}
	}
	
}
