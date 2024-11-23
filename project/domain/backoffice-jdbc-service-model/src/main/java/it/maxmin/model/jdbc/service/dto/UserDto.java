package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.User;

public final class UserDto implements Serializable {

	private static final long serialVersionUID = -9044903217593257701L;

	private final CredentialsDto credentials;
	private final LocalDate birthDate;
	private final LocalDateTime createdAt;
	private final DepartmentDto department;
	private final Set<AddressDto> addresses;
	private final Set<RoleDto> roles;

	public static UserDto newInstance(CredentialsDto credentials, LocalDate birthDate, LocalDateTime createAt,
			DepartmentDto department, Set<AddressDto> addresses, Set<RoleDto> roles) {
		return new UserDto(credentials, birthDate, createAt, department, addresses, roles);
	}

	public static UserDto newInstance(User user) {
		CredentialsDto credentials = CredentialsDto.newInstance(user.getAccountName(), user.getFirstName(),
				user.getLastName());
		DepartmentDto department = DepartmentDto.newInstance(user.getDepartment().getName());
		return new UserDto(credentials, user.getBirthDate(), user.getCreatedAt(), department,
				user.getAddresses().stream().map(AddressDto::newInstance).collect(Collectors.toSet()),
				user.getRoles().stream().map(RoleDto::newInstance).collect(Collectors.toSet()));
	}

	UserDto(CredentialsDto credentials, LocalDate birthDate, LocalDateTime createAt, DepartmentDto department,
			Set<AddressDto> addresses, Set<RoleDto> roles) {
		super();
		this.credentials = credentials;
		this.birthDate = birthDate;
		this.createdAt = createAt;
		this.department = department;
		this.addresses = addresses;
		this.roles = roles;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Set<AddressDto> getAddresses() {
		return new HashSet<>(addresses);
	}

	public Optional<AddressDto> getAddress(String postalCode) {
		if (postalCode == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(
				addresses.stream().filter(each -> each.getPostalCode().equals(postalCode)).findFirst().orElse(null));
	}

	public Set<RoleDto> getRoles() {
		return new HashSet<>(roles);
	}

	public Optional<RoleDto> getRole(String roleName) {
		if (roleName == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(
				roles.stream().filter(each -> each.getRoleName().equals(roleName)).findFirst().orElse(null));
	}

	public User toEntity() {
		User user = User.newInstance().withAccountName(credentials.getAccountName())
				.withFirstName(credentials.getFirstName()).withLastName(credentials.getLastName())
				.withBirthDate(birthDate).withCreatedAt(createdAt).withDepartment(department.toEntity());
		user.setAddresses(this.getAddresses().stream()
				.map(a -> Address.newInstance().withCity(a.getCity()).withDescription(a.getDescription())
						.withPostalCode(a.getPostalCode()).withRegion(a.getRegion()).withState(a.getState().toEntity()))
				.collect(Collectors.toSet()));
		user.setRoles(this.getRoles().stream().map(r -> Role.newInstance().withRoleName(r.getRoleName()))
				.collect(Collectors.toSet()));

		return user;
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
				+ ", createdAt=" + createdAt + ", addresses=" + addresses + ", roles=" + roles + "]";
	}

}
