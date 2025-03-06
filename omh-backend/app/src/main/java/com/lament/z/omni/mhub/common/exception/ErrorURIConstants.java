package com.lament.z.omni.mhub.common.exception;

import java.net.URI;

/**
 * TODO 这玩意后期再说吧...
 *
 * 考虑挂在 {@code  https://lament-z.com/omh} 路径下
 *
 * 开发期间用 DEFAULT_EMPTY_TYPE
 * */
public class ErrorURIConstants {
	public static final URI BLANK_TYPE = URI.create("about:blank");
	public static final String BASE_URL = "http://localhost:8080/problems";
}
