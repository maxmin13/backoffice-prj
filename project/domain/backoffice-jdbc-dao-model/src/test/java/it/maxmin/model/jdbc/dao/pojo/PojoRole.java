package it.maxmin.model.jdbc.dao.pojo;

import java.io.Serializable;

public class PojoRole implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;
	
	private Long id;
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
			
}
