package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_EFFECTIVE_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_END_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VALUE_NOT_NULL_MSG;
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
@Table(name = "UserPassword", uniqueConstraints = @UniqueConstraint(columnNames = { "UserId", "EffDate" }))
public class UserPassword implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "UserPasswordSeq")
	@GenericGenerator(name = "UserPasswordSeq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "UserPasswordSeq", value = "UserPasswordSeq"),
			@Parameter(name = "initial_value", value = "100"), @Parameter(name = "increment_size", value = "1") })
	@Column(name = "Id")
	private Long id;

	@Version
	@Column(name = "Version")
	private Integer version;

	@Column(name = "Value", nullable = false)
	private String value;

	@Column(name = "EffDate", nullable = false)
	private Instant effDate;

	@Column(name = "EndDate")
	private Instant endDate;

	@OneToOne
	@JoinColumn(name = "UserId", nullable = false)
	private User user;

	public static UserPassword newInstance() {
		return new UserPassword();
	}

	public Long getId() {
		return id;
	}

	UserPassword withId(Long id) {
		this.id = id;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		notNull(value, ERROR_VALUE_NOT_NULL_MSG);
		this.value = value;
	}

	public UserPassword withValue(String value) {
		notNull(value, ERROR_VALUE_NOT_NULL_MSG);
		this.value = value;
		return this;
	}

	public Instant getEffDate() {
		return effDate;
	}

	public void setEffDate(Instant effDate) {
		notNull(effDate, ERROR_EFFECTIVE_DATE_NOT_NULL_MSG);
		this.effDate = effDate.truncatedTo(ChronoUnit.SECONDS);
	}

	public UserPassword withEffDate(Instant effDate) {
		setEffDate(effDate);
		return this;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		notNull(endDate, ERROR_END_DATE_NOT_NULL_MSG);
		this.endDate = endDate.truncatedTo(ChronoUnit.SECONDS);
	}

	public UserPassword withEndDate(Instant endDate) {
		setEndDate(endDate);
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		this.user = user;
	}

	public UserPassword withUser(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		this.user = user;
		return this;
	}

	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.getUser().getAccountName()).append(this.getEffDate());
		return hcb.toHashCode();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof UserPassword)) {
			return false;
		}
		UserPassword that = (UserPassword) other;
		// effDate is truncated to seconds precision because the field may be returned
		// with different precision by Hibernate, see entity retrived in different
		// persistent contexts.
		return this.getUser().getAccountName().equals(that.getUser().getAccountName())
				&& this.getEffDate().equals(that.getEffDate());
	}

	public String toString() {
		return "UserPassword [id=" + id + ", user=" + user + ", value=xxxx, effDate=" + effDate + ", endDate=" + endDate
				+ "]";
	}
}
