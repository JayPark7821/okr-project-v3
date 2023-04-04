package kr.service.model.user;

import kr.service.model.guset.ProviderType;

public record OAuth2UserInfo(String id, String name, String email, String picture, ProviderType providerType) {
}