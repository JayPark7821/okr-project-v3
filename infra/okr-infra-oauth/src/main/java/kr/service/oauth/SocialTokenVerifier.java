package kr.service.oauth;

public interface SocialTokenVerifier {

	boolean support(SocialPlatform socialPlatform);

	OAuth2UserInfo verifyIdToken(String token);
}
