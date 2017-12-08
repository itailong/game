package com.starland.xyqp.gmback.exception;

public class LoginTimeoutException extends BaseException {

	private static final long serialVersionUID = 4618954876346698020L;

	public LoginTimeoutException() {
		super();
	}

	public LoginTimeoutException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginTimeoutException(String message) {
		super(message);
	}

	public LoginTimeoutException(Throwable cause) {
		super(cause);
	}

	@Override
	public int getCode() {
		return 100;
	}

}
