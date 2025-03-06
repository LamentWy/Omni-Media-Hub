package com.lament.z.omni.mhub.scan;

import java.util.Arrays;

public enum FileType {
	MKV("mkv",ResourceType.VIDEO),
	MP4("mp4",ResourceType.VIDEO),
	UNSUPPORTED("unsupported",ResourceType.UNKNOWN),;
	public final String value;
	public final ResourceType resourceType;

	FileType(String value, ResourceType resourceType) {
		this.value = value;
		this.resourceType = resourceType;
	}

	public String value() {
		return value;
	}

	public ResourceType resourceType() {
		return resourceType;
	}

	/**
	 * 根据文件后缀判断资源类型
	 * */
	public static ResourceType determineResourceType(String fileExtension){

		return Arrays.stream(FileType.values()).filter(fileType -> fileType.value().equals(fileExtension)).findFirst()
				.map(FileType::resourceType).orElse(ResourceType.UNKNOWN);
	}
}
