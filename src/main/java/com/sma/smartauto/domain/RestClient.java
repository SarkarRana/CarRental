package com.sma.smartauto.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity
@Table(name = "rest_clients")

public class RestClient implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long restClientId;

    @NotEmpty
    @Column(name = "rest_client_name")
    private String restClientName;

    @NotEmpty
    @Column(name = "rest_client_key")
    private String restClientKey;

    
    @Column(name = "role", nullable = false)
    private String role;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_datetime", nullable = false)
    private Date createdDate;
    
    @Column(name = "active", nullable = false)
    private boolean active;
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<String> roles = new ArrayList<>();
    	roles.add(role);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }


	public Long getRestClientId() {
		return restClientId;
	}


	public void setRestClientId(Long restClientId) {
		this.restClientId = restClientId;
	}


	public String getRestClientName() {
		return restClientName;
	}


	public void setRestClientName(String restClientName) {
		this.restClientName = restClientName;
	}


	public String getRestClientKey() {
		return restClientKey;
	}


	public void setRestClientKey(String restClientKey) {
		this.restClientKey = restClientKey;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return restClientKey;
	}


	@Override
	public String getUsername() {
		return restClientName;
	}


	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isEnabled() {
		return active;
	}
	
}
