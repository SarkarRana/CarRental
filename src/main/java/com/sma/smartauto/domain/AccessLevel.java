package com.sma.smartauto.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "access_level")
public class AccessLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "access_level_id")
    private Long id;

    @Column(name = "access_desc")
    private String accessDesc;

	@CreationTimestamp
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;
	
	@UpdateTimestamp
	@Column(name = "last_updated_datetime", nullable = false)
	private Date lastUpdatedDate;
    
    public AccessLevel() {
        super();
    }

    public AccessLevel(final String accessDesc) {
        super();
        this.accessDesc = accessDesc;
    }

    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getAccessDesc() {
        return accessDesc;
    }

    public void setAccessDesc(final String accessDesc) {
        this.accessDesc = accessDesc;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessDesc == null) ? 0 : accessDesc.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccessLevel other = (AccessLevel) obj;
        if (accessDesc == null) {
            if (other.accessDesc != null)
                return false;
        } else if (!accessDesc.equals(other.accessDesc))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "AccessLevel [id=" + id + ", accessDesc=" + accessDesc + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}

}
