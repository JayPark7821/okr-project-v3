package kr.jay.okrver3.domain.user;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JobFieldDetail implements JobType {
	UI_UX_PLANNER("UI/UX 기획자", "PLAN"),
	GAME_PLANNER("게임 기획자", "PLAN"),
	PROJECT_MANAGER("프로젝트 메니저", "PLAN"),
	HARDWARE_PLANNER("하드웨어(제품) 기획", "PLAN"),
	ETC_PLANNER("(기획) 기타", "PLAN"),

	GRAPHIC_DESIGNER("그래픽 디자이너", "DESIGN"),
	UI_UX_DESIGNER("UI/UX 디자이너", "DESIGN"),
	THREEDIMENTION_DESIGNER("3D 디자이너", "DESIGN"),
	HARDWARE_DESIGNER("하드웨어(제품) 디자이너", "DESIGN"),
	ETC_DESIGNER("(디자인) 기타", "DESIGN"),

	IOS_DEVELOPER("IOS 개발자", "FRONT_END"),
	ANDROID_DEVELOPER("Android 개발자", "FRONT_END"),
	WEB_FRONT_END_DEVELOPER("웹 프론트엔드 개발자", "FRONT_END"),
	WEB_PUBLISHER("웹 퍼블리셔", "FRONT_END"),
	CROSS_PLATFORM_DEVELOPER("크로스 플랫폼 개발자", "FRONT_END"),

	WEB_SERVER_DEVELOPER("서버 개발자", "BACK_END"),
	BLOCK_CHAIN_DEVELOPER("블록체인 개발자", "BACK_END"),
	AI_DEVELOPER("AI 개발자", "BACK_END"),

	DB_BIG_DATA_DS("DB/빅데이터/DS", "BACK_END"),
	GAME_SERVER("게임 서버 개발자", "BACK_END"),

	BUSINESS_PLANNING("사업 기획", "BUSINESS"),
	MARKETING("마케팅", "BUSINESS"),
	FINANCE_ACCOUNTING("재무/회계", "BUSINESS"),
	SALES("영업", "BUSINESS"),
	STRATEGY_CONSULTING("전략/컨설팅", "BUSINESS"),
	INVESTMENT_ADVISOR("투자/고문", "BUSINESS"),
	ETC_BUSINESS("사업/기타", "BUSINESS"),

	WRITER_BLOGGER("작가/블로거", "ETC"),
	INFLUENCER_STREAMER("인플루언서/스트리머", "ETC"),
	LAW_LABOR("법률/노무", "ETC"),
	MEDICAL_MEDICINE("의료/의학", "ETC"),
	CATERING_CHEF("요식업/쉐프", "ETC"),
	PRODUCER_CP("프로듀서/CP", "ETC"),
	COMPOSING("작곡(사운드)", "ETC"),
	VIDEO("영상", "ETC"),
	OPERATE("운영", "ETC"),
	QA("QA", "ETC"),
	ETC("기타", "ETC");

	private String title;
	private String jobField;

	public String getJobField() {
		return jobField;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getTitle() {
		return title;
	}

	public static JobFieldDetail lookup(String id) {
		return lookup(JobFieldDetail.class, id);
	}

	private static <E extends Enum<E>> E lookup(Class<E> e, String id) {
		E result;
		try {
			result = Enum.valueOf(e, id);
		} catch (Exception exception) {
			throw new OkrApplicationException(ErrorCode.INVALID_JOB_DETAIL_FIELD);
		}
		return result;
	}
}
