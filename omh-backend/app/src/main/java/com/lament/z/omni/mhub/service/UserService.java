package com.lament.z.omni.mhub.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.lament.z.omni.mhub.model.Role;
import com.lament.z.omni.mhub.model.User;
import com.lament.z.omni.mhub.model.UserRole;
import com.lament.z.omni.mhub.model.vm.AdminUserVM;
import com.lament.z.omni.mhub.repository.UserRepository;
import com.lament.z.omni.mhub.repository.UserRoleRepository;
import com.lament.z.omni.mhub.security.SecurityConstants;
import com.lament.z.omni.mhub.service.exceptions.DBUpdateFailedException;
import com.lament.z.omni.mhub.service.exceptions.InvalidPasswordException;
import com.lament.z.omni.mhub.service.exceptions.UserExistException;
import com.lament.z.omni.mhub.service.exceptions.UserNotFoundException;
import com.lament.z.omni.mhub.service.handler.TransactionHandler;
import com.lament.z.omni.mhub.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lament.z.omni.mhub.model.constants.UserConstants.DefaultNickName;
import static com.lament.z.omni.mhub.model.constants.UserConstants.DefaultResetPassword;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;
	private final TransactionHandler transactionHandler;

	public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, TransactionHandler transactionHandler) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
		this.transactionHandler = transactionHandler;
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
					if (adminUserVM.getNickName() != null && !adminUserVM.getNickName().isEmpty()){
						user.setNickName(adminUserVM.getNickName());
					}else {
						user.setNickName(DefaultNickName);
					}
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

	/**
	 * 根据 user.id 修改对应的密码
	 * <p>
	 * 手写 sql 的一个例子，注意需要自行检查异常，update 语句检查异常的标准是影响行数，delete 同理。
	 * auditing 也需要自行处理，这里顺便用了 spEL 表达式。
	 * */
	@Transactional
	public Mono<Void> changePasswordById(Integer id, String newPassword) {
		String encryptedPWD = passwordEncoder.encode(newPassword);
		return SecurityUtils.getCurrentUserNameFromSecurityContext()
						.flatMap(curUserName -> {
							User user = new User();
							user.setId(id);
							user.setPassword(encryptedPWD);
							user.setLastModifiedBy(curUserName);
							user.setLastModifiedDate(Instant.now());
							return userRepository.changePasswordByUserId(user);
						})
				.doOnNext( row -> log.debug("changePasswordById | user-id: [{}] and affected row [{}]",id , row))
				.flatMap(row -> {
					if (row != 1){
						return Mono.error(new DBUpdateFailedException(row));
					}
					return Mono.empty();
				}).then();
	}


	/**
	 * resetPassword by userName
	 * */
	@Transactional
	public Mono<Void> resetPassword(String userName) {

		return userRepository.findOneWithActivatedByName(userName)
				.switchIfEmpty(Mono.error( new UserNotFoundException()))
				.map(user -> {
					User update = new User();
					update.setId(user.getId());
					update.setPassword(DefaultResetPassword);
					return update;
				})
				.flatMap(update ->
						transactionHandler
								.runWithTransaction(()-> this.updateUserUsingSave(update)))
				.then();
	}

	@Transactional
	public Mono<Void> changeCurUserPWD(String curPassword, String newPassword) {
		return SecurityUtils.getCurrentUserNameFromSecurityContext()
				.flatMap(userRepository::findOneWithActivatedByName)
				.<User>handle((user, sink) -> {
					String curEncryptedPWD = user.getPassword();
					if (!passwordEncoder.matches(curPassword,curEncryptedPWD)) {
						sink.error(new InvalidPasswordException());
						return;
					}
					User update = new User();
					update.setId(user.getId());
					update.setPassword(newPassword);
					sink.next(update);
				})
				.flatMap(update -> transactionHandler
						.runWithTransaction( () ->
								this.updateUserUsingSave(update)))
				.then();
	}


	/**
	 * 通用 updateUser 方法，可通过 transactionHandler::runWithTransaction 来添加事务。
	 * <p>
	 * 已知缺陷：查询次数略多，好在没啥影响。
	 *
	 * @param update update 中的非空属性为待更新的值。
	 *                  update.id 不能为空，update.name 不允许修改，update.password 无需提前加密, audit 字段无需手动处理.
	 *
	 * @return 更新后的 User 对象，密码字段永远返回 null.
	 * */
	private Mono<User> updateUserUsingSave(User update) {
		log.debug("updateUserUsingSave | update: {}",update);


		return userRepository.findById(update.getId())
				.flatMap(user -> {
					if (update.getPassword() != null && !update.getPassword().isEmpty()){
						user.setPassword(passwordEncoder.encode(update.getPassword()));
					}
					if (update.getNickName() != null && !update.getNickName().isEmpty()){
						user.setNickName(update.getNickName());
					}
					if (update.getActivated() != null){
						user.setActivated(update.getActivated());
					}

					log.debug("updateUserUsingSave | before save | user: {}", user);
					return userRepository.save(user)
							.map(savedUser -> {
								savedUser.setPassword(null);
								return savedUser;
							})
							.doOnNext(savedUser -> log.debug("updateUserUsingSave | after save | user : {}", savedUser));
				});

	}
}
