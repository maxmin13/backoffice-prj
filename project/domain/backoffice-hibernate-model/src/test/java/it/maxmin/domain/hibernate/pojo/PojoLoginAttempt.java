package it.maxmin.domain.hibernate.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PojoLoginAttempt implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private boolean success;
	private LocalDateTime createdAt;
	private String userId;
	
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
