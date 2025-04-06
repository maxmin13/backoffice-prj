package it.maxmin.dao.jdbc.constant;

public enum Role {

	ADMINISTRATOR("Administrator"), WORKER("Worker"), USER("User");

	private String name;

	private Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
