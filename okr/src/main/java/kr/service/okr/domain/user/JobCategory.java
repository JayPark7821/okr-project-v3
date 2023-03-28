package kr.service.okr.domain.user;

import java.util.Arrays;
import java.util.List;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.utils.EnumLookUpUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JobCategory implements JobType {
	PLAN("기획", Arrays.asList(
		JobField.UI_UX_PLANNER,
		JobField.GAME_PLANNER,
		JobField.PROJECT_MANAGER,
		JobField.HARDWARE_PLANNER,
		JobField.ETC_PLANNER
	)),
	DESIGN("디자인", Arrays.asList(
		JobField.GRAPHIC_DESIGNER,
		JobField.UI_UX_DESIGNER,
		JobField.THREEDIMENTION_DESIGNER,
		JobField.HARDWARE_DESIGNER,
		JobField.ETC_DESIGNER
	)),
	FRONT_END("프론트엔드 개발", Arrays.asList(
		JobField.IOS_DEVELOPER,
		JobField.ANDROID_DEVELOPER,
		JobField.WEB_FRONT_END_DEVELOPER,
		JobField.WEB_PUBLISHER,
		JobField.CROSS_PLATFORM_DEVELOPER
	)),
	BACK_END("백엔드 개발", Arrays.asList(
		JobField.WEB_SERVER_DEVELOPER,
		JobField.BLOCK_CHAIN_DEVELOPER,
		JobField.AI_DEVELOPER,
		JobField.GAME_SERVER
	)),
	BUSINESS("사업", Arrays.asList(
		JobField.BUSINESS_PLANNING,
		JobField.MARKETING,
		JobField.FINANCE_ACCOUNTING,
		JobField.SALES,
		JobField.STRATEGY_CONSULTING,
		JobField.INVESTMENT_ADVISOR,
		JobField.ETC_BUSINESS
	)),
	ETC("기타", Arrays.asList(
		JobField.WRITER_BLOGGER,
		JobField.INFLUENCER_STREAMER,
		JobField.LAW_LABOR,
		JobField.MEDICAL_MEDICINE,
		JobField.CATERING_CHEF,
		JobField.PRODUCER_CP,
		JobField.COMPOSING,
		JobField.VIDEO,
		JobField.OPERATE,
		JobField.QA,
		JobField.ETC
	));

	private String title;
	private List<JobField> detailList;

	public List<JobField> getDetailList() {
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

	public static JobCategory of(String code) {
		return EnumLookUpUtil.lookup(JobCategory.class, code, ErrorCode.INVALID_JOB_CATEGORY);
	}

}
