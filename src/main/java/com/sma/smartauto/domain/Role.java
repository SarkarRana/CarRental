package com.sma.smartauto.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_roles_id")
    private Long id;

    /*@OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="role_access_mapping",
    joinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")},
    inverseJoinColumns = {@JoinColumn(name="access_level_id", referencedColumnName="id")}
)
    private Collection<AccessLevel> privileges;*/
	@OneToMany
	private Collection<AccessLevel> privileges = new ArrayList<>();

    @Column(name = "role_desc")
    private String name;
    
	@CreationTimestamp
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;
	
	@UpdateTimestamp
	@Column(name = "last_updated_datetime", nullable = false)
	private Date lastUpdatedDate;

    public Role() {
        super();
    }

    public Role(final String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<AccessLevel> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(final Collection<AccessLevel> privileges) {
        this.privileges = privileges;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role role = (Role) obj;
        if (!role.equals(role.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Role [name=").append(name).append("]").append("[id=").append(id).append("]");
        return builder.toString();
    }
}