package it.maxmin.model.jdbc.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Department implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String name;
	
	public static Department newInstance() {
		return new Department();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
	}
	
	public Department withId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		notNull(name, ERROR_NAME_NOT_NULL_MSG);
		this.name = name;
	}

	public Department withName(String name) {
		notNull(name, ERROR_NAME_NOT_NULL_MSG);
		this.name = name;
		return this;
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
		Department that = (Department) obj;
		return name.equals(that.name);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
}
