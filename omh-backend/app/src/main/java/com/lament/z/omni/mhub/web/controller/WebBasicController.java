package com.lament.z.omni.mhub.web.controller;


import com.lament.z.omni.mhub.model.dto.UserDTO;
import com.lament.z.omni.mhub.utils.SecurityUtils;
import com.lament.z.omni.mhub.service.UserService;
import com.lament.z.omni.mhub.model.vm.AdminUserVM;
import com.lament.z.omni.mhub.model.vm.LoginVM;
import com.lament.z.omni.mhub.web.exception.UserAccountException;
import com.lament.z.omni.mhub.web.response.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/z")
public class WebBasicController {

	private static final Logger log = LoggerFactory.getLogger(WebBasicController.class);

	@Value("${omni-media-hub.security.auth.jwt.validity-in-seconds}")
	private long tokenValidity;
	@Value("${omni-media-hub.security.auth.jwt.remember-me-validity-in-seconds}")
	private long tokenValidity4Remember;

	private final JwtEncoder jwtEncoder;
	private final UserService userService;
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;


	public WebBasicController(JwtEncoder jwtEncoder, UserService userService, ReactiveAuthenticationManager reactiveAuthenticationManager) {
		this.jwtEncoder = jwtEncoder;
		this.userService = userService;
		this.reactiveAuthenticationManager = reactiveAuthenticationManager;
	}


	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> registerAccount(@RequestBody AdminUserVM adminUserVM) {
		return userService.registerUser(adminUserVM).then();
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<JwtToken>> login(@RequestBody Mono<LoginVM> loginMono){

		return loginMono.flatMap(
				login -> reactiveAuthenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(login.getUserName(),login.getPassword()))
						.flatMap(auth -> Mono.fromCallable(() -> SecurityUtils.createToken(auth, login.getRememberMe(), tokenValidity, tokenValidity4Remember, jwtEncoder)
						))
				)
				.map(jwtStr -> {
					HttpHeaders headers = new HttpHeaders();
					headers.setBearerAuth(jwtStr);
					return new ResponseEntity<>(new JwtToken(jwtStr),headers,HttpStatus.OK);
				});
	}

	/**
	 * get current userInfo
	 *
	 * @return current user
	 * @throws RuntimeException {@code 500 Internal Server Error}, if can't find user.
	 *
	 * */
	@GetMapping("cur")
	public Mono<UserDTO> getCurrentUser(){

		return userService.getCurrentUserWithAuth()
				.map(UserDTO::new)
				.switchIfEmpty(Mono.error(new UserAccountException()));
	}

}
