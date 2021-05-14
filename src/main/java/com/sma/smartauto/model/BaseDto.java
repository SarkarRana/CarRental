package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class BaseDto {

	@ApiModelProperty(hidden = true)
	private Boolean success;
	
	@ApiModelProperty(hidden = true)
	private String errorCode;
	
	@ApiModelProperty(hidden = true)
	private String statusDetail;
	
	@ApiModelProperty(hidden = true)
	private Integer createdById;
	
	@ApiModelProperty(hidden = true)
	private String createdBy;
	
	@ApiModelProperty(hidden = true)
	private Integer lastUpdatedById;
	
	@ApiModelProperty(hidden = true)
	private String lastUpdatedBy;
	
	@ApiModelProperty(hidden = true)
	private Date createdOn;
	
	@ApiModelProperty(hidden = true)
	private Date lastUpdatedOn;


	public BaseDto() {
		super();
	}

	public BaseDto(Integer createdById, String createdBy, Integer lastUpdatedById, String lastUpdatedBy, Date createdOn,
			Date lastUpdatedOn) {
		super();
		this.createdById = createdById;
		this.createdBy = createdBy;
		this.lastUpdatedById = lastUpdatedById;
		this.lastUpdatedBy = lastUpdatedBy;
		this.createdOn = createdOn;
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public BaseDto(Boolean success, String errorCode, String errorDesc) {
		super();
		this.success = success;
		this.errorCode = errorCode;
		this.statusDetail = errorDesc;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getStatusDetail() {
		return statusDetail;
	}

	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getLastUpdatedById() {
		return lastUpdatedById;
	}

	public void setLastUpdatedById(Integer lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
}
