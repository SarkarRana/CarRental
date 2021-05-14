package com.sma.smartauto.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@SuppressWarnings("serial")
@Entity
@Table(name = "client_notification_history")
public class ClientNotificationHistory implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "client_notification_history_id")
	private Integer id;
	
	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "safe_id")
	private Integer safeId;
	
	@Column(name = "compartment_id")
	private Integer compartmentId;
	
	@Column(name = "code_id")
	private Integer codeId;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "identifier")
	private String identifier;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "response_desc")
	private String responseDesc;

	@UpdateTimestamp
	@Column(name = "last_update_date")
	private Date lastUpdatedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getSafeId() {
		return safeId;
	}

	public void setSafeId(Integer safeId) {
		this.safeId = safeId;
	}

	public Integer getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(Integer compartmentId) {
		this.compartmentId = compartmentId;
	}

	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}	

}
