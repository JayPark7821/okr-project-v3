package kr.service.oauth.google;

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

import kr.service.oauth.OAuth2UserInfo;
import kr.service.oauth.SocialPlatform;
import kr.service.oauth.SocialTokenVerifier;

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
				(String)payload.get("name"),
				(String)payload.get("email"),
				(String)payload.get("picture"),
				SocialPlatform.GOOGLE.name()
			);
		} else {
			throw new IllegalArgumentException("Invalid ID token");
		}
	}
}
