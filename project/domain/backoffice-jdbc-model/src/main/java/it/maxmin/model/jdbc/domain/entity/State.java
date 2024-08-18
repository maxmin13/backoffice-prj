package it.maxmin.model.jdbc.domain.entity;

import java.io.Serializable;

public class State implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String name;
	private String code;
	
	public static State newInstance() {
		return new State();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public State withId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public State withName(String name) {
		this.name = name;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public State withCode(String code) {
		this.code = code;
		return this;
	}
}
