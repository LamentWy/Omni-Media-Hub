package com.lament.z.omni.mhub.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import com.lament.z.omni.mhub.config.omni.OmniDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public final class SecurityUtils {


	private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

	private SecurityUtils() {}

	/**
	 * get UserName (aka login_name in database) from SecurityContext
	 *
	 * */
	public static Mono<String> getCurrentUserNameFromSecurityContext() {
		return ReactiveSecurityContextHolder.getContext()
				.map(SecurityContext::getAuthentication)
				.flatMap(auth -> Mono.justOrEmpty(auth.getName()));
	}

	private static String getPrincipalFromAuthentication(Authentication auth) {
		if (auth == null){
			return null;
		}
		else if (auth.getPrincipal() instanceof UserDetails springSecurityUser) {
			return springSecurityUser.getUsername();
		}
		else if (auth.getPrincipal() instanceof Jwt jwt) {
			return jwt.getSubject();
		}
		else if (auth.getPrincipal() instanceof String s) {
			return s;
		}
		return null;
	}


	public static String createToken(Authentication authentication,boolean rememberMe,
			long tokenValidity, long tokenValidity4remember, JwtEncoder jwtEncoder){

		log.debug("jwt | createToken | Starting token creation");
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

		Instant now = Instant.now();
		Instant validity;
		if(rememberMe){
			validity = now.plus(tokenValidity4remember, ChronoUnit.SECONDS);
		}else {
			validity = now.plus(tokenValidity,ChronoUnit.SECONDS);
		}

		JwtClaimsSet claimsSet = JwtClaimsSet.builder()
				.issuedAt(now)
				.expiresAt(validity)
				.subject(authentication.getName())
				.claim("auth",authorities)
				.build();

		JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
		log.debug(" jwt | createToken | authorities: {}",authorities);
		return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claimsSet)).getTokenValue();
	}

}
