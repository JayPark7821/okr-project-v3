package kr.service.okr.acceptance.project.team;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okrcommon.common.exception.ErrorCode;

public class TeamMemberAcceptanceTestAssertions {

	static void 팀원_초대_가능여부_응답_검증(ExtractableResponse<Response> 응답, String 검증할_유저_이메일) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo(검증할_유저_이메일);
	}

	static void 팀원_초대_가능여부_실패_응답_검증_참여중인_프로젝트_X(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
	}

	static void 팀원_초대_가능여부_실패_응답_검증_프로젝트_리더_X(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}

	static void 팀원_초대_가능여부_실패_응답_검증_가입한_유저_X(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	static void 팀원_초대_가능여부_실패_응답_검증_이미_프로젝트에_등록된_유저(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	static void 팀원_초대_가능여부_실패_응답_검증_검증_요청자의_이메일(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	static void 팀원_초대_응답_검증(ExtractableResponse<Response> 응답, String 초대한_유저_이메일) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).isEqualTo(초대한_유저_이메일);
	}
}
