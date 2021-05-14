package com.sma.smartauto.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class UserDto extends BaseDto {
	
	@ApiModelProperty(notes = "The database generated user ID", required = true)
	private Integer userId;
	
	@ApiModelProperty(notes = "user name", required = true, hidden = true)
	private String username;
	
	@ApiModelProperty(notes = "user password", hidden = true)
	private String password;
	
	@ApiModelProperty(notes = "user app password", required = true)
	private String appPassword;
	
	@ApiModelProperty(notes = "user staff card code")
	private String staffCardCode;
	
	@ApiModelProperty(notes = "user role", allowableValues="ROLE_ADMIN, ROLE_ASSIST, ROLE_USER", required = true)
	private String role;
	
	@ApiModelProperty(hidden = true)
	private List<String> roleList;
	
	@ApiModelProperty(notes = "Client Id of user/staff member", required = true)
	private Integer clientId;
	
	@ApiModelProperty(hidden = true)
	private String clientName;
	
	@ApiModelProperty(notes = "The location id of safe", required = true)
	private Integer locationId;
	
	@ApiModelProperty(notes = "The location name of safe", hidden = true)
	private String locationName;
	
	public UserDto() {
		super();
	}
	
	public UserDto(Integer userId, String password, String appPassword, String staffCardCode, String role, Integer locationId, String locationName, Integer clientId, String clientName) {
		super();
		this.userId = userId;
		this.password = password;
		this.appPassword = appPassword;
		this.staffCardCode = staffCardCode;
		this.role = role;
		this.locationId = locationId;
		this.locationName = locationName;
		this.clientId = clientId;
		this.clientName = clientName;
	}

	public UserDto(Integer userId, String password, String appPassword, String staffCardCode, String role, Integer locationId, String locationName,
			Integer clientId, String clientName, String createdBy, Date createdOn, String lastUpdatedBy, Date lastUpdatedOn) {
		super(null, createdBy, null, lastUpdatedBy, createdOn, lastUpdatedOn);
		this.userId = userId;
		this.password = password;
		this.appPassword = appPassword;
		this.staffCardCode = staffCardCode;
		this.role = role;
		this.locationId = locationId;
		this.locationName = locationName;		
		this.clientId = clientId;
		this.clientName = clientName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppPassword() {
		return appPassword;
	}

	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}

	public String getStaffCardCode() {
		return staffCardCode;
	}

	public void setStaffCardCode(String staffCardCode) {
		this.staffCardCode = staffCardCode;
	}

	public String getRole() {
		return role;
	}

	public void setRoleId(String role) {
		this.role = role;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public Integer getClientId() {
		return clientId;
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

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
