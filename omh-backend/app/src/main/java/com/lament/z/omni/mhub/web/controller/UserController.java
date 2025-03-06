package com.lament.z.omni.mhub.web.controller;

import com.lament.z.omni.mhub.model.vm.ChangePassVM;
import com.lament.z.omni.mhub.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/z/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 修改当前登录用户的密码。
	 * */
	@PostMapping("/change-password")
	public Mono<Void> changePassword(@RequestBody @Valid ChangePassVM pwdChangeVM) {
		if (log.isDebugEnabled()){
			log.debug("UserController | changePassword | ");
		}
		return userService.changeCurUserPWD(pwdChangeVM.getCurPassword(),pwdChangeVM.getNewPassword());
	}



}
