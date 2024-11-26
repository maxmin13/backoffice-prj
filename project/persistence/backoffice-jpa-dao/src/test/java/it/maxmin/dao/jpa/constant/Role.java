package it.maxmin.dao.jpa.constant;

public enum Role {

	ADMINISTRATOR("Administrator"), WORKER("Worker"), USER("User");

	private String roleName;

	private Role(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

}
