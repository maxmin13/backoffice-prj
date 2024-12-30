package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USERS_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Immutable
@Table(name = "Department", uniqueConstraints = @UniqueConstraint(columnNames = "Name"))
public class Department implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@Id
	@Column(name = "Id")
	private Long id;

	@Column(name = "Name", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
	private Set<User> users = new HashSet<>();

	public static Department newInstance() {
		return new Department();
	}

	public Long getId() {
		return this.id;
	}

	Department withId(Long id) {
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		notNull(users, ERROR_USERS_NOT_NULL_MSG);
		this.users = users;
	}

	public Department withUsers(Set<User> users) {
		notNull(users, ERROR_USERS_NOT_NULL_MSG);
		this.users = users;
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
		if (!(other instanceof Department)) {
			return false;
		}
		Department that = (Department) other;
		return this.getName().equals(that.getName());
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
}
