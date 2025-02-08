package it.maxmin.service.jdbc.constant;

public enum Department {

	ACCOUNTING("Accounting"), LEGAL("Legal"), PRODUCTION("Production");

	private String name;

	private Department(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
