package kr.service.model.project.team;

public enum ProjectRoleType {
	LEADER("Leader"),
	MEMBER("Memeber");

	private final String value;

	public String getValue() {
		return value;
	}

	ProjectRoleType(final String value) {
		this.value = value;
	}
}
