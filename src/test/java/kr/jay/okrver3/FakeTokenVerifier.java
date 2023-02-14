package kr.jay.okrver3;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.interfaces.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.auth.TokenVerifier;

public class FakeTokenVerifier implements TokenVerifier {

	public static final String id = "fakeId";
	public static final String name = "fakeName";
	public static final String email = "fakeEmail";
	public static final String pic = "fakePic";
	public static final ProviderType provider = ProviderType.APPLE;

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {
		if (token.equals("userToken")) {
			return new OAuth2UserInfo(id, name, email, pic, provider);
		}
		return new OAuth2UserInfo(id, name, email, pic, provider);
	}
}
