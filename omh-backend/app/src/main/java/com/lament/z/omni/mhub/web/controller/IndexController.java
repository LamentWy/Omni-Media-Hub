package com.lament.z.omni.mhub.web.controller;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	@GetMapping("/")
	public Mono<String> greeting() {
		return Mono.just("Hello Webflux!");
	}
}
