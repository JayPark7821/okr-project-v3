package kr.jay.okrver3.domain.guset.service;

import kr.jay.okrver3.domain.user.ProviderType;

public record GuestInfo(String guestUuid, String email, String name, ProviderType providerType, String profileImageUrl) {
}
