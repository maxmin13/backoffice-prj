package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_CREATED_AT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_SUCCESS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "LoginAttempt")
public class LoginAttempt implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "LoginAttemptSeq")
	@GenericGenerator(
      name = "LoginAttemptSeq",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "LoginAttemptSeq", value = "LoginAttemptSeq"),
        @Parameter(name = "initial_value", value = "100"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	@Column(name = "Id")
	private Long id;

	@Version
	@Column(name = "Version")
	private Integer version;

	@NotNull
	@Column(name = "Success")
	private Boolean success;

	@NotNull
	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;

	@NotNull
	@OneToOne
	@JoinColumn(name = "UserId", referencedColumnName = "id")
	private User user;

	public static LoginAttempt newInstance() {
		return new LoginAttempt();
	}

	public Long getId() {
		return id;
	}

	LoginAttempt withId(Long id) {
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
