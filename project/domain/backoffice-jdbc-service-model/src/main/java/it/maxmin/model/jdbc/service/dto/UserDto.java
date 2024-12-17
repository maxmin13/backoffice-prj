package it.maxmin.model.jdbc.service.dto;

import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_BIRTH_DATE_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_DEPARTMENT_NAME_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_DEPARTMENT_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_LAST_NAME_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.User;

public final class UserDto implements Serializable {

	private static final long serialVersionUID = -9044903217593257701L;

	private final CredentialsDto credentials;
	private final LocalDate birthDate;
	private final DepartmentDto department;
	private final Set<AddressDto> addresses;
	private final Set<RoleDto> roles;

	public static UserDto newInstance(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		notNull(user.getDepartment(), ERROR_DEPARTMENT_NOT_NULL_MSG);
		String departmentName = null;
		if (user.getDepartment() != null) {
			departmentName = user.getDepartment().getName();
		}
		Set<AddressDto> addresses = null;
		if (user.getAddresses() != null) {
			addresses = user.getAddresses().stream().map(AddressDto::newInstance).collect(Collectors.toSet());
		}
		Set<RoleDto> roles = null;
		if (user.getRoles() != null) {
			roles = user.getRoles().stream().map(RoleDto::newInstance).collect(Collectors.toSet());
		}
		return newInstance(user.getAccountName(), user.getFirstName(), user.getLastName(), user.getBirthDate(),
				departmentName, addresses, roles);
	}

	public static UserDto newInstance(String accountName, String firstName, String lastName, LocalDate birthDate,
			String departmentName, Set<AddressDto> addresses, Set<RoleDto> roles) {
		return new UserDto(accountName, firstName, lastName, birthDate, departmentName, addresses, roles);
	}

	public static UserDto newInstance(String accountName, String firstName, String lastName, LocalDate birthDate,
			String departmentName) {
		return new UserDto(accountName, firstName, lastName, birthDate, departmentName, null, null);
	}

	UserDto(String accountName, String firstName, String lastName, LocalDate birthDate, String departmentName) {
		this(accountName, firstName, lastName, birthDate, departmentName, null, null);
	}

	UserDto(String accountName, String firstName, String lastName, LocalDate birthDate, String departmentName,
			Set<AddressDto> addresses, Set<RoleDto> roles) {
		super();
		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		notNull(firstName, ERROR_FIRST_NAME_NOT_NULL_MSG);
		notNull(lastName, ERROR_LAST_NAME_NOT_NULL_MSG);
		notNull(birthDate, ERROR_BIRTH_DATE_NOT_NULL_MSG);
		notNull(departmentName, ERROR_DEPARTMENT_NAME_NOT_NULL_MSG);
		this.credentials = CredentialsDto.newInstance(accountName, firstName, lastName);
		this.birthDate = birthDate;
		this.department = DepartmentDto.newInstance(departmentName);
		if (addresses != null) {
			this.addresses = addresses;
		}
		else {
			this.addresses = new HashSet<>();
		}
		if (roles != null) {
			this.roles = roles;
		}
		else {
			this.roles = new HashSet<>();
		}
	}

	public CredentialsDto getCredentials() {
		return credentials;
	}

	public DepartmentDto getDepartment() {
		return department;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Set<AddressDto> getAddresses() {
		return new HashSet<>(addresses);
	}

	public Optional<AddressDto> getAddress(String postalCode) {
		if (postalCode == null) {
			return Optional.empty();
		}
		return addresses.stream().filter(each -> each.getPostalCode().equals(postalCode)).findFirst();
	}

	public Set<RoleDto> getRoles() {
		return new HashSet<>(roles);
	}

	public Optional<RoleDto> getRole(String name) {
		if (name == null) {
			return Optional.empty();
		}
		return roles.stream().filter(each -> each.getName().equals(name)).findFirst();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(credentials.getAccountName());
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
		UserDto that = (UserDto) obj;
		return this.credentials.getAccountName().equals(that.credentials.getAccountName());
	}

	@Override
	public String toString() {
		return "User [accountName=" + credentials.getAccountName() + ", firstName=" + credentials.getFirstName()
				+ ", lastName=" + credentials.getLastName() + ", department=" + department + ", birthDate=" + birthDate
				+ ", addresses=" + addresses + ", roles=" + roles + "]";
	}

}
