package it.maxmin.domain.jpa.entity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Department")
public class Department extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String name;
	private Set<User> users = new HashSet<>();

	public static Department newInstance() {
		return new Department();
	}

	public Department withId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department withName(String name) {
		this.name = name;
		return this;
	}

	// CascadeType.ALL: any operation done on the Department record are propagated to departments linked to it.
	// orphanRemoval attribute false: after the users Set has been updated, the User entries that no longer exist in 
	//                                the set should not be deleted from the database.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Department withUsers(Set<User> users) {
		this.users = users;
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
