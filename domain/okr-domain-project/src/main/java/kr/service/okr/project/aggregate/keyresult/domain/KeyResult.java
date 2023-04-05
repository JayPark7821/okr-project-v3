package kr.service.okr.project.aggregate.keyresult.domain;

import java.util.ArrayList;
import java.util.List;

import kr.service.okr.project.aggregate.initiative.domain.Initiative;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KeyResult {

	private static final String KEYRESULT_TOKEN_PREFIX = "keyresult-";
	private Long id;
	private String keyResultToken;
	private Long projectId;
	private String name;
	private Integer keyResultIndex;
	private List<Initiative> initiative = new ArrayList<>();

	public KeyResult(
		final String name,
		final Long projectId,
		final Integer keyResultIndex,
		final List<Initiative> initiative
	) {
		this.keyResultToken = TokenGenerator.randomCharacterWithPrefix(KEYRESULT_TOKEN_PREFIX);
		this.projectId = projectId;
		this.name = name;
		this.keyResultIndex = keyResultIndex;
		this.initiative = initiative;
	}

	@Builder
	private KeyResult(
		final Long id,
		final String keyResultToken,
		final Long projectId,
		final String name,
		final Integer keyResultIndex,
		final List<Initiative> initiative
	) {
		this.id = id;
		this.keyResultToken = keyResultToken;
		this.projectId = projectId;
		this.name = name;
		this.keyResultIndex = keyResultIndex;
		this.initiative = initiative;
	}
}
