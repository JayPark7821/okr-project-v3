package kr.jay.okrver3.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

	ADMIN("ADMIN");

	private final String value;
}
