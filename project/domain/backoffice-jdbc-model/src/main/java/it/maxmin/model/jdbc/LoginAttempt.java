package it.maxmin.model.jdbc;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class LoginAttempt implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private Long loginAttemptId;
	private String accountName;
	private boolean success;
	private LocalDateTime createdDate;
	
	public Long getLoginAttemptId() {
		return loginAttemptId;
	}
	public void setLoginAttemptId(Long loginAttemptId) {
		this.loginAttemptId = loginAttemptId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	
}
