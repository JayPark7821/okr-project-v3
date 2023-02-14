package kr.jay.okrver3.domain.user;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
	APPLE("APPLE"),
	GOOGLE("GOOGLE");

	private String name;

	public static ProviderType of(String providerInString) {
		return Arrays.stream(ProviderType.values())
			.filter(provider -> provider.getName().equals(providerInString))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 타입입니다."));
	}

}
