package kr.jay.okrver3.domain.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectRoleType {
	LEADER("Leader"),
	MEMBER("Memeber");

	private final String value;
}
