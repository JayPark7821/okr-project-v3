package kr.service.okr.model.user;

import kr.service.okr.model.guset.ProviderType;

public record OAuth2UserInfo(String id, String name, String email, String picture, ProviderType providerType) {
}