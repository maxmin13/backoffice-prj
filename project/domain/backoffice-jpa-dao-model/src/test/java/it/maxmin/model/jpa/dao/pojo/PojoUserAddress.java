package it.maxmin.model.jpa.dao.pojo;

import java.io.Serializable;

public class PojoUserAddress implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long userId;
	private Long addressId;

	public static PojoUserAddress newInstance() {
		return new PojoUserAddress();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public PojoUserAddress withUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public PojoUserAddress withAddressId(Long addressId) {
		this.addressId = addressId;
		return this;
	}
}
