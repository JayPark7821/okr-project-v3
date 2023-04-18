package kr.service.user.exception;

public enum ErrorCode {
	INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
	MISS_MATCH_PROVIDER("{} (으)로 가입한 계정이 있습니다."),
	PROVIDER_TYPE_IS_REQUIRED("소셜플랫폼 타입은 필수 값 입니다."),
	EMAIL_IS_REQUIRED("이메일이 누락되었습니다."),
	USERNAME_IS_REQUIRED("사용자명(20자 이하)은 필수 값 입니다."),
	ID_IS_REQUIRED("ID값이 누락되었습니다."),
	JOB_FIELD_IS_REQUIRED("직무분야는 필수 값 입니다."),
	UNSUPPORTED_SOCIAL_TYPE("지원하지 않는 소셜 타입입니다."),
	;

	final String message;

	ErrorCode(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
