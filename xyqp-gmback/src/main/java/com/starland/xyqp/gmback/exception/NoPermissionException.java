package com.starland.xyqp.gmback.exception;

public class NoPermissionException extends BaseException {

	private static final long serialVersionUID = 2280801429920786300L;

	public NoPermissionException() {
		super();
	}

	public NoPermissionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPermissionException(String message) {
		super(message);
	}

	public NoPermissionException(Throwable cause) {
		super(cause);
	}

	@Override
	public int getCode() {
		return 101;
	}

}
