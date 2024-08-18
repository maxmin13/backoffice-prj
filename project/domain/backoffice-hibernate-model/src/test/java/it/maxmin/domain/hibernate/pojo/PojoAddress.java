package it.maxmin.domain.hibernate.pojo;

import java.io.Serializable;

public class PojoAddress implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;
	
	private Long id;
	private String description;
	private String city;
	private Long stateId;
	private String region;
	private String postalCode;
	
	public static PojoAddress newInstance() {
		return new PojoAddress();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PojoAddress withId(Long id) {
		this.id = id;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public PojoAddress withDescription(String description) {
		this.description = description;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public PojoAddress withCity(String city) {
		this.city = city;
		return this;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	
	public PojoAddress withStateId(Long stateId) {
		this.stateId = stateId;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	public PojoAddress withRegion(String region) {
		this.region = region;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public PojoAddress withPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}
}
