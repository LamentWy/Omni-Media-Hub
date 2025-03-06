package com.lament.z.omni.mhub.model.dto;

import com.lament.z.omni.mhub.model.ResourceCollection;

public class ResCollectionDTO {
	private Long id;
	private String name;
	private String description;
	/**
	 * 0 管理员可见，1 用户可见，系统自动创建的 collection 默认为 0。
	 * */
	private Integer visible;
	private String createdBy;


	public ResCollectionDTO() {
	}


	public ResCollectionDTO(ResourceCollection resourceCollection){
		this.id = resourceCollection.getId();
		this.name = resourceCollection.getName();
		this.description = resourceCollection.getDescription();
		this.visible = resourceCollection.getVisible();
		this.createdBy = resourceCollection.getCreatedBy();
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
}
