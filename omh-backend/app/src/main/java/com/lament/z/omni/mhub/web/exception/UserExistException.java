package com.lament.z.omni.mhub.web.exception;

public class UserExistException extends RuntimeException{
	public UserExistException(String name) {
		super("User: ["+name+"] already exist!");
	}
}
