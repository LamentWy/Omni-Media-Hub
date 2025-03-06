package com.lament.z.omni.mhub.scan;

public class UnSupportedFileTypeException extends Exception {
	public UnSupportedFileTypeException(String fileExtension) {
		super("Unsupported file extension: " + fileExtension);
	}
}
