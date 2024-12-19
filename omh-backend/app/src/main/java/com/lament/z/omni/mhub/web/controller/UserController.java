package com.lament.z.omni.mhub.web.controller;

import com.lament.z.omni.mhub.security.SecurityConstants;
import com.lament.z.omni.mhub.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/z/admin/users")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") Integer userId) {
		return userService.deleteUserById(userId)
				.then(Mono.just(ResponseEntity.noContent().build()));

	}
}
