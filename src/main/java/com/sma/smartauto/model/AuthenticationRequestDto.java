package com.sma.smartauto.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AuthenticationRequestDto implements Serializable {
	
	@ApiModelProperty(notes = "Static API client name for key generation", required = true)
	private String clientName;
	
	@ApiModelProperty(notes = "Static API client password for key generation", required = true)
	private String clientKey;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	@Override
	public String toString() {
		return "AuthenticationRequestDto [clientName=" + clientName + ", clientKey=" + clientKey + "]";
	}

}
