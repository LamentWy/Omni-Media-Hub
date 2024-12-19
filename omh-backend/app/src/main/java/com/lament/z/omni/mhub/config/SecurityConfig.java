package com.lament.z.omni.mhub.config;

import com.lament.z.omni.mhub.config.omni.OmniMediaHubProperties;
import com.lament.z.omni.mhub.web.filter.OmniWebFilter;
import com.lament.z.omni.mhub.model.User;
import com.lament.z.omni.mhub.security.SecurityConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;


@Configuration
@EnableR2dbcAuditing
@EnableReactiveMethodSecurity
@SuppressWarnings("java:S1135")
public class SecurityConfig {

	private final OmniMediaHubProperties omniProperties;

	public SecurityConfig(OmniMediaHubProperties omniProperties) {
		this.omniProperties = omniProperties;
	}

	/**
	 * filter chain
	 * */
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		http.securityMatcher(new NegatedServerWebExchangeMatcher(new OrServerWebExchangeMatcher(pathMatchers("/assets/**", "/swagger-ui/**"))))
				.cors(withDefaults())
				// disable csrf, 1. we use jwt; 2. this application should living in a LAN
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				// 无效路径跳转 index.html
				.addFilterAfter(new OmniWebFilter(), SecurityWebFiltersOrder.HTTPS_REDIRECT)
				.headers(h -> h  // https://developer.mozilla.org/zh-CN/docs/Web/HTTP/CSP
								.contentSecurityPolicy(csp -> csp.policyDirectives(omniProperties.getSecurity().getCsp()))
								// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/X-Frame-Options
								// TODO X-Frame-Options 正在被废弃，考虑切换到 Content-Security-Policy: frame-ancestors 'none'，效果类似 X-Frame-Options: DENY
						        // 直接添加到 com.lament.z.omni.mhub.config.omni.OmniDefault.Security.defaultCSP 中即可。
								.frameOptions(frameOptionsSpec -> frameOptionsSpec.mode(Mode.DENY))
								// see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Referrer-Policy
								.referrerPolicy(referrer -> referrer.policy(STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
								// details see https://developer.mozilla.org/en-US/docs/Web/HTTP/Permissions_Policy
								.permissionsPolicy(pp -> pp.policy(omniProperties.getSecurity().getPp())
								)
				)
				.authorizeExchange(authEx ->
						authEx.pathMatchers("/").permitAll()
								.pathMatchers("/*.*").permitAll()
								.pathMatchers("/z/register").permitAll()
								.pathMatchers("/z/login").permitAll()
								.pathMatchers("/z/admin/**").hasAuthority(SecurityConstants.ADMIN)
								.pathMatchers("/z/**").authenticated()
								.pathMatchers("/services/**").authenticated()
								.pathMatchers("/v3/api-docs/**").hasAuthority(SecurityConstants.ADMIN)
								.pathMatchers("/management/health").permitAll()
								.pathMatchers("/management/health/**").permitAll()
								.pathMatchers("/management/info").permitAll()
								.pathMatchers("/management/prometheus").permitAll()
								.pathMatchers("/management/**").hasAuthority(SecurityConstants.ADMIN)

				)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
		return http.build();
	}
	/**
	 * impl for ReactiveAuditorAware
	 */
	@Bean
	public ReactiveAuditorAware<String> reactiveAuditorAware() {
		return () -> ReactiveSecurityContextHolder.getContext()
				.map(SecurityContext::getAuthentication)
				.filter(Authentication::isAuthenticated)
				.map(Authentication::getPrincipal)
				.map(User.class::cast)
				.map(User::getName);
 	}

	/**
	 * 密码加密器 PasswordEncoder
	 * */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * create a AuthenticationManager  &&
	 * set a passwordEncoder for this AuthenticationManager
	 */
	@Bean
	public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService){
		UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager =
				new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

		reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder());
		return reactiveAuthenticationManager;
	}

}
