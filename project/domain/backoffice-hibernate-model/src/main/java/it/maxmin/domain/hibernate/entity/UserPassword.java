package it.maxmin.domain.hibernate.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserPassword")
public class UserPassword extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String userId;
	private String value;
	private LocalDateTime effDate;
	private LocalDateTime endDate;

	@Column(name = "UserId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "Value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "EffDate")
	public LocalDateTime getEffDate() {
		return effDate;
	}

	public void setEffDate(LocalDateTime effDate) {
		this.effDate = effDate;
	}

	@Column(name = "EndDate")
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
