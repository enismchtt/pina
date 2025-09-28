package com.bbm.backend.util;

import com.bbm.backend.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	private static final long EXPIRATION_TIME = 3600000;  // 1 hour
	MessageDigest digest = MessageDigest.getInstance("SHA-256");

	public JwtService() throws NoSuchAlgorithmException {
	}

	public String generateToken(User user) {

		byte[] keyBytes = digest.digest((secretKey+user.getUsername()).getBytes());
		SecretKey key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
		return Jwts.builder()
				.setSubject(user.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key)
				.compact();
	}
}
