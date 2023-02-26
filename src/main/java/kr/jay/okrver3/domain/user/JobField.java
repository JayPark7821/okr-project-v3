package kr.jay.okrver3.domain.user;

import java.util.Arrays;
import java.util.List;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JobField implements JobType {
	PLAN("기획", Arrays.asList(
		JobFieldDetail.UI_UX_PLANNER,
		JobFieldDetail.GAME_PLANNER,
		JobFieldDetail.PROJECT_MANAGER,
		JobFieldDetail.HARDWARE_PLANNER,
		JobFieldDetail.ETC_PLANNER
	)),
	DESIGN("디자인", Arrays.asList(
		JobFieldDetail.GRAPHIC_DESIGNER,
		JobFieldDetail.UI_UX_DESIGNER,
		JobFieldDetail.THREEDIMENTION_DESIGNER,
		JobFieldDetail.HARDWARE_DESIGNER,
		JobFieldDetail.ETC_DESIGNER
	)),
	FRONT_END("프론트엔드 개발", Arrays.asList(
		JobFieldDetail.IOS_DEVELOPER,
		JobFieldDetail.ANDROID_DEVELOPER,
		JobFieldDetail.WEB_FRONT_END_DEVELOPER,
		JobFieldDetail.WEB_PUBLISHER,
		JobFieldDetail.CROSS_PLATFORM_DEVELOPER
	)),
	BACK_END("백엔드 개발", Arrays.asList(
		JobFieldDetail.WEB_SERVER_DEVELOPER,
		JobFieldDetail.BLOCK_CHAIN_DEVELOPER,
		JobFieldDetail.AI_DEVELOPER,
		JobFieldDetail.GAME_SERVER
	)),
	BUSINESS("사업", Arrays.asList(
		JobFieldDetail.BUSINESS_PLANNING,
		JobFieldDetail.MARKETING,
		JobFieldDetail.FINANCE_ACCOUNTING,
		JobFieldDetail.SALES,
		JobFieldDetail.STRATEGY_CONSULTING,
		JobFieldDetail.INVESTMENT_ADVISOR,
		JobFieldDetail.ETC_BUSINESS
	)),
	ETC("기타", Arrays.asList(
		JobFieldDetail.WRITER_BLOGGER,
		JobFieldDetail.INFLUENCER_STREAMER,
		JobFieldDetail.LAW_LABOR,
		JobFieldDetail.MEDICAL_MEDICINE,
		JobFieldDetail.CATERING_CHEF,
		JobFieldDetail.PRODUCER_CP,
		JobFieldDetail.COMPOSING,
		JobFieldDetail.VIDEO,
		JobFieldDetail.OPERATE,
		JobFieldDetail.QA,
		JobFieldDetail.ETC
	));

	private String title;
	private List<JobFieldDetail> detailList;

	public List<JobFieldDetail> getDetailList() {
		return detailList;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getTitle() {
		return title;
	}

	public static JobField of(String code) {
		return Arrays.stream(JobField.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_JOB_FIELD));
	}
}
