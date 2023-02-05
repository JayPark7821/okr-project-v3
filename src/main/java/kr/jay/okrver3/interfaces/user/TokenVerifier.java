package kr.jay.okrver3.interfaces.user;

public interface TokenVerifier {
	OAuth2UserInfo verifyIdToken(String token);
}
