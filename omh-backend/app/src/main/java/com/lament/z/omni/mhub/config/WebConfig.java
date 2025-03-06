package com.lament.z.omni.mhub.config;

import com.lament.z.omni.mhub.config.omni.OmniMediaHubProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class WebConfig {

	private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

	private final OmniMediaHubProperties omniProperties;

	public WebConfig(OmniMediaHubProperties omniProperties) {
		this.omniProperties = omniProperties;
	}

	@Bean
	public CorsConfigurationSource reactiveCorsConfig(){

		UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfig = omniProperties.getCors();

		if (!CollectionUtils.isEmpty(corsConfig.getAllowedOrigins()) ||
		!CollectionUtils.isEmpty(corsConfig.getAllowedOriginPatterns())){
			log.debug("Setting CORS Rules");
			corsConfigSource.registerCorsConfiguration("/z/**", corsConfig);
//			corsConfigSource.registerCorsConfiguration("/v3/api-docs", corsConfig);
//			corsConfigSource.registerCorsConfiguration("/swagger-ui/**", corsConfig);
		}
		return corsConfigSource;
	}


//	@Bean
//	@Order(-99)
//	public WebExceptionHandler customExceptionHandler(ObjectMapper mapper, ExceptionTranslator customTranslator){
//
//		return new CustomReactiveWebExceptionHandler(customTranslator,mapper);
//	}
}
