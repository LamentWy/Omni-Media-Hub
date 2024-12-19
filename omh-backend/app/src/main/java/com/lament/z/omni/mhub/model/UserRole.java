package com.lament.z.omni.mhub.model;

import java.util.StringJoiner;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class UserRole {

	@Id
	private Integer id;
	private Integer userId;
	private String roleName;

	public UserRole() {
	}

	public UserRole(Integer userId) {
		this.userId = userId;
	}

	public UserRole(Integer userId, String roleName) {
		this.id = null;
		this.userId = userId;
		this.roleName = roleName;
	}

	public UserRole(Integer id, Integer userId, String roleName) {
		this.id = id;
		this.userId = userId;
		this.roleName = roleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserRole userRole)) return false;

		return getId().equals(userRole.getId()) && getUserId().equals(userRole.getUserId()) && getRoleName().equals(userRole.getRoleName());
	}

	@Override
	public int hashCode() {
		return 31 + getRoleName().hashCode();
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", UserRole.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("userId=" + userId)
				.add("roleName='" + roleName + "'")
				.toString();
	}


}
