package com.sma.smartauto.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sma.smartauto.utils.DateHandler;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class ActionDto extends BaseDto {

	@ApiModelProperty(notes = "The database generated action ID, will not be passed in request at the time of saving new action")
	private Integer actionId;
	
	@ApiModelProperty(notes = "Action performed by user", required=true, allowableValues = "TAKE, RETURN")
	private String actionType;
	
	@ApiModelProperty(notes = "The database generated action type ID")
	private Integer actionTypeId;
	
	@ApiModelProperty(hidden = true)
	private Integer assetId;
	
	@ApiModelProperty(notes = "Asset identifier", required=true)
	private String identifier;

	@ApiModelProperty(hidden = true)
	private String clientName;

	@ApiModelProperty(notes = "Client ID", required=true)
	private Integer clientId;

	@ApiModelProperty(hidden = true)
	private String safeName;

	@ApiModelProperty(notes = "Safe ID", required=true)
	private Integer safeId;

	@ApiModelProperty(hidden = true)
	private String compartmentName;

	@ApiModelProperty(notes = "Compartement ID", required=true)
	private Integer compartmentId;	

	@ApiModelProperty(notes = "User code against which action is performed", required=true)
	private String code;

	@ApiModelProperty(notes = "The database generated user ID of user who has performed the action. Null in case of end users")
	private Integer userId;
	
	@ApiModelProperty(hidden = true)
	private String userName;
	
	@ApiModelProperty(notes = "Staff Card Code of Staff memeber performing LOAD/UNLOAD action on safe")
	private String staffCardCode;
	
	@JsonDeserialize(using = DateHandler.class)
	@ApiModelProperty(notes = "Date and Time when action is performed. Format - yyyy-MM-dd HH:mm:ss", required = true)
	private Date date;

	public ActionDto() {
		super();
	}

	public ActionDto(Integer actionId, String actionType, Integer actionTypeId, Integer assetId, String identifier, String clientName,
			Integer clientId, String safeName, Integer safeId, String compartmentName, Integer compartmentId,
			String code, Integer userId, String userName, String staffCardCode, Date date) {
		super();
		this.actionId = actionId;
		this.actionType = actionType;
		this.actionTypeId = actionTypeId;
		this.assetId = assetId;
		this.identifier = identifier;
		this.clientName = clientName;
		this.clientId = clientId;
		this.safeName = safeName;
		this.safeId = safeId;
		this.compartmentName = compartmentName;
		this.compartmentId = compartmentId;
		this.code = code;
		this.userId = userId;
		this.userName = userName;
		this.staffCardCode = staffCardCode;
		this.date = date;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getActionTypeId() {
		return actionTypeId;
	}

	public void setActionTypeId(Integer actionTypeId) {
		this.actionTypeId = actionTypeId;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public String getStaffCardCode() {
		return staffCardCode;
	}

	public void setStaffCardCode(String staffCardCode) {
		this.staffCardCode = staffCardCode;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ActionDto [actionId=" + actionId + ", actionType=" + actionType + ", actionTypeId=" + actionTypeId
				+ ", assetId=" + assetId + ", identifier=" + identifier + ", clientName=" + clientName + ", clientId="
				+ clientId + ", safeName=" + safeName + ", safeId=" + safeId + ", compartmentName=" + compartmentName
				+ ", compartmentId=" + compartmentId + ", code=" + code + ", userId=" + userId + ", userName="
				+ userName + ", staffCardCode=" + staffCardCode + ", date=" + date + "]";
	}

}
