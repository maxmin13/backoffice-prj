package it.maxmin.model.jdbc;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UserRole implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;
	
	private Long id;
	private String roleName;
	private boolean active;
	private LocalDateTime createdDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
		
}
