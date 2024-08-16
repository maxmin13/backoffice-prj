package com.maxmin.domain.hibernate.entities;

import java.io.Serial;
import java.time.LocalDateTime;

public class Password extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private Long userId;
	private String value;
	private boolean active;
	private LocalDateTime createDate;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

}
