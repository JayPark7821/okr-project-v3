package kr.service.okr.project.usecase;

import kr.service.okr.project.domain.Project;

public interface QueryProjectUseCase {

	Project queryProjectBy(Query query);

	record Query(
		String projectToken,
		Long userSeq
	) {
	}

}
