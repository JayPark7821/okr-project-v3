package kr.jay.okrver3.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
	INVALID_JOIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 가입 정보 입니다."),
	ALREADY_JOINED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	MISS_MATCH_PROVIDER(HttpStatus.BAD_REQUEST, "소셜 provider 불일치"),
	INVALID_JOB_DETAIL_FIELD(HttpStatus.BAD_REQUEST, "선택한 직업 정보가 정보가 없습니다."),
	INVALID_JOB_FIELD(HttpStatus.BAD_REQUEST, "직업 카테고리 정보가 없습니다."),
	NOT_AVAIL_INVITE_MYSELF(HttpStatus.BAD_REQUEST, "자기 자신은 초대할 수 없습니다."),
	INVALID_USER_EMAIL(HttpStatus.BAD_REQUEST, "등록된 사용자가 없습니다."),
	USER_ALREADY_PROJECT_MEMBER(HttpStatus.BAD_REQUEST, "이미 해당 프로젝트 팀원 입니다."),
	USER_IS_NOT_LEADER(HttpStatus.BAD_REQUEST, "해당 프로젝트의 리더만 팀원을 초대할 수 있습니다."),
	INVALID_PROJECT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 토큰 입니다."),
	CASTING_USER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Casting to User failed"),
	PROJECT_LEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "프로젝트의 리더가 없습니다."),
	REQUIRED_DATE_VALUE(HttpStatus.BAD_REQUEST, "날짜는 필수 값 입니다."),
	INVALID_FINISHED_PROJECT_YN(HttpStatus.BAD_REQUEST, "종료된 프로젝트 포함여부는 Y 또는 N 만 가능합니다. "),
	INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 정렬 타입 입니다."),
	INVALID_PROJECT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 타입입니다."),
	INVALID_USER_INFO(HttpStatus.BAD_REQUEST, "잘못된 사용자 정보 입니다."),
	KEYRESULT_LIMIT_EXCEED(HttpStatus.BAD_REQUEST, "핵심 결과는 최대 3개까지 등록 가능합니다."),
	PROJECT_FINISHED(HttpStatus.BAD_REQUEST, "종료된 프로젝트 입니다."),
	;

	private HttpStatus status;
	private String message;

}
