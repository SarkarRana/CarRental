package com.sma.smartauto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SmartAutoSafeException extends RuntimeException {
	private ApiErrorCode errorCode;

	public SmartAutoSafeException(String message) {
		super(message);
	}
	
	public SmartAutoSafeException(ApiErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ApiErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ApiErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "SmartAutoSafeException [errorCode=" + errorCode + ", getMessage()=" + getMessage() + "]";
	}

}
