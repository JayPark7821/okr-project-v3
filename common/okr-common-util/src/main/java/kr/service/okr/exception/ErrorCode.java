package kr.service.okr.exception;

public enum ErrorCode {

	OBJECTIVE_IS_REQUIRED("목표는 필수 값 입니다."),
	OBJECTIVE_WRONG_INPUT_LENGTH("목표는 1자 이상 50자 이내로 입력해주세요."),
	KEYRESULT_NAME_WRONG_INPUT_LENGTH("핵심결과는 1자 이상 50자 이내로 입력해주세요."),
	INITIATIVE_NAME_WRONG_INPUT_LENGTH("행동전략은 1자 이상 50자 이내로 입력해주세요."),
	INITIATIVE_DETAIL_WRONG_INPUT_LENGTH("행동전략 1자 이상 상세는 50자 이내로 입력해주세요."),
	PROJECT_START_DATE_IS_REQUIRED("프로젝트 시작일은 필수 값 입니다."),
	PROJECT_END_DATE_IS_REQUIRED("프로젝트 종료일은 필수 값 입니다."),
	LEADER_IS_IN_TEAM_MEMBER("팀원에 리더가 포함되어 있습니다."),
	PROJECT_NOT_FOUND("프로젝트를 찾을 수 없습니다."),
	REQUIRED_DATE_VALUE("날짜는 필수 값 입니다."),
	INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
	PROJECT_START_DATE_IS_AFTER_END_DATE("프로젝트 시작일은 종료일 이전이어야 합니다."),
	PROJECT_END_DATE_IS_BEFORE_TODAY("프로젝트 종료일은 오늘 이후여야 합니다."),
	NOT_AVAIL_INVITE_MYSELF("자신을 초대할 수 없습니다."),
	INVALID_PROJECT_TOKEN("유효하지 않은 프로젝트 토큰입니다."),
	USER_IS_NOT_LEADER("해당 프로젝트의 리더만 진행 할 수 있습니다."),
	NOT_UNDER_PROJECT_DURATION("프로젝트 기간이 아닙니다."),

	USER_ALREADY_PROJECT_MEMBER("이미 프로젝트에 참여중인 사용자입니다."),

	PROJECT_IS_FINISHED("이미 종료된 프로젝트 입니다."),
	PROJECT_ALREADY_HAS_LEADER("이미 리더가 존재합니다."),
	MAX_KEYRESULT_COUNT_EXCEEDED("핵심결과 등록 최대 개수를 초과했습니다."),

	INVALID_INITIATIVE_DATE("행동전략의 시작, 종료일은 목표 기간 안에서만 등록할 수 있습니다."),
	INVALID_KEYRESULT_TOKEN("유효하지 않은 핵심결과 토큰입니다."),
	INITIATIVE_NAME_IS_REQUIRED("행동전략명은 필수 값 입니다."),

	INITIATIVE_DETAIL_IS_REQUIRED("행동전략 상세는 필수 값 입니다."),
	INITIATIVE_START_DATE_IS_REQUIRED("행동전략 시작일은 필수 값 입니다."),
	INITIATIVE_END_DATE_IS_REQUIRED("행동전략 종료일은 필수 값 입니다."),
	INITIATIVE_DATES_WILL_BE_OVER_PROJECT_DATES("프로젝트 기간이 행동전략 기간을 포함해야 합니다."),
	MISS_MATCH_PROVIDER("{} (으)로 가입한 계정이 있습니다."),
	PROVIDER_TYPE_IS_REQUIRED("소셜플랫폼 타입은 필수 값 입니다."),
	EMAIL_IS_REQUIRED("이메일이 누락되었습니다."),
	USERNAME_IS_REQUIRED("사용자명(20자 이하)은 필수 값 입니다."),
	ID_IS_REQUIRED("ID값이 누락되었습니다."),
	JOB_FIELD_IS_REQUIRED("직무분야는 필수 값 입니다."),
	UNSUPPORTED_SOCIAL_TYPE("지원하지 않는 소셜 타입입니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN("만료된 토큰입니다.");

	final String message;

	ErrorCode(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
