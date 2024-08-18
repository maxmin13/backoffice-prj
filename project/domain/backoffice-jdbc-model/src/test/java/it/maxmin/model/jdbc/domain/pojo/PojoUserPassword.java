package it.maxmin.model.jdbc.domain.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PojoUserPassword implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String userId;
	private String value;
	private LocalDateTime effDate;
	private LocalDateTime endDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

}
