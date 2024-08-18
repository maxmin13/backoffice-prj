package it.maxmin.domain.hibernate.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "LoginAttempt")
public class LoginAttempt extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private User user;
	private boolean success;
	private LocalDateTime createdAt;

	@OneToOne
	@JoinColumn(name = "UserId")
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

	@Column(name = "Success")
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

	@Column(name = "CreatedAt")
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
		hcb.append(user.id).append(createdAt);
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
		if (!super.equals(obj)) {
			return false;
		}
		LoginAttempt that = (LoginAttempt) obj;
		if (that.getId() != null && this.getId() != null) {
			return super.equals(obj);
		}
		return user.id.equals(that.user.id) && createdAt.equals(that.createdAt);
	}

	@Override
	public String toString() {
		return "LoginAttempt [id=" + id + ", user=" + user + ", success=" + success + ", createdAt=" + createdAt + "]";
	}
	
}
