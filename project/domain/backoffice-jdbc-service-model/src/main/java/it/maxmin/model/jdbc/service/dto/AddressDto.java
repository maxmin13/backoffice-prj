package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Address;

public final class AddressDto implements Serializable {

	private static final long serialVersionUID = 1814335872466130231L;

	private String description;
	private String city;
	private String region;
	private String postalCode;
	private final StateDto state;
	private final Set<UserDto> users;

	public static AddressDto newInstance(String description, String city, String region, String postalCode,
			StateDto state, Set<UserDto> users) {
		return new AddressDto(description, city, region, postalCode, state, users);
	}

	public static AddressDto newInstance(Address address) {
		return new AddressDto(address.getDescription(), address.getCity(), address.getRegion(), address.getPostalCode(),
				StateDto.newInstance(address.getState()),
				address.getUsers().stream().map(UserDto::newInstance).collect(Collectors.toSet()));
	}

	AddressDto(String description, String city, String region, String postalCode, StateDto state, Set<UserDto> users) {
		super();
		this.description = description;
		this.city = city;
		this.region = region;
		this.postalCode = postalCode;
		this.state = state;
		this.users = users;
	}

	public String getDescription() {
		return description;
	}

	public String getCity() {
		return city;
	}

	public StateDto getState() {
		return state;
	}

	public String getRegion() {
		return region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public Set<UserDto> getUsers() {
		return new HashSet<>(users);
	}

	public Optional<UserDto> getUser(String accountName) {
		if (accountName == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(users.stream()
				.filter(each -> each.getCredentials().getAccountName().equals(accountName)).findFirst().orElse(null));
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(postalCode);
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
		AddressDto that = (AddressDto) obj;
		return postalCode.equals(that.postalCode);
	}

	@Override
	public String toString() {
		return "Address [description=" + description + ", city=" + city + ", state=" + state + ", region=" + region
				+ ", postalCode=" + postalCode + "]";
	}
}
