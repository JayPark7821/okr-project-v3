package kr.service.okr.common.mdc;

import lombok.Getter;

@Getter
public enum MDCKey {
	TRX_ID("trxId");

	private String key;

	MDCKey(String key) {
		this.key = key;
	}
}
