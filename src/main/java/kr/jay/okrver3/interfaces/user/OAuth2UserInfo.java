package kr.jay.okrver3.interfaces.user;

import kr.jay.okrver3.domain.user.ProviderType;

public record OAuth2UserInfo(String id, String name, String email, String picture, ProviderType providerType) {
}