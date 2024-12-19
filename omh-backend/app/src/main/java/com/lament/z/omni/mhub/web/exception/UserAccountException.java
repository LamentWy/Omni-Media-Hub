package com.lament.z.omni.mhub.web.exception;

public class UserAccountException extends RuntimeException {
	public UserAccountException() {
		super("User not found!");
	}
}
