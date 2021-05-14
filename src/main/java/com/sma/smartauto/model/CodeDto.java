package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class CodeDto extends BaseDto {

	@ApiModelProperty(notes = "The database generated code ID which is not required in request at the time of code creation")
	private Integer id;
	
	@ApiModelProperty(notes = "One time code", required = true)
	private String code;
	
	@ApiModelProperty(notes = "DB id of client", required = true)
	private Integer clientId;
	
	@ApiModelProperty(hidden = true)
	private String clientName;
	
	@ApiModelProperty(hidden = true)
	private Integer assetId;
	
	@ApiModelProperty(hidden = true)
	private String assetName;
	
	@ApiModelProperty(notes = "DB id of safe", required = true)
	private Integer safeId;
	
	@ApiModelProperty(hidden = true)
	private String safeName;
	
	@ApiModelProperty(notes = "DB id of associated compartment", hidden = true)
	private Integer compartmentId;
	
	@ApiModelProperty(hidden = true)
	private String compartmentName;
	
	@ApiModelProperty(notes = "identifier of associated Asset either identifier or identifier type or both")
	private String identifier;
	
	@ApiModelProperty(notes = "identifier type  of associated Asset either identifier or identifier type or both")
	private String identifierType;
	
	@ApiModelProperty
	private Integer locationId;
	
	@ApiModelProperty(hidden = true)
	private String locationName;
	
	@ApiModelProperty(notes = "Compartment status empty=true, filled=false", hidden = true)
	private Boolean compartmentEmpty;
	
	@ApiModelProperty(notes = "Valid from date time for code", required = true)
	private String validFrom;
	
	@ApiModelProperty(notes = "Valid to date time for code", required = true)
	private String validTill;
	
	private String actionType;
	
	@ApiModelProperty(notes = "Code active=true, expired=false")
	private boolean active;

	public CodeDto() {
		super();
	}

	public CodeDto(Integer id, String code, Integer clientId, String clientName, Integer assetId, String assetName, String identifier,
			String identifierType, String locationName) {
		super();
		this.id = id;
		this.code = code;
		this.clientId = clientId;
		this.clientName = clientName;
		this.assetId = assetId;
		this.assetName = assetName;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.locationName = locationName;
	}
	
	public CodeDto(Integer id, String code, Integer clientId, String clientName, Integer assetId, String assetName, Integer safeId,
			String safeName, String identifier, String identifierType, String locationName) {
		super();
		this.id = id;
		this.code = code;
		this.clientId = clientId;
		this.clientName = clientName;
		this.assetId = assetId;
		this.assetName = assetName;
		this.safeId = safeId;
		this.safeName = safeName;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.locationName = locationName;
	}

	public CodeDto(Integer id, String code, Integer clientId, String clientName, Integer assetId, String assetName, Integer safeId,
			String safeName, Integer compartmentId, String compartmentName, String identifier, String identifierType,
			String locationName, String validFrom, String validTill, boolean active, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.id = id;
		this.code = code;
		this.clientId = clientId;
		this.clientName = clientName;
		this.assetId = assetId;
		this.assetName = assetName;
		this.safeId = safeId;
		this.safeName = safeName;
		this.compartmentId = compartmentId;
		this.compartmentName = compartmentName;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.locationName = locationName;
		this.validFrom = validFrom;
		this.validTill = validTill;
		this.active = active;
	}
	
	public CodeDto(Integer id, String code, Integer clientId, String clientName, Integer assetId, String assetName, Integer safeId,
			String safeName, Integer compartmentId, String compartmentName, String identifier, String identifierType,
			String locationName, Boolean compartmentEmpty, String validFrom, String validTill, boolean active) {
		super();
		this.id = id;
		this.code = code;
		this.clientId = clientId;
		this.clientName = clientName;
		this.assetId = assetId;
		this.assetName = assetName;
		this.safeId = safeId;
		this.safeName = safeName;
		this.compartmentId = compartmentId;
		this.compartmentName = compartmentName;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.locationName = locationName;
		this.compartmentEmpty = compartmentEmpty;
		this.validFrom = validFrom;
		this.validTill = validTill;
		this.active = active;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String codeId) {
		code = codeId;
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
	
	public Integer getSafeId() {
		return safeId;
	}

	public void setSafeId(Integer safeId) {
		this.safeId = safeId;
	}

	public String getSafeName() {
		return safeName;
	}

	public void setSafeName(String safeName) {
		this.safeName = safeName;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
		
		
	}

	public String getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	public Integer getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(Integer compartmentId) {
		this.compartmentId = compartmentId;
	}

	public String getCompartmentName() {
		return compartmentName;
	}

	public void setCompartmentName(String compartmentName) {
		this.compartmentName = compartmentName;
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

	public Boolean getCompartmentEmpty() {
		return compartmentEmpty;
	}

	public void setCompartmentEmpty(Boolean compartmentStatus) {
		this.compartmentEmpty = compartmentStatus;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTill() {
		return validTill;
	}

	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "CodeDto [code=" + code + ", clientId=" + clientId + ", clientName=" + clientName + ", assetId="
				+ assetId + ", assetName=" + assetName + ", safeId=" + safeId + ", safeName=" + safeName
				+ ", compartmentId=" + compartmentId + ", compartmentName=" + compartmentName + ", identifier="
				+ identifier + ", identifierType=" + identifierType + ", locationName=" + locationName
				+ ", compartmentStatus=" + compartmentEmpty + ", validFrom=" + validFrom + ", validTill=" + validTill
				+ "]";
	}


}
