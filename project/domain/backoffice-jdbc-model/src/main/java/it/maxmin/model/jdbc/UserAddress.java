package it.maxmin.model.jdbc;

import java.io.Serial;
import java.io.Serializable;

public class UserAddress implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

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
