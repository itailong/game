package com.starland.xyqp.gmback.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PermissionGroupTagSupport extends SimpleTagSupport {
	
	private boolean isEmpty = true;
	
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	@Override
	public void doTag() throws JspException, IOException {
		try (StringWriter writer = new StringWriter()) {
			getJspBody().invoke(writer);
			JspWriter out = getJspContext().getOut();
			if (!isEmpty) {
				out.write(writer.toString());
			}
		}
	}

}
