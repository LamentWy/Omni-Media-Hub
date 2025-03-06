package com.lament.z.omni.mhub.scan;

public enum ResourceType {
	VIDEO(0),
	AUDIO(1),
	BOOK(2),
	PIC(3),
	UNKNOWN(-1);
	private final int value;

	ResourceType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
