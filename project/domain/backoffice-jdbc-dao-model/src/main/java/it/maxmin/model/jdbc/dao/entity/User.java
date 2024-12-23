package it.maxmin.model.jdbc.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESSES_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_BIRTH_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_CREATED_AT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_LAST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLES_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VERSION_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
		return this;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		this.accountName = accountName;
	}

	public User withAccountName(String accountName) {
		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		this.accountName = accountName;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		notNull(firstName, ERROR_FIRST_NAME_NOT_NULL_MSG);
		this.firstName = firstName;
	}

	public User withFirstName(String firstName) {
		notNull(firstName, ERROR_FIRST_NAME_NOT_NULL_MSG);
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		notNull(lastName, ERROR_LAST_NAME_NOT_NULL_MSG);
		this.lastName = lastName;
	}

	public User withLastName(String lastName) {
		notNull(lastName, ERROR_LAST_NAME_NOT_NULL_MSG);
		this.lastName = lastName;
		return this;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		notNull(department, ERROR_DEPARTMENT_NOT_FOUND_MSG);
		this.department = department;
	}

	public User withDepartment(Department department) {
		notNull(department, ERROR_DEPARTMENT_NOT_FOUND_MSG);
		this.department = department;
		return this;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		notNull(birthDate, ERROR_BIRTH_DATE_NOT_NULL_MSG);
		this.birthDate = birthDate;
	}

	public User withBirthDate(LocalDate birthDate) {
		notNull(birthDate, ERROR_BIRTH_DATE_NOT_NULL_MSG);
		this.birthDate = birthDate;
		return this;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		notNull(createdAt, ERROR_CREATED_AT_NOT_NULL_MSG);
		this.createdAt = createdAt;
	}

	public User withCreatedAt(LocalDateTime createdAt) {
		notNull(createdAt, ERROR_CREATED_AT_NOT_NULL_MSG);
		this.createdAt = createdAt;
		return this;
	}
	
	public User withVersion(Integer version) {
		notNull(version, ERROR_VERSION_NOT_NULL_MSG);
		this.version = version;
		return this;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		notNull(addresses, ERROR_ADDRESSES_NOT_NULL_MSG);
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

	public Optional<Address> getAddress(String postalCode) {
		if (postalCode == null) {
			return Optional.empty();
		}
		return addresses.stream().filter(each -> each.getPostalCode().equals(postalCode)).findFirst();
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		notNull(roles, ERROR_ROLES_NOT_NULL_MSG);
		this.roles = roles;
	}

	public boolean addRole(Role role) {
		if (role == null || roles.contains(role)) {
			return false;
		}
		else {
			roles.add(role);
			return true;
		}
	}

	public Optional<Role> getRole(String name) {
		if (name == null) {
			return Optional.empty();
		}
		return roles.stream().filter(each -> each.getName().equals(name)).findFirst();
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
				+ ", birthDate=" + birthDate + ", createdAt=" + createdAt + ", version=" + version + ", department="
				+ department + ", addresses=" + addresses + ", roles=" + roles + "]";
	}

}
