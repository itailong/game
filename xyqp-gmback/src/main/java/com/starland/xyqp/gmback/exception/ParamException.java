package com.starland.xyqp.gmback.exception;

public class ParamException extends BaseException {

	private static final long serialVersionUID = -4295580181038234928L;

	public ParamException() {
		super();
	}

	public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamException(String message) {
		super(message);
	}

	public ParamException(Throwable cause) {
		super(cause);
	}

	@Override
	public int getCode() {
		return 102;
	}

}
