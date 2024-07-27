package it.maxmin.model.plain.pojos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Password implements Serializable {

	private static final long serialVersionUID = 6363392891972889778L;

	private Long passwordId;
	private Long userId;
	private String value;
	private boolean active;
	private LocalDateTime createDate;

	public Long getPasswordId() {
		return passwordId;
	}

	public void setPasswordId(Long passwordId) {
		this.passwordId = passwordId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

}