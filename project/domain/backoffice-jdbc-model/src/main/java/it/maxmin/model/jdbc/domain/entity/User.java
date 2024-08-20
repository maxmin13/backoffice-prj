package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class User implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String accountName;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private LocalDateTime createdAt;
	private Department department;
	private Set<Address> addresses = new HashSet<>();
	private Set<UserRole> roles = new HashSet<>();

	public static User newInstance() {
		return new User();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User withId(Long id) {
		this.id = id;
		return this;
	}

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

	public Set<Address> getAddresses() {
		return addresses;
	}

	void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public boolean addAddress(Address address) {
		if (address == null || addresses.contains(address)) {
			return false;
		}
		else {
			addresses.add(address);
			return true;
		}
	}

	public Address getAddress(String postalCode) {
		if (postalCode == null) {
			return null;
		}
		return addresses.stream().filter(each -> each.getPostalCode().equals(postalCode)).findFirst().orElse(null);
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public boolean addRole(UserRole role) {
		if (role == null || roles.contains(role)) {
			return false;
		}
		else {
			roles.add(role);
			return true;
		}
	}

	public UserRole getRole(String roleName) {
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
