package kr.service.okr.common.logging;

import lombok.Getter;

@Getter
public enum MDCKey {
	TRX_ID("trxId"),
	TRX_TIME("trxTime"),

	;
	private String key;

	MDCKey(String key) {
		this.key = key;
	}
}
