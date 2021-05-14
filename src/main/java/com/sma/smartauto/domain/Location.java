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

@ApiModel(description = "Locations data")
@Entity
@Table(name = "locations")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "location_id")
	private Integer locId;

	@Column(name = "location_name", nullable = false)
	private String name;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

/*	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "safe_id", nullable = false)
	private Safe safe;*/
	
	//ToDO; region, city and country ID will be added later if required
	
	@Column(name = "region_name", nullable = false)
	private String region;
	
	@Column(name = "city_name", nullable = false)
	private String city;
	
	@Column(name = "country_name", nullable = false)
	private String country;
	
	@Column(name = "address1", nullable = false)
	private String address1;
	
	@Column(name = "address2", nullable = false)
	private String address2;
	
	@Column(name = "latitude", nullable = false)
	private String latitude;
	
	@Column(name = "longitude", nullable = false)
	private String longitude;
		
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

	public Integer getCompartmentId() {
		return locId;
	}

	public void setCompartmentId(Integer compartmentId) {
		this.locId = compartmentId;
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

	public Integer getLocId() {
		return locId;
	}

	public void setLocId(Integer locId) {
		this.locId = locId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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

	/*public Safe getSafe() {
		return safe;
	}

	public void setSafe(Safe safe) {
		this.safe = safe;
	}*/

	@Override
	public String toString() {
		return "Location [locId=" + locId + ", name=" + name + ", client=" + client + ", region="
				+ region + ", city=" + city + ", country=" + country + ", address1=" + address1 + ", address2="
				+ address2 + ", latitude=" + latitude + ", longitude=" + longitude + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDate="
				+ lastUpdatedDate + "]";
	}

}
