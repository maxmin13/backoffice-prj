package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Role;

public final class RoleDto implements Serializable {

	private static final long serialVersionUID = 2498809195948472094L;

	private String name;

	public static RoleDto newInstance(String name) {
		return new RoleDto(name);
	}

	public static RoleDto newInstance(Role role) {
		return new RoleDto(role.getName());
	}

	RoleDto(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(name);
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
		return name.equals(that.name);
	}

	@Override
	public String toString() {
		return "Role [name=" + name + "]";
	}

}
