package com.maxmin.domain.hibernate.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LoginAttempt extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String userId;
	private boolean success;
	private LocalDateTime createdDate;

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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(userId).append(createdDate);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LoginAttempt)) {
			return false;
		}
		LoginAttempt that = (LoginAttempt) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(userId, that.userId).append(createdDate, createdDate);
		return eb.isEquals();
	}
}
