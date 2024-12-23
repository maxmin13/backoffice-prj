package it.maxmin.model.jdbc.service.dto;

import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLE_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Role;

public final class RoleDto implements Serializable {

	private static final long serialVersionUID = 2498809195948472094L;

	private String name;

	public static RoleDto newInstance(Role role) {
		notNull(role, ERROR_ROLE_NOT_NULL_MSG);
		return newInstance(role.getName());
	}

	public static RoleDto newInstance(String name) {
		return new RoleDto(name);
	}

	RoleDto(String name) {
		notNull(name, ERROR_NAME_NOT_NULL_MSG);
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
