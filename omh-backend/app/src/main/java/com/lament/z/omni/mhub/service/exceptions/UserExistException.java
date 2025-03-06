package com.lament.z.omni.mhub.service.exceptions;

public class UserExistException extends RuntimeException{
	public UserExistException(String name) {
		super("User: ["+name+"] already exist!");
	}
}
