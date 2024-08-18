package it.maxmin.domain.hibernate.entity;

import java.io.Serializable;

public class Department implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String name;
	
	public static Department newInstance() {
		return new Department();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Department withId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department withName(String name) {
		this.name = name;
		return this;
	}
}
