package com.maxmin.domain.hibernate.entities;

public class UserAddress {

	private Long userId;
	private Long addressId;
	
	public static UserAddress newInstance() {
		return new UserAddress();
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public UserAddress withUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	public Long getAddressId() {
		return addressId;
	}
	
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public UserAddress withAddressId(Long addressId) {
		this.addressId = addressId;
		return this;
	}
}
