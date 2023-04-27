package kr.service.okr.user.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.service.okr.user.enums.JobCategory;
import kr.service.okr.user.usecase.user.JobInfo;
import kr.service.okr.user.usecase.user.QueryJobFieldsUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryJobFields implements QueryJobFieldsUseCase {

	@Override
	public List<JobInfo> query(JobCategory jobCategory) {
		return jobCategory.getDetailList().stream()
			.map(jobField -> new JobInfo(jobField.name(), jobField.getTitle()))
			.toList();
	}
}
