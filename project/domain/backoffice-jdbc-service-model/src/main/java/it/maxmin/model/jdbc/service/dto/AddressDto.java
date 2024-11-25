package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.Address;

public final class AddressDto implements Serializable {

	private static final long serialVersionUID = 1814335872466130231L;

	private String description;
	private String city;
	private String region;
	private String postalCode;
	private final StateDto state;

	public static AddressDto newInstance(String description, String city, String region, String postalCode,
			StateDto state) {
		return new AddressDto(description, city, region, postalCode, state);
	}

	public static AddressDto newInstance(Address address) {
		return new AddressDto(address.getDescription(), address.getCity(), address.getRegion(), address.getPostalCode(),
				StateDto.newInstance(address.getState()));
	}

	AddressDto(String description, String city, String region, String postalCode, StateDto state) {
		super();
		this.description = description;
		this.city = city;
		this.region = region;
		this.postalCode = postalCode;
		this.state = state;
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
