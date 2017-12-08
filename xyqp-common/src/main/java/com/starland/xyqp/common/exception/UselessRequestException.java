package com.starland.xyqp.common.exception;

/**
 * 无效请求异常
 */
public class UselessRequestException extends RuntimeException {

	private static final long serialVersionUID = 569431788909165586L;

	public UselessRequestException() {
		super();
	}

	public UselessRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UselessRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public UselessRequestException(String message) {
		super(message);
	}

	public UselessRequestException(Throwable cause) {
		super(cause);
	}

}
