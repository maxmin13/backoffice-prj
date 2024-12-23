package it.maxmin.model.jdbc.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_EFFECTIVE_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_END_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VALUE_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserPassword implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String value;
	private LocalDateTime effDate;
	private LocalDateTime endDate;
	private User user;
	
	public static UserPassword newInstance() {
		return new UserPassword();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
	}
	
	public UserPassword withId(Long id) {
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
	
	public UserPassword withUser(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		this.user = user;
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

	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(user.getAccountName()).append(effDate);
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
		UserPassword that = (UserPassword) obj;
		return user.getAccountName().equals(that.user.getAccountName()) && effDate.equals(that.effDate);
	}

	@Override
	public String toString() {
		return "UserPassword [id=" + id + ", user=" + user + ", value=xxxx, effDate=" + effDate + ", endDate=" + endDate
				+ "]";
	}
}
