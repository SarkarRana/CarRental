package com.sma.smartauto.domain;

import java.io.Serializable;
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
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(description="Asset details")
@Entity
@Table(name = "assets")
public class Asset implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "asset_id")
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "safe_id", nullable = true)
	private Safe safe;
	
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "compartment_id", referencedColumnName = "compartment_id")
	private Compartment compartment;
	
	@ApiModelProperty(notes="Name should have atleast 2 characters")
	@Size(min=2, message="Name should have atleast 2 characters")
	@Column(name = "asset_name")
	private String name;
	
	@Column(name = "identifier")
	private String identifier;
	
	@Column(name = "identifier_type")
	private String identifierType;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "asset_type_id", nullable = false)
	private AssetType assetType;
	
	@Column(name = "is_active", nullable = true)
	private boolean active;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Compartment getCompartment() {
		return compartment;
	}

	public void setCompartment(Compartment compartment) {
		this.compartment = compartment;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifierNum) {
		this.identifier = identifierNum;
	}

	public String getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
		return "Asset [id=" + id + ", client=" + client + ", safe=" + safe + ", compartment=" + compartment + ", name="
				+ name + ", identifierNum=" + identifier + ", identifierType=" + identifierType + ", assetType="
				+ assetType + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}
	

}
