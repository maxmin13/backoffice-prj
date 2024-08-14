package it.maxmin.model.jdbc;

public class Address {

	private Long addressId;
	private String address;
	private String city;
	private Long stateId;
	private String region;
	private String postalCode;
	
	public static Address newInstance() {
		return new Address();
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public Address withAddressId(Long addressId) {
		this.addressId = addressId;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Address withAddress(String address) {
		this.address = address;
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
