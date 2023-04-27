package kr.service.okr.user.service.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.service.okr.user.enums.JobCategory;
import kr.service.okr.user.usecase.user.JobInfo;
import kr.service.okr.user.usecase.user.QueryJobCategoryUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QueryJobCategory implements QueryJobCategoryUseCase {

	@Override
	public List<JobInfo> query() {
		return Arrays.stream(JobCategory.values())
			.map(jobCategory -> new JobInfo(jobCategory.name(), jobCategory.getTitle()))
			.toList();
	}
}
