package com.lament.z.omni.mhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Video {
	@Id
	private Long id;
	private String title;
	private String fileName;
	private String filePath;
	private String cover;
	private String description;

	public Video() {
		//
	}

	public Video(Long id, String title, String fileName, String filePath, String cover, String description) {
		this.id = id;
		this.title = title;
		this.fileName = fileName;
		this.filePath = filePath;
		this.cover = cover;
		this.description = description;
	}

	public Video(String fileName, String filePath) {
		this.title = fileName;
		this.fileName = fileName;
		this.filePath = filePath;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
