package kr.service.okr.initiative.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.service.okr.feedback.domain.Feedback;
import kr.service.okr.keyresult.domain.KeyResult;
import kr.service.okr.team.domain.TeamMember;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Initiative {

	private static final String INITIATIVE_TOKEN_PREFIX = "initiative-";
	private Long id;
	private String initiativeToken;
	private KeyResult keyResult;
	private TeamMember teamMember;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private String detail;
	private boolean done = Boolean.FALSE;
	private List<Feedback> feedback = new ArrayList<>();

	public Initiative(
		final KeyResult keyResult,
		final TeamMember teamMember,
		final String name,
		final LocalDate startDate,
		final LocalDate endDate,
		final String detail
	) {
		this.initiativeToken = TokenGenerator.randomCharacterWithPrefix(INITIATIVE_TOKEN_PREFIX);
		this.keyResult = keyResult;
		this.teamMember = teamMember;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
	}

	@Builder
	private Initiative(
		final Long id,
		final String initiativeToken,
		final KeyResult keyResult,
		final TeamMember teamMember,
		final String name,
		final LocalDate startDate,
		final LocalDate endDate,
		final String detail,
		final boolean done,
		final List<Feedback> feedback
	) {
		this.id = id;
		this.initiativeToken = initiativeToken;
		this.keyResult = keyResult;
		this.teamMember = teamMember;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
		this.done = done;
		this.feedback = feedback;
	}
}

