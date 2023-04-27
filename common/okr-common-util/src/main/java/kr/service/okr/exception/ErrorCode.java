package kr.service.okr.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "서버에 오류가 발생했습니다."),
	MISS_MATCH_PROVIDER(HttpStatus.BAD_REQUEST, "%s (으)로 가입한 계정이 있습니다."),
	PROVIDER_TYPE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "소셜플랫폼 타입은 필수 값 입니다."),
	EMAIL_IS_REQUIRED(HttpStatus.BAD_REQUEST, "이메일이 누락되었습니다."),
	USERNAME_IS_REQUIRED(HttpStatus.BAD_REQUEST, "사용자명(20자 이하)은 필수 값 입니다."),
	ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ID값이 누락되었습니다."),
	JOB_FIELD_IS_REQUIRED(HttpStatus.BAD_REQUEST, "직무분야는 필수 값 입니다."),
	UNSUPPORTED_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 타입입니다."),
	OBJECTIVE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "목표는 필수 값 입니다."),
	OBJECTIVE_WRONG_INPUT_LENGTH(HttpStatus.BAD_REQUEST, "목표는 1자 이상 50자 이내로 입력해주세요."),
	KEYRESULT_NAME_WRONG_INPUT_LENGTH(HttpStatus.BAD_REQUEST, "핵심결과는 1자 이상 50자 이내로 입력해주세요."),
	INITIATIVE_NAME_WRONG_INPUT_LENGTH(HttpStatus.BAD_REQUEST, "행동전략은 1자 이상 50자 이내로 입력해주세요."),
	INITIATIVE_DETAIL_WRONG_INPUT_LENGTH(HttpStatus.BAD_REQUEST, "행동전략 1자 이상 상세는 50자 이내로 입력해주세요."),
	PROJECT_START_DATE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "프로젝트 시작일은 필수 값 입니다."),
	PROJECT_END_DATE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "프로젝트 종료일은 필수 값 입니다."),
	PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "프로젝트를 찾을 수 없습니다."),
	PROJECT_START_DATE_IS_AFTER_END_DATE(HttpStatus.BAD_REQUEST, "프로젝트 시작일은 종료일 이전이어야 합니다."),
	PROJECT_END_DATE_IS_BEFORE_TODAY(HttpStatus.BAD_REQUEST, "프로젝트 종료일은 오늘 이후여야 합니다."),
	NOT_AVAIL_INVITE_MYSELF(HttpStatus.BAD_REQUEST, "자신을 초대할 수 없습니다."),
	INVALID_PROJECT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 프로젝트 토큰입니다."),
	USER_IS_NOT_LEADER(HttpStatus.BAD_REQUEST, "해당 프로젝트의 리더만 진행 할 수 있습니다."),
	NOT_UNDER_PROJECT_DURATION(HttpStatus.BAD_REQUEST, "프로젝트 기간이 아닙니다."),
	USER_ALREADY_PROJECT_MEMBER(HttpStatus.BAD_REQUEST, "이미 프로젝트에 참여중인 사용자입니다."),
	PROJECT_IS_FINISHED(HttpStatus.BAD_REQUEST, "이미 종료된 프로젝트 입니다."),
	PROJECT_ALREADY_HAS_LEADER(HttpStatus.BAD_REQUEST, "이미 리더가 존재합니다."),
	MAX_KEYRESULT_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "핵심결과 등록 최대 개수를 초과했습니다."),
	INVALID_INITIATIVE_DATE(HttpStatus.BAD_REQUEST, "행동전략의 시작, 종료일은 목표 기간 안에서만 등록할 수 있습니다."),
	INVALID_KEYRESULT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 핵심결과 토큰입니다."),
	INITIATIVE_NAME_IS_REQUIRED(HttpStatus.BAD_REQUEST, "행동전략명은 필수 값 입니다."),
	INITIATIVE_DETAIL_IS_REQUIRED(HttpStatus.BAD_REQUEST, "행동전략 상세는 필수 값 입니다."),
	INITIATIVE_START_DATE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "행동전략 시작일은 필수 값 입니다."),
	INITIATIVE_END_DATE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "행동전략 종료일은 필수 값 입니다."),
	INITIATIVE_DATES_WILL_BE_OVER_PROJECT_DATES(HttpStatus.BAD_REQUEST, "프로젝트 기간이 행동전략 기간을 포함해야 합니다."),
	REQUIRED_DATE_VALUE(HttpStatus.BAD_REQUEST, "날짜는 필수 값 입니다."),
	INVALID_USER_EMAIL(HttpStatus.BAD_REQUEST, "등록된 사용자가 없습니다."),
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	INVALID_JOIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 가입 정보 입니다."),
	ALREADY_JOINED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	INVALID_JOB_CATEGORY(HttpStatus.BAD_REQUEST, "직업 카테고리 정보가 없습니다."),
	INVALID_JOB_DETAIL_FIELD(HttpStatus.BAD_REQUEST, "선택한 직업 정보가 정보가 없습니다."),
	;

	final String message;
	final HttpStatus httpStatus;

	ErrorCode(final HttpStatus httpStatus, final String message) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
