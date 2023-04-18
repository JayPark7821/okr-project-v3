package kr.service.okr.project.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Initiative {

	private static final String INITIATIVE_TOKEN_PREFIX = "initiative-";
	private Long id;
	private String initiativeToken;
	private Long keyResultId;
	private TeamMember teamMember;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private String detail;
	private boolean done = Boolean.FALSE;
	private List<Feedback> feedback = new ArrayList<>();

	protected Initiative(
		final Long keyResultId,
		final TeamMember teamMember,
		final String name,
		final LocalDate startDate,
		final LocalDate endDate,
		final String detail
	) {
		this.initiativeToken = TokenGenerator.randomCharacterWithPrefix(INITIATIVE_TOKEN_PREFIX);
		this.keyResultId = keyResultId;
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
		final Long keyResultId,
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
		this.keyResultId = keyResultId;
		this.teamMember = teamMember;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
		this.done = done;
		this.feedback = feedback;
	}
}

