package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Immutable
@Table(name = "Role", uniqueConstraints = @UniqueConstraint(columnNames = "Name"))
public class Role implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@Id
	@Column(name = "Id")
	private Long id;

	@NotNull
	@Column(name = "Name", nullable = false)
	private String name;
	
	public static Role newInstance() {
		return new Role();
	}

	public Long getId() {
		return this.id;
	}
	
	Role withId(Long id) {
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
		hcb.append(this.getName());
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof Role)) {
			return false;
		}
		Role that = (Role) other;
		return this.getName().equals(that.getName());
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}

}
