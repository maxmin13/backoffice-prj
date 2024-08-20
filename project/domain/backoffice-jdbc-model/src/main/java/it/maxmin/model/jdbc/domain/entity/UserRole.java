package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserRole implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String roleName;

	public static UserRole newInstance() {
		return new UserRole();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserRole withId(Long id) {
		this.id = id;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public UserRole withRoleName(String roleName) {
		this.roleName = roleName;
		return this;
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
		UserRole that = (UserRole) obj;
		return roleName.equals(that.roleName);
	}

	@Override
	public String toString() {
		return  "UserRole [id=" + id + ", roleName=" + roleName + "]";
	}

}
