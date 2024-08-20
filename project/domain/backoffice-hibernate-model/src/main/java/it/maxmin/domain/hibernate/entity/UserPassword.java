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
@Table(name = "UserPassword")
public class UserPassword extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String value;
	private LocalDateTime effDate;
	private LocalDateTime endDate;
	private User user;
	
	public static UserPassword newInstance() {
		return new UserPassword();
	}

	@OneToOne
	@JoinColumn(name = "UserId")
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

	@Column(name = "Value")
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

	@Column(name = "EffDate")
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

	@Column(name = "EndDate")
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
