package it.maxmin.dao.jdbc.impl.constant;

public enum State {
	ITALY("Italy","IT"), IRELAND("Ireland","IE");
	
	private String name;
	private String code;
	
	private State(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
}
