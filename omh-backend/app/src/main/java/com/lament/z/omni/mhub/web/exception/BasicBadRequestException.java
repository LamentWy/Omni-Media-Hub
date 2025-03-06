package com.lament.z.omni.mhub.web.exception;

import java.net.URI;

import com.lament.z.omni.mhub.common.exception.ErrorURIConstants;
import com.lament.z.omni.mhub.common.exception.ProblemDetailWithBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class BasicBadRequestException extends ErrorResponseException {
	// 自定义的错误码和错误 ID，仅供演示在 header 中返回业务向的错误代码。
	// doesn't mean anything for now
	private final Integer errorCode;
	private final String errorID;

	public BasicBadRequestException(String defaultMsg, Integer errorCode, String errorID) {
		this(ErrorURIConstants.BLANK_TYPE, defaultMsg, errorCode, errorID);
	}

	public BasicBadRequestException(URI type, String defaultMsg, Integer errorCode, String errorID) {
		super(
				HttpStatus.BAD_REQUEST,
				ProblemDetailWithBuilder.ProblemDetailBuilder.getBuilder()
						.type(type)
						.status(HttpStatus.BAD_REQUEST.value())
						.title(defaultMsg)
						.propertie("errorCode",errorCode)
						.propertie("errorId", errorID)
						.build(),
				null
		);
		this.errorCode = errorCode;
		this.errorID = errorID;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorID() {
		return errorID;
	}

}
