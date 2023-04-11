package kr.service.oauth.apple;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kr.service.oauth.OAuth2UserInfo;
import kr.service.oauth.SocialPlatform;
import kr.service.oauth.SocialTokenVerifier;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleSocialTokenVerifier implements SocialTokenVerifier {

	private final AppleClient appleClient;

	@Override
	public boolean support(SocialPlatform socialPlatform) {
		return SocialPlatform.APPLE == socialPlatform;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {
		Map<String, String> header = getHeader(token.substring(0, token.indexOf(".")));

		ApplePublicKeyResponse.Key key = appleClient.getAppleAuthPublicKey()
			.getMatchedKeyBy(header.get("kid"), header.get("alg"))
			.orElseThrow(() -> new RuntimeException("Unable to find public key for token"));

		Claims body = getBodyFromToken(token, key);
		String uuid = UUID.randomUUID().toString();

		return new OAuth2UserInfo(
			uuid,
			"일잘러",
			body.get("email", String.class),
			null,
			SocialPlatform.APPLE.name()
		);
	}

	private static Claims getBodyFromToken(String token, ApplePublicKeyResponse.Key key) {
		try {
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
				new BigInteger(1, Base64.getUrlDecoder().decode(key.getN())),
				new BigInteger(1, Base64.getUrlDecoder().decode(key.getE())));

			PublicKey publicKey = KeyFactory.getInstance(key.getKty()).generatePublic(publicKeySpec);

			return Jwts.parserBuilder()
				.setSigningKey(publicKey).build()
				.parseClaimsJws(token)
				.getBody();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new RuntimeException("Unable to generate public key from RSA key spec", ex);
		}
	}

	private static Map<String, String> getHeader(String headerOfIdentityToken) {
		try {
			return new ObjectMapper().readValue(
				new String(Base64.getDecoder().decode(headerOfIdentityToken), StandardCharsets.UTF_8), Map.class);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse header of identity token", e);
		}
	}

}
