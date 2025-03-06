package com.lament.z.omni.mhub.model.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminChangePassVM {

	@NotNull
	private Integer id;

	@Size(min = 5)
	private String newPassword;

	public @NotNull Integer getId() {
		return id;
	}

	public void setId(@NotNull Integer id) {
		this.id = id;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
