package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExceptionResponse {

	private Date timestamp;
	private String errorCode;
	private String statusDetails;
	private String path;
	private boolean success = false;

	public ExceptionResponse() {
		super();
	}

	public ExceptionResponse(Date timestamp, String errorCode, String message) {
		super();
		this.timestamp = timestamp;
		this.errorCode = errorCode;
		this.statusDetails = message;
	}

	public ExceptionResponse(Date timestamp, String errorCode, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.errorCode = errorCode;
		this.statusDetails = message;
		this.path = path;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatusDetails() {
		return statusDetails;
	}

	public void setStatusDetails(String statusDetails) {
		this.statusDetails = statusDetails;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
