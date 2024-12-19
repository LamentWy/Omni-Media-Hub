package com.lament.z.omni.mhub.model.vm;

import java.util.StringJoiner;

public class LoginVM {

	// same as loginName
	private String userName;
	private String password;
	private Boolean rememberMe;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", LoginVM.class.getSimpleName() + "[", "]")
				.add("userName='" + userName + "'")
				.add("password= ******")
				.add("rememberMe=" + rememberMe)
				.toString();
	}
}
