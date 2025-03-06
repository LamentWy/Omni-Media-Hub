package com.lament.z.omni.mhub.model.vm;

import jakarta.validation.constraints.Size;

/**
 * A vm for user change password.
 * */
public class ChangePassVM {


	private String curPassword;
	/* 暂时只限制最小长度 */
	@Size(min = 5)
	private String newPassword;

	public ChangePassVM() {
		// for json
	}

	public String getCurPassword() {
		return curPassword;
	}

	public void setCurPassword(String curPassword) {
		this.curPassword = curPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "Don't print password";
	}
}
