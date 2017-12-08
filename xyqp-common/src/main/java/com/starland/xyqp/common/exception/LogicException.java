package com.starland.xyqp.common.exception;

/**
 * 逻辑错误
 * 
 */
public class LogicException extends RuntimeException {

	private static final long serialVersionUID = 7308586072032957075L;

	public LogicException() {
		super();
	}

	public LogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogicException(String message) {
		super(message);
	}

	public LogicException(Throwable cause) {
		super(cause);
	}

}
