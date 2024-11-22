package it.maxmin.model.jpa.dao.entity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Role")
public class Role extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String roleName;
	private Set<User> users = new HashSet<>();

	public static Role newInstance() {
		return new Role();
	}

	public Role withId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "RoleName")
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Role", joinColumns = @JoinColumn(name = "RoleId"), inverseJoinColumns = @JoinColumn(name = "UserId"))
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Role withUsers(Set<User> users) {
		this.users = users;
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
		return "Role [id=" + id + ", roleName=" + roleName + "]";
	}

}
