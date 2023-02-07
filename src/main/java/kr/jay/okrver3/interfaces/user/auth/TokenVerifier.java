package kr.jay.okrver3.interfaces.user.auth;

public interface TokenVerifier {

	OAuth2UserInfo verifyIdToken(String token);
}
