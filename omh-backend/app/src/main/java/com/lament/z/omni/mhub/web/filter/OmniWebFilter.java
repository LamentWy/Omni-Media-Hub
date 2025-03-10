package com.lament.z.omni.mhub.web.filter;

import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

public class OmniWebFilter implements WebFilter {

	@Override
	@NonNull
	public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		if (isUnMappedPath(path)) {
			return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build());
		}
		return chain.filter(exchange);
	}

	/**
	 * 调整过滤逻辑改这里
	 * */
	private static boolean isUnMappedPath(String path) {

		return !path.startsWith("/z") &&
				!path.startsWith("/v3/api-docs") &&
				!path.contains(".") &&
				path.matches("/(.*)");
	}
}
