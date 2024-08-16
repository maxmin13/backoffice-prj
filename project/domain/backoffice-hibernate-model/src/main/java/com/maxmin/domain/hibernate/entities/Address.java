package com.maxmin.domain.hibernate.entities;

import java.io.Serial;

public class Address extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String description;
	private String city;
	private Long stateId;
	private String region;
	private String postalCode;
	
	public static Address newInstance() {
		return new Address();
	}
	
	public Address withId(Long id) {
		this.id = id;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Address withDescription(String description) {
		this.description = description;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public Address withCity(String city) {
		this.city = city;
		return this;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	
	public Address withStateId(Long stateId) {
		this.stateId = stateId;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	public Address withRegion(String region) {
		this.region = region;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public Address withPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}
}
