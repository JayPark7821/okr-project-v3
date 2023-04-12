package kr.service.oauth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kr.service.user.token.repository.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

	@Value("${app.auth.tokenSecret}")
	private String secretKey;
	@Value("${app.auth.refreshTokenExpiry}")
	private Long refreshExpiredTimeMs;
	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;
	@Value("${app.auth.refreshTokenRegenerationThreshold}")
	private Long refreshTokenRegenerationThreshold;

	@Override
	public String generateJwtToken(final String userEmail, boolean isRefreshToken) {
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

	@Override
	public boolean needNewRefreshToken(final String token) {
		try {
			final long remainingTimeOf =
				extractClaims(token, secretKey).getExpiration().getTime() - new Date().getTime();
			return remainingTimeOf <= refreshTokenRegenerationThreshold;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	private Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractClaims(String token, String key) {
		return Jwts.parserBuilder().setSigningKey(getKey(key))
			.build().parseClaimsJws(token).getBody();
	}
}
