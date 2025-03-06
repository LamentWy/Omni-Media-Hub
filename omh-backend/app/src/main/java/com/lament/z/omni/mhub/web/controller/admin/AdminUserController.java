package com.lament.z.omni.mhub.web.controller.admin;

import com.lament.z.omni.mhub.model.vm.AdminChangePassVM;
import com.lament.z.omni.mhub.model.vm.ChangePassVM;
import com.lament.z.omni.mhub.service.UserService;
import com.lament.z.omni.mhub.web.exception.UserUpdateFailedException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/z/admin/users")
public class AdminUserController {

	private static Logger log = LoggerFactory.getLogger(AdminUserController.class);

	private final UserService userService;

	public AdminUserController(UserService userService) {
		this.userService = userService;
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") Integer userId) {
		return userService.deleteUserById(userId)
				.then(Mono.just(ResponseEntity.noContent().build()));

	}

	/**
	 * 修改指定 id 用户的密码
	 *
	 * */
	@PostMapping("/change-password")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Mono<ResponseEntity<Object>> changePassword(@RequestBody @Valid AdminChangePassVM acpVM) {

		return userService
				.changePasswordById(acpVM.getId(),acpVM.getNewPassword())
				.then(Mono.just(ResponseEntity.noContent().build()))
				.onErrorResume(e -> Mono.error(new UserUpdateFailedException()));
	}
}


