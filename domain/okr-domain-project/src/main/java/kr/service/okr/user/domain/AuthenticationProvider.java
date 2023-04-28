package kr.service.okr.user.domain;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public final class AuthenticationProvider {

	private static final String secretKey = "secretKey-test-okr-project-jwt-token";
	private static final long refreshExpiredTimeMs = 604800000L;
	private static final long accessExpiredTimeMs = 1800000L;
	private static final long refreshTokenRegenerationThreshold = 259200000L;

	public static Optional<String> findEmailByToken(final String token) {
		try {
			return Optional.of(getEmail(token, secretKey));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static String generateAccessToken(final String email) {
		return generateJwtToken(email, false);
	}
	
	static boolean isTokenAboutToExpired(final String token) {
		try {
			final long remainingTimeOf =
				extractClaims(token, secretKey).getExpiration().getTime() - new Date().getTime();
			return remainingTimeOf <= refreshTokenRegenerationThreshold;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	static String generateRefreshToken(final String email) {
		return generateJwtToken(email, true);
	}

	private static String generateJwtToken(final String userEmail, boolean isRefreshToken) {
		Claims claims = Jwts.claims();
		claims.put("email", userEmail);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(
				new Date(System.currentTimeMillis() + (isRefreshToken ? refreshExpiredTimeMs : accessExpiredTimeMs)))
			.signWith(getKey(secretKey), SignatureAlgorithm.HS256)
			.compact();
	}

	private static String getEmail(String token, String key) {
		return extractClaims(token, key).get("email", String.class);
	}

	private static Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private static Claims extractClaims(String token, String key) {
		return Jwts.parserBuilder().setSigningKey(getKey(key))
			.build().parseClaimsJws(token).getBody();
	}

}
