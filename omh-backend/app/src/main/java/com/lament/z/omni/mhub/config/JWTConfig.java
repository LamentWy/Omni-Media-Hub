package com.lament.z.omni.mhub.config;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JWTConfig {

	private static final Logger log = LoggerFactory.getLogger(JWTConfig.class);

	@Value("${omni-media-hub.security.auth.jwt.base64-secret}")
	private String base64Secret;

	private SecretKey getSecretKey(){
		byte[] bytes = Base64.from(base64Secret).decode();
		return new SecretKeySpec(bytes, MacAlgorithm.HS512.getName());
	}

	@Bean
	public ReactiveJwtDecoder jwtDecoder(){
		NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder
				.withSecretKey(getSecretKey())
				.macAlgorithm(MacAlgorithm.HS512)
				.build();

		return token -> {
			try {
				return jwtDecoder
						.decode(token)
						.doOnError(e -> {
							if (e.getMessage().contains("Jwt expired at")){
								// 可以在这里统计发生“过期”的次数，并持久化，而不是打印日志
								log.info("Jwt error: {}",e.getMessage());
							}
							else if (e.getMessage().contains("Failed to validate the token")) {
								// 同理
								log.info("Jwt error: {}",e.getMessage());
							}
							else if (e.getMessage().contains("Invalid JWT serialization:") || e.getMessage()
									.contains("Invalid unsecured/JWS/JWE header:")) {
								// 同理
								log.info("Jwt error: {}",e.getMessage());
							}else {
								log.error("Un track JWT reactive error: {}",e.getMessage());
							}
						});
			}catch (Exception e){
				log.error("Un track JWT exception: {}",e.getMessage());
				throw e;
			}
		  };
		}

		@Bean
		public JwtEncoder jwtEncoder(){
			return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
		}

}

