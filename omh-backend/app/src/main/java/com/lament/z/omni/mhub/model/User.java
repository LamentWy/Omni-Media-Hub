package com.lament.z.omni.mhub.model;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class User {

	@Id
	private Integer id;
	@Column("login_name")
	private String name;
	@Column("password_hash")
	private String password;
	private String nickName;
	private Boolean activated;
	@CreatedDate
	private Instant createdDate;
	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private Instant lastModifiedDate;
	@LastModifiedBy
	private String lastModifiedBy;
	@Transient
	private Set<Role> roles = new HashSet<>();


	public User(Integer id, String name, String password, String nickName, Boolean activated) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.nickName = nickName;
		this.activated = activated;
	}

	public User() {
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("name='" + name + "'")
				.add("password= ****** ")
				.add("activated=" + activated)
				.add("createdDate=" + createdDate)
				.add("createdBy='" + createdBy + "'")
				.add("lastModifiedDate=" + lastModifiedDate)
				.add("lastModifiedBy='" + lastModifiedBy + "'")
				.add("roles=" + roles)
				.toString();
	}
}
