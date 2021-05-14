package com.sma.smartauto.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Code details")
@Entity
@Table(name = "code")
public class Code {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "code", nullable = false)
	private String code;
		
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "safe_id", nullable = true)
	private Safe safe;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "asset_id", nullable = true)
	private Asset asset;

	@Column(name = "identifier", nullable = false)
	private String identifier;
	
	@Column(name = "identifier_type", nullable = false)
	private String identifierType;
	
	@Column(name = "date_time_from", nullable = false)
	private Date validFrom;
	
	@Column(name = "date_time_to", nullable = false)
	private Date validTill;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", nullable = true)
	private Location location;
	
	@Column(name = "active", nullable = true)
	private boolean active;
	
	@Column(name = "data_complete", nullable = true)
	private boolean dataComplete;
	
	@Column(name = "created_by", nullable = true)
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;

	@Column(name = "last_updated_by", nullable = true)
	private Integer lastUpdatedBy;

	@UpdateTimestamp
	@Column(name = "last_updated_datetime", nullable = false)
	private Date lastUpdatedDate;
	
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Safe getSafe() {
		return safe;
	}

	public void setSafe(Safe safe) {
		this.safe = safe;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
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

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getDataComplete() {
		return dataComplete;
	}

	public void setDataComplete(boolean dataComplete) {
		this.dataComplete = dataComplete;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@Override
	public String toString() {
		return "Code [id=" + id + ", code=" + code + ", client=" + client + ", safe=" + safe + ", asset=" + asset
				+ ", identifier=" + identifier + ", identifierType=" + identifierType + ", validFrom=" + validFrom
				+ ", validTill=" + validTill + ", location=" + location + ", active=" + active + ", dataComplete="
				+ dataComplete + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}	
		
}
