package com.lament.z.omni.mhub.common.exception;

import org.springframework.http.HttpHeaders;

public final class HeadersUtils {

	private HeadersUtils() {}

	public static HttpHeaders createBadRequestAlert(Integer errorCode, String errorID) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-" + errorID + "-error", errorCode.toString());
		return headers;
	}
}
