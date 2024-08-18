package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoginAttempt implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private String userId;
	private boolean success;
	private LocalDateTime createdAt;

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
