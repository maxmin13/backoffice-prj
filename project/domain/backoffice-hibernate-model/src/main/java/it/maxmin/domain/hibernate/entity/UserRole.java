package it.maxmin.domain.hibernate.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserRole")
public class UserRole extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@Column(unique = true, updatable = false)
	private String roleName;
	private boolean active;
	private LocalDateTime createdDate;

	@Column(name = "RoleName")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "Active")
	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "CreatedDate")
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
