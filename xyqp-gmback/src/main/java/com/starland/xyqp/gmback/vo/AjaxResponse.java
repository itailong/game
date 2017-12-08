package com.starland.xyqp.gmback.vo;

public class AjaxResponse {

	public static final int CODE_SUCCESS = 200;
	
	private int code;
	
	private String msg;
	
	private Object data;

	public AjaxResponse() {
	}

	public AjaxResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public AjaxResponse(Object data) {
		this.data = data;
		this.code = CODE_SUCCESS;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Object getData() {
		return data;
	}

}
