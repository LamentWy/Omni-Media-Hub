package com.lament.z.omni.mhub.config.omni;


public interface OmniDefault {

	interface Security{
		String defaultCSP =
				"default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";
		String defaultPermissionsPolicy =
				"camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()";

		interface Auth {
			interface Jwt{
				String secret = null;
				String base64Secret = null;
				long validityInSeconds = 1800L; // 30 minutes
				// 30 * 24 * 60 * 60L = 30 days ~ 1 month
				long rememberMeValidityInSeconds = 15552000L; // 180 days ~ half year
			}
		}
	}
}
