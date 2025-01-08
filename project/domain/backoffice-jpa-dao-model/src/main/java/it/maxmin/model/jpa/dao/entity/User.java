package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESSES_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_BIRTH_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DEPARTMENT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_LAST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLES_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "AccountName"))
@NamedQuery(name = "User.findByAccountName", query = """
		          select distinct u
		               from User u
		               left join fetch u.department
		               left join fetch u.addresses
		               left join fetch u.roles
		               where u.accountName = :accountName
		""")
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "UserSeq")
	@GenericGenerator(name = "UserSeq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "UserSeq", value = "UserSeq"), @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "increment_size", value = "1") })
	@Column(name = "Id")
	private Long id;

	@Version
	@Column(name = "Version")
	private Integer version;

	@Size(min = 2, max = 60, message = "accountName is required, maximum 60 characters.")
	@Column(name = "AccountName", nullable = false)
	private String accountName;

	@Size(min = 2, max = 60, message = "firstName is required, maximum 60 characters.")
	@Column(name = "FirstName", nullable = false)
	private String firstName;

	@Size(min = 2, max = 60, message = "lastName is required, maximum 60 characters.")
	@Column(name = "LastName", nullable = false)
	private String lastName;

	@Column(name = "BirthDate", nullable = false)
	private LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "DepartmentId", nullable = false)
	private Department department;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "UserAddress", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "AddressId"))
	private Set<Address> addresses = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
	private Set<Role> roles = new HashSet<>();

	public static User newInstance() {
		return new User();
	}

	public Long getId() {
		return id;
	}

	User withId(Long id) {
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		notNull(department, ERROR_DEPARTMENT_NOT_NULL_MSG);
		this.department = department;
	}

	public User withDepartment(Department department) {
		notNull(department, ERROR_DEPARTMENT_NOT_NULL_MSG);
		this.department = department;
		return this;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	void setAddresses(Set<Address> addresses) {
		notNull(addresses, ERROR_ADDRESSES_NOT_NULL_MSG);
		this.addresses = addresses;
	}

	public Optional<Address> getAddress(String postalCode) {
		if (postalCode == null) {
			return Optional.empty();
		}
		return addresses.stream().filter(each -> each.getPostalCode().equals(postalCode)).findFirst();
	}

	public boolean addAddress(Address address) {
		if (address == null) {
			return false;
		}
		else {
			return addresses.add(address);
		}
	}

	public boolean removeAddress(Address address) {
		if (address == null) {
			return false;
		}
		else {
			return addresses.remove(address);
		}
	}

	public Set<Role> getRoles() {
		return roles;
	}

	void setRoles(Set<Role> roles) {
		notNull(roles, ERROR_ROLES_NOT_NULL_MSG);
		this.roles = roles;
	}

	public Optional<Role> getRole(String name) {
		if (name == null) {
			return Optional.empty();
		}
		return roles.stream().filter(each -> each.getName().equals(name)).findFirst();
	}

	public boolean addRole(Role role) {
		if (role == null) {
			return false;
		}
		else {
			return roles.add(role);
		}
	}

	public boolean removeRole(Role role) {
		if (role == null) {
			return false;
		}
		else {
			return roles.remove(role);
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.getAccountName());
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
		if (!(other instanceof User)) {
			return false;
		}
		User that = (User) other;
		return this.getAccountName().equals(that.getAccountName());
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", accountName=" + accountName + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", department=" + department + ", birthDate=" + birthDate + ", addresses=" + addresses + ", roles="
				+ roles + "]";
	}

}
