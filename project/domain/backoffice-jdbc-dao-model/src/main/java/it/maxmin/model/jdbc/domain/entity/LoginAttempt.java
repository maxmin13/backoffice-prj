package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LoginAttempt implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private long id;
	private boolean success;
	private LocalDateTime createdAt;
	private User user;

	public static LoginAttempt newInstance() {
		return new LoginAttempt();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public LoginAttempt withId(long id) {
		this.id = id;
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoginAttempt withUser(User user) {
		this.user = user;
		return this;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public LoginAttempt withSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LoginAttempt withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(user.getAccountName()).append(createdAt);
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
		LoginAttempt that = (LoginAttempt) obj;
		return user.getAccountName().equals(that.user.getAccountName()) && createdAt.equals(that.createdAt);
	}

	@Override
	public String toString() {
		return "LoginAttempt [user=" + user + ", success=" + success + ", createdAt=" + createdAt + "]";
	}
}
