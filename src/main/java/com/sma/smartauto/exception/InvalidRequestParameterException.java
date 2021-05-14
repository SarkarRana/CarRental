package com.sma.smartauto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidRequestParameterException extends RuntimeException {

	public InvalidRequestParameterException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
