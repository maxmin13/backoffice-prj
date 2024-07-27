package it.maxmin.model.plain.pojos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserRole implements Serializable {
	
	private static final long serialVersionUID = 7930838172341521178L;
	
	private Long userRoleId;
	private String roleName;
	private boolean active;
	private LocalDateTime createdDate;
	
	public Long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
