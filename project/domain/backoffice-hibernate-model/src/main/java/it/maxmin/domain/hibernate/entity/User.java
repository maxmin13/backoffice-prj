package it.maxmin.domain.hibernate.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class User extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String accountName;
	private String firstName;
	private String lastName;
	private Department department;
	private LocalDate birthDate;
	private LocalDateTime createdDate;
	private Set<Address> addresses = new HashSet<>();

	public static User newInstance() {
		return new User();
	}

	public User withId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "AccountName", unique = true, updatable = false)
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

//	@Column(name = "Department")
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

	@Column(name = "BirtDate")
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

	@Column(name = "CreatedDate")
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public User withCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public boolean addAddress(Address address) {
		if (addresses.contains(address)) {
			return false;
		}
		else {
			addresses.add(address);
			return true;
		}
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
		if (!(obj instanceof User)) {
			return false;
		}
		User that = (User) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(accountName, that.accountName);
		return eb.isEquals();
	}
}
