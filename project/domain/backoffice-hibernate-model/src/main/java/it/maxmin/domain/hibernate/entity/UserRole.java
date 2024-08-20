package it.maxmin.domain.hibernate.entity;

import java.io.Serial;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserRole")
public class UserRole extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String roleName;

	@Column(name = "RoleName")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		UserRole that = (UserRole) obj;
		if (that.getId() != null && this.getId() != null) {
			return super.equals(obj);
		}
		return roleName.equals(that.roleName);
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", roleName=" + roleName + "]";
	}

}
