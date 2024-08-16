package it.maxmin.dao.hibernate.pojo;

import java.io.Serializable;

public class UserUserRole implements Serializable {

	private static final long serialVersionUID = 7632536256395423354L;

	private Long userId;
	private Long userRoleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
}
