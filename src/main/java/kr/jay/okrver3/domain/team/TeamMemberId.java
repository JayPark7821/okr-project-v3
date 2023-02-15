package kr.jay.okrver3.domain.team;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TeamMemberId implements Serializable {

	@EqualsAndHashCode.Include
	private Long user;
	@EqualsAndHashCode.Include
	private Long project;

}
