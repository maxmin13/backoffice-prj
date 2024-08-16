package com.maxmin.domain.hibernate.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserPassword extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String userId;
	private String value;
	private LocalDateTime effDate;
	private LocalDateTime endDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDateTime getEffDate() {
		return effDate;
	}

	public void setEffDate(LocalDateTime effDate) {
		this.effDate = effDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(userId).append(effDate);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserPassword)) {
			return false;
		}
		UserPassword that = (UserPassword) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(userId, that.userId).append(effDate, that.effDate);
		return eb.isEquals();
	}
}
