package com.lament.z.omni.mhub.service.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException  extends AuthenticationException {
	public UserNotFoundException() {
		super("Can't find User !");

	}
}
