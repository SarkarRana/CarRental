package com.sma.smartauto.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Client details")
@Entity
@Table(name = "client_type")
public class ClientType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_type_id")
	private Integer id;
	
	@ApiModelProperty(notes="Name should have atleast 2 characters")
	@Size(min=2, message="Name should have atleast 2 characters")
	@Column(name = "client_type_desc")
	private String clientTypeDesc;
	
	/*@Column(name = "created_by", nullable = false)
	private String createdBy;
	
	@CreationTimestamp
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;
	
    @Column(name = "last_updated_by", nullable = false)
    private String lastUpdatedBy;
	
	@UpdateTimestamp
	@Column(name = "last_updated_datetime", nullable = false)
	private Date lastUpdatedDate;*/
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientTypeDesc() {
		return clientTypeDesc;
	}

	public void setClientTypeDesc(String clientTypeDesc) {
		this.clientTypeDesc = clientTypeDesc;
	}

	/*public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Override
	public String toString() {
		return "ClientType [id=" + id + ", clientTypeDesc=" + clientTypeDesc + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}*/

}
