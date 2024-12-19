package com.lament.z.omni.mhub.web.response;

public class JwtToken {
	private String token;
	public JwtToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
