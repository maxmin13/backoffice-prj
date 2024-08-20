package it.maxmin.domain.hibernate.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PojoUser implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String accountName;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private LocalDateTime createdAt;
	private Long departmentId;
	
	public static PojoUser newInstance() {
		return new PojoUser();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PojoUser withId(Long id) {
		this.id = id;
		return this;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public PojoUser withAccountName(String accountName) {
		this.accountName = accountName;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public PojoUser withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public PojoUser withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public PojoUser withDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
		return this;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public PojoUser withBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public PojoUser withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}
	
}
