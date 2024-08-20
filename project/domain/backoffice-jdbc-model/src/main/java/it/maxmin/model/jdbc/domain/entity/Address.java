package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Address implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;
	
	private Long id;
	private String description;
	private String city;
	private String region;
	private String postalCode;
	private State state;
	
	public static Address newInstance() {
		return new Address();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public Address withState(State state) {
		this.state = state;
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
		Address that = (Address) obj;
		return postalCode.equals(that.postalCode);
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", description=" + description + ", city=" + city + ", state=" + state
				+ ", region=" + region + ", postalCode=" + postalCode + "]";
	}
}
