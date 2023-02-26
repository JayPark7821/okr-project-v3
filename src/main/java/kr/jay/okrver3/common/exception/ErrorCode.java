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
	;

	private HttpStatus status;
	private String message;

}
