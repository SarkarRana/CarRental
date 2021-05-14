package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description="Safe details")
@JsonInclude(Include.NON_NULL)
public class SafeDto extends BaseDto{

	@ApiModelProperty(notes = "The database generated safe ID and not required at the time of safe creation in application")
	private Integer safeId;

	@ApiModelProperty(notes = "The client specific safe name", required = true)
	private String name;
	
	@ApiModelProperty(notes = "Client Id of safe", required = true)
	private Integer clientId;

	@ApiModelProperty(notes = "Client name of safe", hidden = true)
	private String clientName;

	@ApiModelProperty(notes = "The location id of safe", required = true)
	private Integer locationId;
	
	@ApiModelProperty(notes = "The location name of safe", hidden = true)
	private String locationName;

	public SafeDto() {
		super();
	}

	public SafeDto(Integer safeId, String name, Integer clientId, String clientName, Integer locationId,
			String locationName) {
		super();
		this.safeId = safeId;
		this.name = name;
		this.clientId = clientId;
		this.clientName = clientName;
		this.locationId = locationId;
		this.locationName = locationName;
	}
	
	public SafeDto(Integer safeId, String name, Integer clientId, String clientName, Integer locationId,
			String locationName, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.safeId = safeId;
		this.name = name;
		this.clientId = clientId;
		this.clientName = clientName;
		this.locationId = locationId;
		this.locationName = locationName;
	}

	public Integer getSafeId() {
		return safeId;
	}

	public void setSafeId(Integer safeId) {
		this.safeId = safeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String toString() {
		return "SafeDto [safeId=" + safeId + ", name=" + name + ", clientId=" + clientId + ", clientName=" + clientName
				+ ", locationId=" + locationId + ", locationName=" + locationName + "]";
	}


}
