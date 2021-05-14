package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing a asset information.")
@JsonInclude(Include.NON_NULL)
public class AssetDto extends BaseDto {

	@ApiModelProperty(notes = "The database generated asset ID and will not be required at the time of creating asset", value="")
	private Integer assetId;

	@ApiModelProperty(hidden = true)
	private String clientName;
	
	@ApiModelProperty(notes = "Client Id of asset", value="1", required = true)
	private Integer clientId;

	@ApiModelProperty(hidden = true)
	private String safeName;

	@ApiModelProperty(notes = "Safe Id of asset and will be required when asset is associated with safe/compartment at the time of creation or associating it later", required = false)
	private Integer safeId;
	
	@ApiModelProperty(hidden = true)
	private String compartmentName;

	@ApiModelProperty(notes = "Compartment Id of asset and will be required when asset is associated with safe/compartment at the time of creation  or associating it later", required = false)
	private Integer compartmentId;

	@ApiModelProperty(notes = "Name of asset", required=true)
	private String name;

	@ApiModelProperty(notes = "Unique identifier of asset", required=true)
	private String identifier;

	@ApiModelProperty(notes = "Identifier type of asset", required=true, allowableValues = "A,B,C,GH")
	private String identifierType;

	@ApiModelProperty(hidden = true)
	private String assetType;

	@ApiModelProperty(notes = "type of asset - KEY,GUN etc", value="1", required=true)
	private Integer assetTypeId;
	
	private Integer userId;
	
	private String staffCardCode;
	
	@ApiModelProperty(notes = "Asset is currently active/inactive", value="1", required=true)
	private boolean active;

	public AssetDto() {
		super();
	}

	public AssetDto(Integer assetId, String clientName, Integer clientId, String name, String identifier,
			String identifierType, String assetType, Integer assetTypeId, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.assetId = assetId;
		this.clientName = clientName;
		this.clientId = clientId;
		this.name = name;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
	}

	public AssetDto(Integer assetId, String clientName, Integer clientId, String safeName, Integer safeId,
			String compartmentName, Integer compartmentId, String name, String identifier, String identifierType,
			String assetType, Integer assetTypeId) {
		super();
		this.assetId = assetId;
		this.clientName = clientName;
		this.clientId = clientId;
		this.safeName = safeName;
		this.safeId = safeId;
		this.compartmentName = compartmentName;
		this.compartmentId = compartmentId;
		this.name = name;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
	}

	public AssetDto(Integer assetId, String clientName, Integer clientId, String safeName, Integer safeId,
			String compartmentName, Integer compartmentId, String name, String identifier, String identifierType,
			String assetType, Integer assetTypeId, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.assetId = assetId;
		this.clientName = clientName;
		this.clientId = clientId;
		this.safeName = safeName;
		this.safeId = safeId;
		this.compartmentName = compartmentName;
		this.compartmentId = compartmentId;
		this.name = name;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
	}
	
	public AssetDto(Integer assetId, String clientName, Integer clientId, String safeName, Integer safeId,
			String compartmentName, Integer compartmentId, String name, String identifier, String identifierType,
			String assetType, Integer assetTypeId, Boolean active) {
		super();
		this.assetId = assetId;
		this.clientName = clientName;
		this.clientId = clientId;
		this.safeName = safeName;
		this.safeId = safeId;
		this.compartmentName = compartmentName;
		this.compartmentId = compartmentId;
		this.name = name;
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
		this.active = active;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getSafeName() {
		return safeName;
	}

	public void setSafeName(String safeName) {
		this.safeName = safeName;
	}

	public Integer getSafeId() {
		return safeId;
	}

	public void setSafeId(Integer safeId) {
		this.safeId = safeId;
	}

	public String getCompartmentName() {
		return compartmentName;
	}

	public void setCompartmentName(String compartmentName) {
		this.compartmentName = compartmentName;
	}

	public Integer getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(Integer compartmentId) {
		this.compartmentId = compartmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getStaffCardCode() {
		return staffCardCode;
	}

	public void setStaffCardCode(String staffCardCode) {
		this.staffCardCode = staffCardCode;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Asset [assetId=" + assetId + ", clientName=" + clientName + ", clientId=" + clientId + ", safeName="
				+ safeName + ", safeId=" + safeId + ", compartmentName=" + compartmentName + ", compartmentId="
				+ compartmentId + ", name=" + name + ", identifier=" + identifier + ", identifierType=" + identifierType
				+ ", assetType=" + assetType + ", assetTypeId=" + assetTypeId + "]";
	}

}
