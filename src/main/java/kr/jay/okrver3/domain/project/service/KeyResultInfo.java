package kr.jay.okrver3.domain.project.service;

import kr.jay.okrver3.domain.keyresult.KeyResult;

public record KeyResultInfo(String name, Integer keyResultIndex) {

	public KeyResultInfo(KeyResult keyResult) {
		this(keyResult.getName(), keyResult.getKeyResultIndex());
	}
}
