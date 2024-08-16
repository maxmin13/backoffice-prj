package com.maxmin.domain.hibernate.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;

public class UserRole extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@Column(unique = true, updatable = false)
	private String roleName;
	private boolean active;
	private LocalDateTime createdDate;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(roleName);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserRole)) {
            return false;
        }
        UserRole that = (UserRole) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(roleName, that.roleName);
        return eb.isEquals();
    }
}
