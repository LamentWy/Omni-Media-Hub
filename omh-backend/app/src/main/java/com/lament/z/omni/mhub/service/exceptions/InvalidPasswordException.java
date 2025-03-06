package com.lament.z.omni.mhub.service.exceptions;

public class InvalidPasswordException extends RuntimeException {
	public InvalidPasswordException() {
		super(" Password is incorrect! ");
	}
}
