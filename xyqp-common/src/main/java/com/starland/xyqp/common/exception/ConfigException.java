package com.starland.xyqp.common.exception;

public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = -3308695041034659547L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

}
