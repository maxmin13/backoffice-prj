package it.maxmin.dao.hibernate.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {

	private Long userId;
	private String accountName;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private LocalDateTime createdDate;
	private List<Address> addresses = new ArrayList<>();;

	public static User newInstance() {
		return new User();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User withUserId(Long userId) {
		this.userId = userId;
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

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
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
}
