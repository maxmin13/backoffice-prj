package it.maxmin.model.jpa.dao.pojo;

import java.io.Serializable;

public class PojoRole implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;
	
	private Long id;
	private String roleName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
			
}
