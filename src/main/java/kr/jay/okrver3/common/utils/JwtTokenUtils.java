package kr.jay.okrver3.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenUtils {

	public static String getEmail(String token, String key) {
		return extractClaims(token, key).get("email", String.class);
	}

	public static boolean isExpired(String token, String key) {
		Date expiration = extractClaims(token, key).getExpiration();
		return expiration.before(new Date());
	}

	private static Claims extractClaims(String token, String key) {
		return Jwts.parserBuilder().setSigningKey(getKey(key))
			.build().parseClaimsJws(token).getBody();
	}

	public static Date getExpiration(String token, String key) {
		return extractClaims(token, key).getExpiration();
	}

	public static String generateToken(String email, String key, Long expiredTimMs) {
		Claims claims = Jwts.claims();
		claims.put("email", email);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredTimMs))
			.signWith(getKey(key), SignatureAlgorithm.HS256)
			.compact();
	}

	private static Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
