package com.lament.z.omni.mhub.web.exception;

import com.lament.z.omni.mhub.common.exception.ErrorURIConstants;

public class UserUpdateFailedException  extends BasicBadRequestException {
	public UserUpdateFailedException() {
		// TODO change type, defaultMsg, 统一错误码。
		super(ErrorURIConstants.BLANK_TYPE, "更新用户信息失败",10001,"user_update_failed");
	}
}
