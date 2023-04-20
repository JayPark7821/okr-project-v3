package kr.service.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kr.service.okr.user.auth.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenRepository implements AuthenticationRepository {

	@Value("${app.auth.tokenSecret}")
	private String secretKey;
	@Value("${app.auth.refreshTokenExpiry}")
	private Long refreshExpiredTimeMs;
	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;
	@Value("${app.auth.refreshTokenRegenerationThreshold}")
	private Long refreshTokenRegenerationThreshold;

	@Override
	public Optional<String> findEmailByToken(final String token) {
		try {
			return Optional.of(getEmail(token, secretKey));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public boolean isTokenExpired(final String token) {
		try {
			final long remainingTimeOf =
				extractClaims(token, secretKey).getExpiration().getTime() - new Date().getTime();
			return remainingTimeOf <= refreshTokenRegenerationThreshold;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	@Override
	public String generateRefreshToken(final String email) {
		return generateJwtToken(email, true);
	}

	@Override
	public String generateAccessToken(final String email) {
		return generateJwtToken(email, false);
	}

	private String generateJwtToken(final String userEmail, boolean isRefreshToken) {
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

	public String getEmail(String token, String key) {
		return extractClaims(token, key).get("email", String.class);
	}

	Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	Claims extractClaims(String token, String key) {
		return Jwts.parserBuilder().setSigningKey(getKey(key))
			.build().parseClaimsJws(token).getBody();
	}

}
