package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.project.aggregate.keyresult.KeyResult;

public record KeyResultInfo(String name, String keyResultToken, Integer keyResultIndex) {

	public KeyResultInfo(KeyResult keyResult) {
		this(keyResult.getName(), keyResult.getKeyResultToken(), keyResult.getKeyResultIndex());
	}
}
