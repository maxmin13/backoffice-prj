package it.maxmin.model.jdbc.service.dto;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_CITY_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_CODE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DESCRIPTION_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_POSTAL_CODE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_REGION_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

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

	public static AddressDto newInstance(Address address) {
		notNull(address, ERROR_ADDRESS_NOT_NULL_MSG);
		notNull(address.getState(), ERROR_STATE_NOT_NULL_MSG);
		return newInstance(address.getDescription(), address.getCity(), address.getRegion(), address.getPostalCode(),
				StateDto.newInstance(address.getState()));
	}

	public static AddressDto newInstance(String description, String city, String region, String postalCode,
			String stateName, String stateCode) {
		return newInstance(description, city, region, postalCode, StateDto.newInstance(stateName, stateCode));
	}

	public static AddressDto newInstance(String description, String city, String region, String postalCode,
			StateDto state) {
		return new AddressDto(description, city, region, postalCode, state);
	}

	AddressDto(String description, String city, String region, String postalCode, StateDto state) {
		super();
		notNull(description, ERROR_DESCRIPTION_NOT_NULL_MSG);
		notNull(city, ERROR_CITY_NOT_NULL_MSG);
		notNull(region, ERROR_REGION_NOT_NULL_MSG);
		notNull(postalCode, ERROR_POSTAL_CODE_NOT_NULL_MSG);
		notNull(state, ERROR_STATE_NOT_NULL_MSG);
		notNull(state.getName(), ERROR_NAME_NOT_NULL_MSG);
		notNull(state.getCode(), ERROR_CODE_NOT_NULL_MSG);
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
