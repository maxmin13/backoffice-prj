package it.maxmin.model.jdbc.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_CREATED_AT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_SUCCESS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LoginAttempt implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private Boolean success;
	private LocalDateTime createdAt;
	private User user;

	public static LoginAttempt newInstance() {
		return new LoginAttempt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
	}
	
	public LoginAttempt withId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		this.user = user;
	}

	public LoginAttempt withUser(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		this.user = user;
		return this;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		notNull(success, ERROR_SUCCESS_NOT_NULL_MSG);
		this.success = success;
	}

	public LoginAttempt withSuccess(Boolean success) {
		notNull(success, ERROR_SUCCESS_NOT_NULL_MSG);
		this.success = success;
		return this;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		notNull(createdAt, ERROR_CREATED_AT_NOT_NULL_MSG);
		this.createdAt = createdAt;
	}

	public LoginAttempt withCreatedAt(LocalDateTime createdAt) {
		notNull(createdAt, ERROR_CREATED_AT_NOT_NULL_MSG);
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
