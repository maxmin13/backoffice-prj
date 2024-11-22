package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserPassword implements Serializable {

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
		this.id = id;
	}
	
	public UserPassword withId(Long id) {
		this.id = id;
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UserPassword withUser(User user) {
		this.user = user;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public UserPassword withValue(String value) {
		this.value = value;
		return this;
	}

	public LocalDateTime getEffDate() {
		return effDate;
	}

	public void setEffDate(LocalDateTime effDate) {
		this.effDate = effDate;
	}
	
	public UserPassword withEffDate(LocalDateTime effDate) {
		this.effDate = effDate;
		return this;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public UserPassword withEndDate(LocalDateTime endDate) {
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
