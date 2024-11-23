package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Role;

public final class RoleDto implements Serializable {

	private static final long serialVersionUID = 2498809195948472094L;

	private String roleName;

	public static RoleDto newInstance(String roleName) {
		return new RoleDto(roleName);
	}

	public static RoleDto newInstance(Role role) {
		return new RoleDto(role.getRoleName());
	}

	RoleDto(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
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
		RoleDto that = (RoleDto) obj;
		return roleName.equals(that.roleName);
	}

	@Override
	public String toString() {
		return "Role [roleName=" + roleName + "]";
	}

}
