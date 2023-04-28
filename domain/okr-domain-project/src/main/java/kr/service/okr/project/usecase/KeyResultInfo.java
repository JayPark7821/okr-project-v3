package kr.service.okr.project.usecase;

import kr.service.okr.project.domain.KeyResult;

public record KeyResultInfo(String name, String keyResultToken, Integer keyResultIndex) {

	public KeyResultInfo(KeyResult keyResult) {
		this(keyResult.getName(), keyResult.getKeyResultToken(), keyResult.getKeyResultIndex());
	}
}
