package it.maxmin.dao.jdbc.impl.constant;

public enum UserRole {

	ADMINISTRATOR("Administrator"), WORKER("Worker"), USER("User");

	private String roleName;

	private UserRole(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

}
