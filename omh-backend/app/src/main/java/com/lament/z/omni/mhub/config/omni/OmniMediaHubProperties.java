package com.lament.z.omni.mhub.config.omni;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(prefix = "omni-media-hub", ignoreUnknownFields = false)
public class OmniMediaHubProperties {
	private final CorsConfiguration cors = new CorsConfiguration();
	private final Security security = new Security();


	public OmniMediaHubProperties() {
	}

	public CorsConfiguration getCors() {
		return cors;
	}

	public Security getSecurity() {
		return security;
	}

	public static class Security{
		// contentSecurityPolicy
		private String csp = OmniDefault.Security.defaultCSP;
		// PermissionsPolicy
		private String pp = OmniDefault.Security.defaultPermissionsPolicy;
		private final Auth auth = new Auth();

		public String getPp() {
			return pp;
		}

		public Auth getAuth() {
			return auth;
		}

		public String getCsp() {
			return csp;
		}

		public void setCsp(String csp) {
			this.csp = csp;
		}

		private static class Auth {
			private final Jwt jwt = new Jwt();

			public Jwt getJwt() {
				return jwt;
			}

			private static class Jwt {
				private String secret = OmniDefault.Security.Auth.Jwt.secret;
				private String base64Secret = OmniDefault.Security.Auth.Jwt.base64Secret;
				private long validityInSeconds = OmniDefault.Security.Auth.Jwt.validityInSeconds;
				private long rememberMeValidityInSeconds = OmniDefault.Security.Auth.Jwt.rememberMeValidityInSeconds;

				public String getSecret() {
					return secret;
				}

				public void setSecret(String secret) {
					this.secret = secret;
				}

				public String getBase64Secret() {
					return base64Secret;
				}

				public void setBase64Secret(String base64Secret) {
					this.base64Secret = base64Secret;
				}

				public long getValidityInSeconds() {
					return validityInSeconds;
				}

				public void setValidityInSeconds(long validityInSeconds) {
					this.validityInSeconds = validityInSeconds;
				}

				public long getRememberMeValidityInSeconds() {
					return rememberMeValidityInSeconds;
				}

				public void setRememberMeValidityInSeconds(long rememberMeValidityInSeconds) {
					this.rememberMeValidityInSeconds = rememberMeValidityInSeconds;
				}
			}
		}
	}

}
