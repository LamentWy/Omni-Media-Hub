package com.lament.z.omni.mhub.model;

import java.time.Instant;
import java.util.StringJoiner;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class ResourceCollection {
	@Id
	private Long id;
	private String name;
	private String description;
	/**
	 * 0 管理员可见，1 用户可见，系统自动创建的 collection 默认为 0。
	 * */
	private Integer visible;
	@InsertOnlyProperty
	@CreatedBy
	private String createdBy;
	@InsertOnlyProperty
	@CreatedDate
	private Instant createdDate;
	@LastModifiedBy
	private String lastModifiedBy;
	@LastModifiedDate
	private Instant lastModifiedDate;


	public ResourceCollection() {
		//
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ResourceCollection.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("name='" + name + "'")
				.add("description='" + description + "'")
				.add("visible=" + visible)
				.add("createdBy='" + createdBy + "'")
				.add("createdDate=" + createdDate)
				.add("lastModifiedBy='" + lastModifiedBy + "'")
				.add("lastModifiedDate=" + lastModifiedDate)
				.toString();
	}
}
