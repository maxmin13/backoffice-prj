package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_LOGIN_AT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_SUCCESS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "LoginAttempt", uniqueConstraints = @UniqueConstraint(columnNames = { "UserId", "LoginAt" }))
public class LoginAttempt implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "LoginAttemptSeq")
	@GenericGenerator(name = "LoginAttemptSeq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "LoginAttemptSeq", value = "LoginAttemptSeq"),
			@Parameter(name = "initial_value", value = "100"), @Parameter(name = "increment_size", value = "1") })
	@Column(name = "Id")
	private Long id;

	@Version
	@Column(name = "Version")
	private Integer version;

	@Column(name = "Success", nullable = false)
	private Boolean success;

	@Column(name = "LoginAt", nullable = false)
	private Instant loginAt;

	@OneToOne
	@JoinColumn(name = "UserId", referencedColumnName = "id", nullable = false)
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

	public Instant getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Instant loginAt) {
		notNull(loginAt, ERROR_LOGIN_AT_NOT_NULL_MSG);
		this.loginAt = loginAt.truncatedTo(ChronoUnit.SECONDS);
	}

	public LoginAttempt withLoginAt(Instant loginAt) {
		setLoginAt(loginAt);
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(getUser().getAccountName()).append(getLoginAt());
		return hcb.toHashCode();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof LoginAttempt)) {
			return false;
		}
		LoginAttempt that = (LoginAttempt) other;
		// loginAt is truncated to seconds precision because the field may be returned
		// with different precision by Hibernate, see entity retrived in different
		// persistent contexts.
		return getUser().getAccountName().equals(that.getUser().getAccountName())
				&& getLoginAt().equals(that.getLoginAt());
	}

	@Override
	public String toString() {
		return "LoginAttempt [user=" + user + ", success=" + success + ", loginAt=" + loginAt + "]";
	}
}
