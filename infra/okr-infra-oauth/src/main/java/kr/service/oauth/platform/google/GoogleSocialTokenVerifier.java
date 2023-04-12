package kr.service.oauth.platform.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.platform.SocialPlatform;
import kr.service.oauth.processor.SocialTokenVerifier;

@Component
public class GoogleSocialTokenVerifier implements SocialTokenVerifier {

	@Value("${google.clientId}")
	private String clientId;
	private final NetHttpTransport transport = new NetHttpTransport();
	private final JsonFactory jsonFactory = new GsonFactory();

	@Override
	public boolean support(SocialPlatform socialPlatform) {
		return SocialPlatform.GOOGLE == socialPlatform;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
			.setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
			.setAudience(Collections.singletonList(clientId))
			.build();

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			return new OAuth2UserInfo(
				payload.getSubject(),
				(String)payload.get("username"),
				(String)payload.get("email"),
				(String)payload.get("profileImage"),
				SocialPlatform.GOOGLE.name()
			);
		} else {
			throw new IllegalArgumentException("Invalid ID token");
		}
	}
}
