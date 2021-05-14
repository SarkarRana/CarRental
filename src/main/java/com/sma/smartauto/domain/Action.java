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

import io.swagger.annotations.ApiModel;

@ApiModel(description="Action history")
@Entity
@Table(name = "actions")
public class Action {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "action_id")
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "action_type_id", nullable = false)
	private ActionType actionType;
	
	@Column(name = "date_time", nullable = false)
	private Date dateTime;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "safe_id", nullable = true)
	private Safe safe;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "compartment_id", nullable = true, referencedColumnName = "compartment_id")
	private Compartment compartment;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "asset_id", nullable = true)
	private Asset asset;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "code_id", nullable = true)
	private Code code;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Action [id=" + id + ", actionType=" + actionType + ", dateTime=" + dateTime + ", client=" + client
				+ ", safe=" + safe + ", compartment=" + compartment + ", asset=" + asset + ", code=" + code + ", user="
				+ user + "]";
	}


}
