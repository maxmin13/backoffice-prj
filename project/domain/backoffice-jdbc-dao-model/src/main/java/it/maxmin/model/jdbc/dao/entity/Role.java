package it.maxmin.model.jdbc.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Role implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String name;

	public static Role newInstance() {
		return new Role();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
	}

	public Role withId(Long id) {
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

	public Role withName(String name) {
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
		Role that = (Role) obj;
		return name.equals(that.name);
	}

	@Override
	public String toString() {
		return  "Role [id=" + id + ", name=" + name + "]";
	}

}
