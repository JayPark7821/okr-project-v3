package kr.jay.okrver3.domain.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectType {

	TEAM("Team"),
	SINGLE("Single"),
	ALL("All"),
	;

	private final String code;

}
