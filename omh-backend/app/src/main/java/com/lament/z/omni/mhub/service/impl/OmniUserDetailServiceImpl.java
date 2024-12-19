package com.lament.z.omni.mhub.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.lament.z.omni.mhub.model.Role;
import com.lament.z.omni.mhub.model.User;
import com.lament.z.omni.mhub.repository.UserRoleRepository;
import com.lament.z.omni.mhub.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * implementation for ReactiveUserDetailsService
 * */
@Service("userDetailsService")
public class OmniUserDetailServiceImpl implements ReactiveUserDetailsService {


	private static final Logger log = LoggerFactory.getLogger(OmniUserDetailServiceImpl.class);
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;

	public OmniUserDetailServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		username = username.toLowerCase();
		log.info("OmniUserDetailServiceImpl | findByUsername | username = {}", username);

		return userRepository.findOneWithActivatedByName(username)
				.switchIfEmpty(Mono.error(new UsernameNotFoundException("User: ["+ username +"] Not Found.")))
				.flatMap(user -> userRoleRepository
						.findByUserId(user.getId())
						.collect(Collectors.toSet())
						.flatMap(userRoles -> Flux.fromIterable(userRoles)
								.all( userRole -> {
									log.debug("OmniUserDetailServiceImpl | findByUsername | before user = {}", user);

									user.getRoles().add(new Role(userRole.getRoleName()));
									log.debug("OmniUserDetailServiceImpl | findByUsername | after user = {}", user);
									return true;
								})
						).then(Mono.just(user))
				)
				.map(this::createSpringSecurityUser);

	}

	private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
		List<SimpleGrantedAuthority> grantedAuths = user.getRoles()
				.stream()
				.map(Role::getName)
				.map(SimpleGrantedAuthority::new)
				.toList();
		log.debug("OmniUserDetailServiceImpl | createSpringSecurityUser | before | user: {} | grantedAuths: {}", user, grantedAuths);
		org.springframework.security.core.userdetails.User ssUser =
				new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(),grantedAuths);

		log.debug("OmniUserDetailServiceImpl | createSpringSecurityUser | after | SpringSecurityUser: {}", ssUser);
		return ssUser;
	}
}
