package kr.service.okr.user.usecase.user;

import java.util.List;

import kr.service.okr.user.enums.JobCategory;

public interface QueryJobFieldsUseCase {
	List<JobInfo> query(JobCategory jobCategory);
}
