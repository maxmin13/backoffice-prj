package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_EFFECTIVE_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_END_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VALUE_NOT_NULL_MSG;
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
@Table(name = "UserPassword")
public class UserPassword implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "UserPasswordSeq")
	@GenericGenerator(
      name = "UserPasswordSeq",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "UserPasswordSeq", value = "UserPasswordSeq"),
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
	@Column(name = "Value")
	private String value;

	@NotNull
	@Column(name = "EffDate")
	private LocalDateTime effDate;

	@Column(name = "EndDate")
	private LocalDateTime endDate;

	@NotNull
	@OneToOne
	@JoinColumn(name = "UserId")
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

	public LocalDateTime getEffDate() {
		return effDate;
	}

	public void setEffDate(LocalDateTime effDate) {
		notNull(effDate, ERROR_EFFECTIVE_DATE_NOT_NULL_MSG);
		this.effDate = effDate;
	}

	public UserPassword withEffDate(LocalDateTime effDate) {
		notNull(effDate, ERROR_EFFECTIVE_DATE_NOT_NULL_MSG);
		this.effDate = effDate;
		return this;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		notNull(endDate, ERROR_END_DATE_NOT_NULL_MSG);
		this.endDate = endDate;
	}

	public UserPassword withEndDate(LocalDateTime endDate) {
		notNull(endDate, ERROR_END_DATE_NOT_NULL_MSG);
		this.endDate = endDate;
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
		hcb.append(user.getAccountName()).append(effDate);
		return hcb.toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		UserPassword that = (UserPassword) obj;
		return user.getAccountName().equals(that.user.getAccountName()) && effDate.equals(that.effDate);
	}

	public String toString() {
		return "UserPassword [id=" + id + ", user=" + user + ", value=xxxx, effDate=" + effDate + ", endDate=" + endDate
				+ "]";
	}
}
