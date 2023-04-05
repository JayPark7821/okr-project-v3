package kr.service.okr.exception;

public enum ErrorCode {

	OBJECTIVE_IS_REQUIRED("목표는 필수 값 입니다."),
	OBJECTIVE_IS_TOO_LONG("목표는 50자 이내로 입력해주세요."),
	PROJECT_START_DATE_IS_REQUIRED("프로젝트 시작일은 필수 값 입니다."),
	PROJECT_END_DATE_IS_REQUIRED("프로젝트 종료일은 필수 값 입니다."),

	LEADER_IS_IN_TEAM_MEMBER("팀원에 리더가 포함되어 있습니다."),
	PROJECT_NOT_FOUND("프로젝트를 찾을 수 없습니다.");
	String message;

	ErrorCode(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
