package com.lament.z.omni.mhub.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

public class CustomReactiveWebExceptionHandler implements WebExceptionHandler {

	private final ExceptionTranslator translator;
	private final ObjectMapper mapper;

	public CustomReactiveWebExceptionHandler(ExceptionTranslator translator, ObjectMapper mapper) {
		this.translator = translator;
		this.mapper = mapper;
	}

	@Override
	public Mono<Void> handle(final ServerWebExchange request,final Throwable ex) {
		if (ex instanceof ResponseStatusException) {
			final Mono<ResponseEntity<Object>> respMono = translator.handleAllExceptons(ex,request);
			return respMono.flatMap(responseEntity -> this.setHttpResponse(responseEntity, request, mapper));
		}
		return Mono.error(ex);
	}

	private Mono<Void> setHttpResponse(ResponseEntity<Object> responseEntity, ServerWebExchange request, ObjectMapper mapper) {
		final ServerHttpResponse response = request.getResponse();
		response.setStatusCode(responseEntity.getStatusCode());
		response.getHeaders().addAll(responseEntity.getHeaders());
		try {
			final DataBuffer buffer = response.bufferFactory().wrap(mapper.writeValueAsBytes(responseEntity.getBody()));
			return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
		} catch (final JsonProcessingException ex) {
			return Mono.error(ex);
		}
	}
}
