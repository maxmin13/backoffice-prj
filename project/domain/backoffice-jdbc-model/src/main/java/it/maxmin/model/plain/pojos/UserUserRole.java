package it.maxmin.model.plain.pojos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserUserRole implements Serializable {

	private static final long serialVersionUID = 5553303836311353225L;

	private Long userId;
	private Long userRoleId;
	private LocalDateTime createdDate;
	private Set<Long> userIds;
	private Set<Long> userRoleIds;

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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Set<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(Set<Long> userIds) {
		this.userIds = userIds;
	}

	public boolean addUserId(Long userId) {
		if (userIds == null) {
			userIds = new HashSet<>();
			userIds.add(userId);
			return true;
		} else {
			if (userIds.contains(userId)) {
				return false;
			}
		}
		userIds.add(userId);
		return true;
	}

	public Set<Long> getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(Set<Long> userRoleIds) {
		this.userRoleIds = userRoleIds;
	}

	public boolean addUserRoleId(Long userRoleId) {
		if (userRoleIds == null) {
			userRoleIds = new HashSet<>();
			userRoleIds.add(userRoleId);
			return true;
		} else {
			if (userRoleIds.contains(userRoleId)) {
				return false;
			}
		}
		userRoleIds.add(userRoleId);
		return true;
	}
}
