package com.lament.z.omni.mhub.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.lament.z.omni.mhub.model.Role;
import com.lament.z.omni.mhub.model.UserRole;
import com.lament.z.omni.mhub.model.User;
import com.lament.z.omni.mhub.repository.UserRoleRepository;
import com.lament.z.omni.mhub.repository.UserRepository;
import com.lament.z.omni.mhub.security.SecurityConstants;
import com.lament.z.omni.mhub.utils.SecurityUtils;
import com.lament.z.omni.mhub.web.exception.UserExistException;
import com.lament.z.omni.mhub.model.vm.AdminUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 创建用户
	 * */
	@Transactional
	public Mono<User> registerUser(AdminUserVM adminUserVM) {
		return userRepository.findByName(adminUserVM.getLoginName().toLowerCase())
				.flatMap(exist -> {
					if (exist != null){
						return Mono.error(new UserExistException(exist.getName()));
					}
					return Mono.empty();
				}).then(Mono.fromCallable( () -> {
					User user = new User();
					user.setName(adminUserVM.getLoginName().toLowerCase());
					user.setPassword(passwordEncoder.encode(adminUserVM.getPassword()));
					user.setActivated(true);
					Set<Role> roles = new HashSet<>();
					// 默认为普通用户
					roles.add(Role.getDefualRole());
					user.setRoles(roles);
					return user;
				}))
				.flatMap(this::saveUserAndRole)
				.doOnNext(user -> log.debug("User: [ {} ] created.",user));
	}


	@Transactional
	public Mono<User> saveUserAndRole(User user) {
		return SecurityUtils.getCurrentUserNameFromSecurityContext()
				.switchIfEmpty(Mono.just(SecurityConstants.SYSTEM))
				.flatMap( currentUser -> {
					if (user.getCreatedBy() == null){
						user.setCreatedBy(currentUser);
					}
					user.setLastModifiedBy(currentUser);
					return userRepository.save(user)
							.flatMap(savedUser ->
								Flux.fromIterable(user.getRoles())
										.flatMap(role -> userRoleRepository.save(new UserRole(savedUser.getId(),role.getName())))
										.then(Mono.just(savedUser))
							);
				});
	}

	/**
	 * 注意：
	 * 尽管设计上看 user - role 是 1:n 的关系，并且也支持创建 1:n 的关系。
	 * 但目前实际上只有 1:1 的关系，也就是说用户的角色要么是 user 要么是 admin
	 * 因此删除 user-role 的关系时只删一次。
	 * */
	@Transactional
	public Mono<Void> deleteUserById(Integer userId) {
		return userRepository.findById(userId)
				.flatMap(userRepository::delete)
				.then(Mono.defer(() -> userRoleRepository.deleteUserId(userId)));
	}

	@Transactional(readOnly = true)
	public Mono<User> getCurrentUserWithAuth() {

	return SecurityUtils.getCurrentUserNameFromSecurityContext()
				.flatMap(this::getOneUserWithRole);
	}

	/**
	 * @param name : User.name
	 *
	 * */
	private Mono<User> getOneUserWithRole(String name) {


		return userRepository.findOneWithActivatedByName(name)
				.flatMap(user -> userRoleRepository.findByUserId(user.getId())
						.collect(Collectors.toSet())
						.flatMap(userRoles -> Flux.fromIterable(userRoles)
								.all(userRole -> user.getRoles().add(new Role(userRole.getRoleName())))
						)
						.then(Mono.just(user)));
	}
}
