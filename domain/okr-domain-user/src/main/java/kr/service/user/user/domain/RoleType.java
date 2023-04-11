package kr.service.user.user.domain;

public enum RoleType {

	ADMIN("ADMIN"), USER("USER");

	private final String value;

	public String getValue() {
		return value;
	}

	RoleType(final String value) {
		this.value = value;
	}
}
