package it.maxmin.model.jdbc.dao.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PojoLoginAttempt implements Serializable {

	private static final long serialVersionUID = -5879326955628574552L;
	
	private Long id;
	private boolean success;
	private LocalDateTime createdAt;
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
