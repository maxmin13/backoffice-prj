package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Role implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String roleName;

	public static Role newInstance() {
		return new Role();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role withId(Long id) {
		this.id = id;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Role withRoleName(String roleName) {
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
		Role that = (Role) obj;
		return roleName.equals(that.roleName);
	}

	@Override
	public String toString() {
		return  "Role [id=" + id + ", roleName=" + roleName + "]";
	}

}
