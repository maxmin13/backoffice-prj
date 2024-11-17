package it.maxmin.domain.jpa.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
@NamedQuery(name = "User.findByAccountName", query = """
		          select distinct u
		               from User u
		               left join fetch u.department
		               left join fetch u.addresses
		               left join fetch u.roles
		               where u.accountName = :accountName
		""")
public class User extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String accountName;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private LocalDateTime createdAt;
	private Department department;
	private Set<Address> addresses = new HashSet<>();
	private Set<Role> roles = new HashSet<>();

	public static User newInstance() {
		return new User();
	}

	public User withId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "AccountName")
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public User withAccountName(String accountName) {
		this.accountName = accountName;
		return this;
	}

	@Column(name = "FirstName")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public User withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	@Column(name = "LastName")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	@Column(name = "BirthDate")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public User withBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	@Column(name = "CreatedAt")
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	@ManyToOne
	@JoinColumn(name = "DepartmentId")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public User withDepartment(Department department) {
		this.department = department;
		return this;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "UserAddress", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "AddressId"))
	public Set<Address> getAddresses() {
		return addresses;
	}

	void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public boolean addAddress(Address address) {
		if (address == null || addresses.contains(address)) {
			return false;
		} else {
			addresses.add(address);
			return true;
		}
	}
	
	public boolean removeAddress(Address address) {
		if (address == null || !addresses.contains(address)) {
			return false;
		} else {
			addresses.remove(address);
			return true;
		}
	}

	public Address getAddress(String postalCode) {
		if (postalCode == null) {
			return null;
		}
		return addresses.stream().filter(each -> each.getPostalCode().equals(postalCode)).findFirst().orElse(null);
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
	public Set<Role> getRoles() {
		return roles;
	}

	void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean addRole(Role role) {
		if (role == null || roles.contains(role)) {
			return false;
		} else {
			roles.add(role);
			return true;
		}
	}
	
	public boolean removeRole(Role role) {
		if (role == null || !roles.contains(role)) {
			return false;
		} else {
			roles.remove(role);
			return true;
		}
	}

	public Role getRole(String roleName) {
		if (roleName == null) {
			return null;
		}
		return roles.stream().filter(each -> each.getRoleName().equals(roleName)).findFirst().orElse(null);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(accountName);
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
		User that = (User) obj;
		return accountName.equals(that.accountName);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", accountName=" + accountName + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", department=" + department + ", birthDate=" + birthDate + ", createdAt=" + createdAt
				+ ", addresses=" + addresses + ", roles=" + roles + "]";
	}

}
