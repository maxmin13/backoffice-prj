package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Department;

public final class DepartmentDto implements Serializable {

	private static final long serialVersionUID = -1854171785540126258L;

	private String name;

	public static DepartmentDto newInstance(String name) {
		return new DepartmentDto(name);
	}

	public static DepartmentDto newInstance(Department department) {
		return new DepartmentDto(department.getName());
	}

	DepartmentDto(String name) {
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
		DepartmentDto that = (DepartmentDto) obj;
		return name.equals(that.name);
	}

	@Override
	public String toString() {
		return "Department [name=" + name + "]";
	}
}
