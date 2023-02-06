package kr.jay.okrver3.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
	APPLE("Apple"), GOOGLE("Google");

	private String name;
}
