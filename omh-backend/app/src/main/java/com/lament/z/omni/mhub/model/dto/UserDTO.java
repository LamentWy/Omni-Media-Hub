package com.lament.z.omni.mhub.model.dto;

import java.time.Instant;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.lament.z.omni.mhub.model.Role;
import com.lament.z.omni.mhub.model.User;

/**
 * User's DTO,
 * no password field
 * */
public class UserDTO {

	private Integer id;
	private String loginName;
	private String nickName;
	private Boolean activated;
	private Instant createdDate;
	private String createdBy;
	private Instant lastModifiedDate;
	private String lastModifiedBy;
	// set of role.RoleName
	private Set<String> roles;


	public UserDTO() {
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.loginName = user.getName();
		this.nickName = user.getNickName();
		this.activated = user.getActivated();
		this.createdDate = user.getCreatedDate();
		this.createdBy = user.getCreatedBy();
		this.lastModifiedDate = user.getLastModifiedDate();
		this.lastModifiedBy = user.getLastModifiedBy();
		this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", UserDTO.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("loginName='" + loginName + "'")
				.add("nickName='" + nickName + "'")
				.add("activated=" + activated)
				.add("createdDate=" + createdDate)
				.add("createdBy='" + createdBy + "'")
				.add("lastModifiedDate=" + lastModifiedDate)
				.add("lastModifiedBy='" + lastModifiedBy + "'")
				.add("roles=" + roles)
				.toString();
	}

}
