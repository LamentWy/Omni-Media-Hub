package com.lament.z.omni.mhub.common.exception;


import com.lament.z.omni.mhub.common.exception.ProblemDetailWithBuilder.ProblemDetailBuilder;
import com.lament.z.omni.mhub.service.exceptions.UserNotFoundException;
import com.lament.z.omni.mhub.web.exception.BasicBadRequestException;
import com.lament.z.omni.mhub.web.exception.UserUpdateFailedException;
import reactor.core.publisher.Mono;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

/**
 * Translate web exception to http response.<br/>
 * <p>
 * follow <a href="https://datatracker.ietf.org/doc/html/rfc9457">RFC9457</a>,and using ProblemDetail.
 * <br/>
 * <p>
 * 暂时没想到更好的方式，但也可以当个“模板”用了，就是好多东西得手动添加，
 * 由于之前为了给 PD 添加个 builder，导致现在得做不少强转。
 * 实际上至少这个项目根本用不到这么细的规则，反正我写到一半后悔了，好烦，所以其实也没完全 follow RFC9457。
 */
//@ControllerAdvice
//@Component
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

	public ExceptionTranslator() {
		// empty
	}

	/**
	 * @param exception 包括 webException 和 普通 rt-exception
	 */
	@ExceptionHandler
	public Mono<ResponseEntity<Object>> handleAllExceptons(Throwable exception, ServerWebExchange request) {

		// 根据异常类型拿到自定义的 pd 的实例
		ProblemDetailWithBuilder problemDetail = getProblemDetailByExceptionType(exception);
		// custom pd
		problemDetail = customProblemDetail(problemDetail, exception, request);
		// transferToResponseEntity and return.
		ResponseEntity<Object> responseEntity = transferToResponseEntity(problemDetail, exception);
		return handleExceptionInternal((Exception) exception,responseEntity.getBody(),responseEntity.getHeaders(),responseEntity.getStatusCode(),request);
	}

	@Override
	protected Mono<ResponseEntity<Object>> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, ServerWebExchange request) {
		if (request.getResponse().isCommitted()){
			return Mono.error(ex);
		}

		if (body == null){
			body = getProblemDetailByExceptionType(ex);
			body = customProblemDetail((ProblemDetailWithBuilder) body,ex,request);
		}

		return Mono.just(new ResponseEntity<>(body, headers, status));
	}

	/**
	 * 将异常转换为 Response。
	 * */
	private ResponseEntity<Object> transferToResponseEntity(ProblemDetailWithBuilder pd, Throwable e) {

		HttpHeaders headers;
		headers = (e instanceof BasicBadRequestException b) ? HeadersUtils.createBadRequestAlert(b.getErrorCode(), b.getErrorID()) : null;
		if (headers == null){
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		}

		HttpStatusCode statusCode = HttpStatus.valueOf(pd.getStatus());

		return new ResponseEntity<>(pd,headers,statusCode);
	}

	/**
	 * 定制 pd。
	 * <p>
	 *     能做的很多，主要围绕 PD 的那些属性制定你需要的规则即可。缺点就是要么你提前想好所有的异常处理，要么不断的手动往里加。
	 */
	private ProblemDetailWithBuilder customProblemDetail(ProblemDetailWithBuilder pd, Throwable e, ServerWebExchange request) {

		return pd;
	}

	/**
	 * 根据异常的类型拿到自定义的 pd 的实例。
	 * <p>
	 * 这里可以做 PD 相关的自定义，包括普通 rt-exception 也可以手写对应关系去获得 PD.
	 */
	@SuppressWarnings("java:S125")
	private ProblemDetailWithBuilder getProblemDetailByExceptionType(Throwable e) {
		// 自定义异常转换，从 rt-exception 转为逻辑上 1:1 对应的 web-exception，从而获取 PD。
		// 需要的时候可以模仿下面的写法。
		// 下方注释代码不要启用，他俩不是 1 对 1 关系，会造成概念混乱。
//		if (e instanceof DBUpdateFailedException) {
//			return (ProblemDetailWithBuilder) new UserUpdateFailedException().getBody();
//		}

		if (e instanceof UserNotFoundException){
			return (ProblemDetailWithBuilder) new UserUpdateFailedException().getBody();
		}

		// Spring 自带的 exception 但是你想修改一下默认的响应 而不是默认返回
		if (e instanceof AuthenticationServiceException) {
			return ProblemDetailBuilder.getBuilder()
					.title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
					.detail("client don't have permission to access this resource or provide a invalid credentials")
					// 可以在这里手动指定 httpcode，比如 .status(HttpStatus.UNAUTHORIZED.value())
					// 也可以利用这里提供的方法做一些自定义的规则并自动返回 httpStatus.
					.status(customAndGetHttpStatus(e).value())
					.build();
		}

		//  web-exception 的通用处理
		if (e instanceof ErrorResponse errorResponse &&
				errorResponse.getBody() instanceof ProblemDetailWithBuilder problemDetail) {
			return problemDetail;
		}

		return ProblemDetailBuilder.getBuilder().status(customAndGetHttpStatus(e).value()).build();
	}

	/**
	 * 根据异常类型自动返回 HttpStatus:
	 * <p>
	 * 可以在这里做一些自定义，既可以手动添加，也可以利用 {@code @ResponseStatus} 注解,详情见代码
	 *
	 * @return HttpStatus
	 */
	private HttpStatus customAndGetHttpStatus(Throwable e) {
		if (e instanceof ErrorResponse errorResponse) {
			return HttpStatus.valueOf(errorResponse.getBody().getStatus());
		}

		/* manually add zone 注意异常类型，如果是 ErrorResponse 类型的上一步就返回了 */
		if (e instanceof AuthenticationServiceException) return HttpStatus.UNAUTHORIZED;
		if (e instanceof AccessDeniedException) return HttpStatus.UNAUTHORIZED;
		if (e instanceof ConcurrencyFailureException) return HttpStatus.CONFLICT;

		/* 解析 @ResponseStatus 注解 */
		ResponseStatus anno = recurResponseStatus(e);
		if (anno != null) return anno.value();

		/* 500 兜底 */
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	private ResponseStatus recurResponseStatus(Throwable e) {
		ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
		return (annotation == null && e.getCause() != null) ? recurResponseStatus(e.getCause()) : annotation;
	}

}
