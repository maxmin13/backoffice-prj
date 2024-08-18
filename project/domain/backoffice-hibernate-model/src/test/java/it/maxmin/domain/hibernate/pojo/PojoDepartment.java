package it.maxmin.domain.hibernate.pojo;

import java.io.Serializable;

public class PojoDepartment implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long id;
	private String name;
	
	public static PojoDepartment newInstance() {
		return new PojoDepartment();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PojoDepartment withId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PojoDepartment withName(String name) {
		this.name = name;
		return this;
	}
}
