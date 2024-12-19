package com.lament.z.omni.mhub.model.vm;

import java.util.StringJoiner;

import com.lament.z.omni.mhub.model.dto.UserDTO;

/**
 * view model for admin-system UI
 *
 * adding password field.
 * */
public class AdminUserVM extends UserDTO {
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * do not include password.
	 * */
	@Override
	public String toString() {
		return new StringJoiner(", ", AdminUserVM.class.getSimpleName() + "[", "]")
				.add(super.toString())
				.toString();
	}
}
