package kr.service.okr.domain.project.info;

import kr.service.okr.domain.project.aggregate.keyresult.KeyResult;

public record KeyResultInfo(String name, String keyResultToken, Integer keyResultIndex) {

	public KeyResultInfo(KeyResult keyResult) {
		this(keyResult.getName(), keyResult.getKeyResultToken(), keyResult.getKeyResultIndex());
	}
}
