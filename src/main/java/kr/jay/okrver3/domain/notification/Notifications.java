package kr.jay.okrver3.domain.notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Notifications {

	NEW_TEAM_MATE("<%s>님이 <%s> 팀원이 되셨습니다!\uD83D\uDC4F\uD83D\uDC4F"),
	INITIATIVE_ACHIEVED("<%s>님의 %s(이)가 완료되었습니다!\uD83D\uDC4F 피드백을 남겨볼까요?"),
	PROJECT_PROGRESS_ACHIEVED_QUARTER("<%s> 프로젝트가 25%% 진행되었어요.\uD83D\uDE09 한걸음씩 계속해보세요!"),
	PROJECT_PROGRESS_ACHIEVED_HALF("<%s> 프로젝트가 50%% 진행되었어요.\uD83D\uDE0E 벌써 절반이나 왔어요!"),
	PROJECT_PROGRESS_ACHIEVED_THREE_QUARTERS("<%s> 프로젝트가 75%% 진행되었어요.\uD83D\uDE00 조금만 더 파이팅!"),
	PROJECT_FINISHED("<%s> 프로젝트가 완료되었습니다.\uD83D\uDC4F\uD83D\uDC4F 다같이 고생한 팀원에게 수고했다 한마디!"),
	NEW_FEEDBACK("<%s>님이 내 <%s>에 피드백을 남기셨어요!"),
	PROJECT_TYPE_CHANGE("팀원이 초대되어 프로젝트 타입이 팀프로젝트로 변경되었습니다."),
	;

	private String msg;

	public String getMsg(String... param) {
		return String.format(msg, param);
	}
}
