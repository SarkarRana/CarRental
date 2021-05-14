package com.sma.smartauto.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "users_id")
    Integer userId;
    
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "username")
    private String username;

//    @NotEmpty
    @Column(name = "password")
    private String password;
    
    @NotEmpty
    @Column(name = "temp_password")
    private String tempPassword;
    
    @NotEmpty
    @Column(name = "staff_card_code")
    private String staffCardCode;

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
    
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_role", nullable = false)
    private Role role;
    
    @OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "location_id", nullable = true)
	private Location location;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<String> roles = new ArrayList<>();
    	roles.add(role.getName());
        return roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }

    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
    public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
    public String getUsername() {
        return this.username;
    }

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
    public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public String getStaffCardCode() {
		return staffCardCode;
	}

	public void setStaffCardCode(String staffCardCode) {
		this.staffCardCode = staffCardCode;
	}

	public void setRole(Role role) {		
		this.role = role;
	}

    public Role getRole() {
		return role;
	}

	public Date getCreatedDate() {
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", client=" + client + ", username=" + username + ", password=" + password
				+ ", createdDate=" + createdDate + ", lastUpdatedDate=" + lastUpdatedDate + ", role=" + role + "]";
	}
}
