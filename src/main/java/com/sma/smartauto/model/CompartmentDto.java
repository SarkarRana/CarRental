package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class CompartmentDto extends BaseDto {
	
	@ApiModelProperty(notes = "Compartment table DB id")
	private Integer id;

	@ApiModelProperty(notes = "Combination of safe id and lock number i.e. if safe id=1, lock num = 12 then compartment id = 112")
	private Integer compartmentId;
	
	@ApiModelProperty(notes = "Compartment number or lock number 1,2,3,...32")
	private String compartmentNum;

	@ApiModelProperty(notes = "Compartment number or lock number 1,2,3,...32")
	private String name;
	
	@ApiModelProperty(notes = "Client Id of compartment")
	private Integer clientId;

	@ApiModelProperty(hidden = true)
	private String clientName;

	@ApiModelProperty(notes = "Safe Id of asset compartment")
	private Integer safeId;
	
	@ApiModelProperty(hidden = true)
	private String safeName;

	@ApiModelProperty(notes = "Compartment status empty=true, filled=false", allowableValues="true, false")
	private boolean empty;
	
	@ApiModelProperty(notes = "Asset Id of compartment")
	private Integer assetId;
	
	@ApiModelProperty(notes = "Asset identifier")
	private String identifier;
	
	@ApiModelProperty(notes = "Asset identifier type")
	private String identifierType;

	public CompartmentDto() {
		super();
	}

	public CompartmentDto(Integer id, Integer compartmentId, String name, Integer clientId, String clientName, Integer safeId,
			String safeName, boolean empty, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.id = id;
		this.compartmentId = compartmentId;
		this.name = name;
		this.clientId = clientId;
		this.clientName = clientName;
		this.safeId = safeId;
		this.safeName = safeName;
		this.empty = empty;
	}

	public CompartmentDto(Integer id, Integer compartmentId, String compartmentNum, String name, Integer clientId, String clientName, Integer safeId,
			String safeName, boolean empty, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.id = id;
		this.compartmentId = compartmentId;
		this.compartmentNum = compartmentNum;
		this.name = name;
		this.clientId = clientId;
		this.clientName = clientName;
		this.safeId = safeId;
		this.safeName = safeName;
		this.empty = empty;
	}
	
	public CompartmentDto(Integer id, Integer compartmentId, String compartmentNum, String name, Integer clientId,
			String clientName, Integer safeId, String safeName, boolean empty, Integer assetId, String identifier,
			String identifierType, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.id = id;
		this.compartmentId = compartmentId;
		this.compartmentNum = compartmentNum;
		this.name = name;
		this.clientId = clientId;
		this.clientName = clientName;
		this.safeId = safeId;
		this.safeName = safeName;
		this.empty = empty;
		this.assetId = assetId;
		this.identifier = identifier;
		this.identifierType = identifierType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(Integer compartmentId) {
		this.compartmentId = compartmentId;
	}

	public String getCompartmentNum() {
		return compartmentNum;
	}

	public void setCompartmentNum(String compartmentNum) {
		this.compartmentNum = compartmentNum;
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

	public boolean getEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
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

	@Override
	public String toString() {
		return "CompartmentDto [id=" + id + ", compartmentId=" + compartmentId + ", compartmentNum=" + compartmentNum
				+ ", name=" + name + ", clientId=" + clientId + ", clientName=" + clientName + ", safeId=" + safeId
				+ ", safeName=" + safeName + ", empty=" + empty + ", assetId=" + assetId + ", identifier=" + identifier
				+ ", identifierType=" + identifierType + "]";
	}

}
