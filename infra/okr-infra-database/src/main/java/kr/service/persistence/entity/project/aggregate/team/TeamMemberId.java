package kr.service.persistence.entity.project.aggregate.team;

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
	private Long userSeq;
	@EqualsAndHashCode.Include
	private Long project;

}
