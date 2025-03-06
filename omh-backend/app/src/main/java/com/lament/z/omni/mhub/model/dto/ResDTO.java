package com.lament.z.omni.mhub.model.dto;

import java.util.StringJoiner;

public class ResDTO {
	private Integer id;
	private String title;
	private String fileName;
	private String filePath;
	private String description;
	private String cover;
	private Integer cId;
	private String cName;
	private String cDesc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getcDesc() {
		return cDesc;
	}

	public void setcDesc(String cDesc) {
		this.cDesc = cDesc;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ResDTO.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("title='" + title + "'")
				.add("fileName='" + fileName + "'")
				.add("filePath='" + filePath + "'")
				.add("description='" + description + "'")
				.add("cover='" + cover + "'")
				.add("cId=" + cId)
				.add("cName='" + cName + "'")
				.add("cDesc='" + cDesc + "'")
				.toString();
	}

}
