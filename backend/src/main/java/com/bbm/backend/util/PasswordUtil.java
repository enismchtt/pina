//package com.bbm.backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//}

package com.bbm.backend.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	public static String encode(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	public static boolean matches(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}
