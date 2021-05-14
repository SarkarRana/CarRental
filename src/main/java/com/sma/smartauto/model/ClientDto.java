package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing a Client information.")
@JsonInclude(Include.NON_NULL)
public class ClientDto  extends BaseDto {

	@ApiModelProperty(notes = "The database generated client ID and will not be required at the time of creating client", value="")
	private Integer clientId;

	@ApiModelProperty(notes = "Name should have atleast 2 characters")
	private String name;

	@ApiModelProperty(notes = "The database generated client type id and will not be required at the time of creating client", value="")
	private Integer clientTypeId;

	@ApiModelProperty(notes = "Client type name", hidden = true)
	private String clientType;

	public ClientDto() {
		super();
	}
	
	public ClientDto(Integer clientId, String name, Integer clientTypeId, String clientType, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null,createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.clientId = clientId;
		this.name = name;
		this.clientTypeId = clientTypeId;
		this.clientType = clientType;
	}

	public ClientDto(Integer clientId, String name, Integer clientTypeId, String clientType) {
		super();
		this.clientId = clientId;
		this.name = name;
		this.clientTypeId = clientTypeId;
		this.clientType = clientType;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getClientTypeId() {
		return clientTypeId;
	}

	public void setClientTypeId(Integer clientTypeId) {
		this.clientTypeId = clientTypeId;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientName) {
		this.clientType = clientName;
	}

	@Override
	public String toString() {
		return "ClientDto [clientId=" + clientId + ", name=" + name + ", clientTypeId=" + clientTypeId + ", clientName="
				+ clientType + "]";
	}

}
